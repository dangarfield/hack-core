var H = H || {};

H.generic = (function() {
	var init = function(refresh) {
		// console.log("generic init")
		$("button[data-action]").click(function() {
			var action = $(this).attr('data-action');
			$.post(action, function(data) {
				var alertClass = "success";
				var alertTitle = "Woohoo!";
				if (data.result == "ERROR") {
					alertClass = "danger";
					alertTitle = "Oh no!";
				}
				if (data.result == "WARNING") {
					alertClass = "warning";
					alertTitle = "Err!";
				}
				var html = "<div class=\"alert alert-" + alertClass
						+ "\"><strong>" + alertTitle + "</strong> "
						+ data.message + "</div>";
				$(".result").html(html);
			});
		});
		
		$("button[data-hide]").click(function() {
			var selector = $(this).attr('data-hide');
			$(selector).hide();
		});
		
		var recruitmentForm = $("form.ajax");
		recruitmentForm.submit(function(e) {
			$.ajax({
				type: recruitmentForm.attr('method'),
				url: recruitmentForm.attr('action'),
				data: recruitmentForm.serialize(),
				success: function (data) {
					var alertClass = "success";
					var alertTitle = "Woohoo!";
					if (data.result == "ERROR") {
						alertClass = "danger";
						alertTitle = "Oh no!";
					}
					if (data.result == "WARNING") {
						alertClass = "warning";
						alertTitle = "Err!";
					}
					var html = "<div class=\"alert alert-" + alertClass
							+ "\"><strong>" + alertTitle + "</strong> "
							+ data.message + "</div>";
					$(".result").html(html);
				}
	        });
			e.preventDefault();
		});
	};
	return {
		init : init
	};
})();
H.map = (function() {
	
	var locations; 
	var currentLocation;
	var data, map;
	var container, stats, locationPopup;
	var camera, scene, renderer, raycaster, controls, locationGroup, cubeCentre, offset;
	var mouse, INTERSECTED;
	
	var defaults = {};
	defaults.tileSize = 25;
	defaults.tileCount = 100;
	defaults.cameraHeight = 250;
	defaults.cameraOffset = 200;
	
	
	var init = function(refresh) {
		// console.log("generic init")
		map = $("body.map");
		if (map.length > 0) {
			console.log("Init map");
			getDataAndLoadScene();
			bindTakeOverAction();
		}
	};
	
	var getDataAndLoadScene = function() {
		loadPlayerLocations();
		console.log(currentLocation.ip);
		$.post("/api/map.location?ip="+currentLocation.ip, function(res) {
			var alertClass = "success";
			var alertTitle = "Woohoo!";
			console.log(res.message);
			if (res.result == "SUCCESS") {
				data = res.message;
				createScene();
				animate();
			}
			//TODO - Deal with failures
		});
	}
	
	var createScene = function() {
		container = map;
		//document.body.appendChild( container );
		var info = document.createElement( 'div' );
		info.style.position = 'absolute';
		info.style.top = '50px';
		info.style.width = '100%';
		info.style.textAlign = 'center';
		container.append( info );
		

		locationPopup = document.createElement( 'div' );
		locationPopup.className = "location-popup";
		container.append( locationPopup );
		
//		camera = new THREE.OrthographicCamera( window.innerWidth / - 2, window.innerWidth / 2, window.innerHeight / 2, window.innerHeight / - 2, 1, 1200 );
		camera = new THREE.PerspectiveCamera( 40, window.innerWidth / (window.innerHeight - 50), 1, 10000 );

		camera.position.x = defaults.cameraOffset;
		camera.position.y = defaults.cameraHeight;
		camera.position.z = defaults.cameraOffset;
		scene = new THREE.Scene();
		
		
		raycaster = new THREE.Raycaster();
		mouse = new THREE.Vector2();
		
		document.addEventListener( 'mousemove', onDocumentMouseMove, false );

		offset = {};
		offset.x = data.location.coord.x;
		offset.y = data.location.coord.y;
		console.log(offset);
		
		// Grid
		var size = defaults.tileCount * defaults.tileSize, step = defaults.tileSize;
		var geometry = new THREE.Geometry();
		for ( var i = 0; i <= size; i += step ) {
			//console.log(((size/2)) + " - 0 - " + i + " - " + (i - (size/2)));
			geometry.vertices.push( new THREE.Vector3( 0 -(size/2) + (defaults.tileSize/2) , 0, i -(size/2) + (defaults.tileSize/2)) );
			geometry.vertices.push( new THREE.Vector3( size-(size/2) + (defaults.tileSize/2) , 0, i -(size/2) + (defaults.tileSize/2) ) );
			geometry.vertices.push( new THREE.Vector3( i -(size/2) + (defaults.tileSize/2), 0, 0 -(size/2) + (defaults.tileSize/2) ) );
			geometry.vertices.push( new THREE.Vector3( i -(size/2) + (defaults.tileSize/2), 0, size-(size/2) + (defaults.tileSize/2)) );
		}
		var material = new THREE.LineBasicMaterial( { color: 0x999999, opacity: 1 } );
		var line = new THREE.LineSegments( geometry, material );
		scene.add( line );

		cubeCentre = new THREE.Vector3( 0, 0, 0 );
		console.log(cubeCentre);
		camera.lookAt(cubeCentre);
		
		// Cubes
		
		var geometry = new THREE.BoxGeometry( defaults.tileSize, defaults.tileSize, defaults.tileSize );
		var material = new THREE.MeshLambertMaterial( { color: 0xffffff, overdraw: 0.5 } );
		
		locationGroup = new THREE.Group();
		
		var playerMaterial = new THREE.MeshLambertMaterial( { color: 0xffffff, overdraw: 0.5 } );
		var otherPlayerMaterial = new THREE.MeshLambertMaterial( { color: 0xFF0000, overdraw: 0.5 } );
		var npcMaterial = new THREE.MeshLambertMaterial( { color: 0x0000CC, overdraw: 0.5 } );
		var unknownMaterial = new THREE.MeshLambertMaterial( { color: 0x000000, overdraw: 0.5 } );
		
		
		//console.log(data);
		for (var i = 0; i < data.items.length; ++i) {
			var location = data.items[i];
			var material, scale;
			switch(location.type) {
			    case "PLAYER":
			        material = new THREE.MeshLambertMaterial( { color: 0xffffff, overdraw: 0.5 } );
			        scale = 4;
			        console.log(location);
			        break;
			    case "OTHER_PLAYER":
			        material = new THREE.MeshLambertMaterial( { color: 0xFF0000, overdraw: 0.5 } );
			        scale = 1;
			        break;
			    case "NPC":
			        material = new THREE.MeshLambertMaterial( { color: 0x0000CC, overdraw: 0.5 } );
			        scale = 1;
			        break;
			    default:
			        material = new THREE.MeshLambertMaterial( { color: 0x000000, overdraw: 0.5 } );
			    scale = 1;
			}
			
			var cube = new THREE.Mesh( geometry, material );
			cube.scale.y = scale;
			cube.scale.x = 0.95;
			cube.scale.z = 0.95;
//			console.log(cube.scale)
			cube.position.y = ( cube.scale.y * defaults.tileSize ) / 2;
			cube.position.x = (location.x + offset.x) * defaults.tileSize;//* defaults.tileSize + (defaults.tileSize / 2);
			cube.position.z = (location.y + offset.y) * defaults.tileSize;//* defaults.tileSize + (defaults.tileSize / 2);
			cube.data = location;
		    
			locationGroup.add(cube);
			
			if(location.centrePoint) {
				cubeCentre = new THREE.Vector3( cube.position.x, cube.position.y, cube.position.z );
				console.log(cubeCentre);
				camera.lookAt(cubeCentre);
				
//				console.log(data.items[i]);
//			    console.log(location.x + " - " + location.y);
//			    console.log(cube.position.x + " - " + cube.position.z);
			}
		}

		scene.add(locationGroup);
		
		// Lights
		var ambientLight = new THREE.AmbientLight(  0xcccccc );
		scene.add( ambientLight );
		var directionalLight = new THREE.DirectionalLight( 0xffffff );
		directionalLight.position.x = Math.random() - 0.5;
		directionalLight.position.y = Math.random() - 0.5;
		directionalLight.position.z = Math.random() - 0.5;
		directionalLight.position.normalize();
		//scene.add( directionalLight );
		var directionalLight = new THREE.DirectionalLight( 0xffffff );
		directionalLight.position.x = Math.random() - 0.5;
		directionalLight.position.y = Math.random() - 0.5;
		directionalLight.position.z = Math.random() - 0.5;
		directionalLight.position.normalize();
		//scene.add( directionalLight );
		var directionalLight = new THREE.DirectionalLight( 0xffffff, 2 );
		directionalLight.position.set( 1, 0.75, 2 ).normalize();
		scene.add( directionalLight );
		var directionalLight2 = new THREE.DirectionalLight( 0xff00ff, 2 );
		directionalLight2.position.set( 0, 0.75, 2 ).normalize();
		//scene.add( directionalLight2 );
		
		// Renderer
//		renderer = new THREE.CanvasRenderer(map);
		renderer = new THREE.WebGLRenderer();
		renderer.setClearColor( 0xf0f0f0 );
		renderer.setPixelRatio( window.devicePixelRatio );
		var width = container.width();
		var height = container.height();
		console.log("width: " + width + " - Height: " + height);
		renderer.setSize( window.innerWidth, window.innerHeight - 50 );
		//renderer.setSize( window.innerWidth, window.innerHeight );
		container.append( renderer.domElement );
		stats = new Stats();
		container.append( stats.dom );
		//
		window.addEventListener( 'resize', onWindowResize, false );
		
		controls = new THREE.TrackballControls( camera );
		controls.rotateSpeed = 0.3;
		controls.zoomSpeed = 0.05;
		controls.panSpeed = 0.8;
		controls.noZoom = false;
		controls.noPan = false;
		controls.noRotate = true;
		controls.staticMoving = false;
		controls.dynamicDampingFactor = 0.3;
		controls.keys = [];
		controls.addEventListener( 'change', render );
		controls.minDistance = defaults.cameraHeight;
		controls.maxDistance = defaults.cameraHeight * 12;
//		console.log(controls);
		
	}
	var onDocumentMouseMove = function (event) {
		event.preventDefault();
//		mouse.x = ( event.clientX / renderer.domElement.clientWidth ) * 2 - 1;
//		mouse.y = - ( event.clientY / renderer.domElement.clientHeight ) * 2 + 1;
		
//	    mouse.x = ( ( event.clientX - renderer.domElement.offsetLeft ) / renderer.domElement.width ) * 2 - 1;
//	    mouse.y = - ( ( event.clientY - renderer.domElement.offsetTop ) / renderer.domElement.height ) * 2 + 1;
	    
//	    var canvasPosition = renderer.domElement.getBoundingClientRect();
//
//	    mouse.x = event.clientX - canvasPosition.left;
//	    mouse.y = event.clientY - canvasPosition.top;

		var mouseOverDiv = $('.location-popup:hover').length + $('.overlay:hover').length != 0;
		console.log(mouseOverDiv);
		
		if(!mouseOverDiv) {
			mouse.x = ( event.clientX / window.innerWidth ) * 2 - 1;
			mouse.y = - ( event.clientY / window.innerHeight ) * 2 + 1;
		    
			raycaster.setFromCamera( mouse, camera );	
			var intersects = raycaster.intersectObjects( locationGroup.children );

			if ( intersects.length > 0 ) {
				if ( INTERSECTED != intersects[ 0 ].object ) {
					if ( INTERSECTED ) {
						INTERSECTED.material.color.setHex( INTERSECTED.currentHex );
					}
					INTERSECTED = intersects[ 0 ].object;
					console.log(INTERSECTED);
					console.log(INTERSECTED.data.ip);
					
					var secondsTotal = INTERSECTED.data.distance*60*30;
					var hoursDisplay = Math.trunc(secondsTotal / 60 / 60);
					var minutesDisplay = Math.trunc((secondsTotal - (hoursDisplay * 60 * 60)) / 60);
					var secondsDisplay = Math.trunc(secondsTotal - (hoursDisplay * 60 * 60) - (minutesDisplay * 60));
					
					hoursDisplay = hoursDisplay < 10 ? '0' + hoursDisplay : hoursDisplay;
					minutesDisplay = minutesDisplay < 10 ? '0' + minutesDisplay : minutesDisplay;
					secondsDisplay = secondsDisplay < 10 ? '0' + secondsDisplay : secondsDisplay;

					var html = "";
					html += "<p>"+INTERSECTED.data.ip+" - Takeover time: "+ hoursDisplay+":"+minutesDisplay+":"+secondsDisplay+"</p>";
					html += "<button data-action=\"/api/attack.scan?ip="+INTERSECTED.data.ip+"\">Scan</button>";
					html += "<button data-action=\"/api/attack.steal-money?ip="+INTERSECTED.data.ip+"\">Attack</button>";
					html += "<button data-takeover-popup=\""+INTERSECTED.data.ip+"\">Takeover</button>";
					html += "<p class=\"result\"></p>";
					
					$(".location-popup").html(html);
					$(".overlay .heading").html("<p>"+INTERSECTED.data.ip+" - Takeover time: "+ hoursDisplay+":"+minutesDisplay+":"+secondsDisplay+"</p>");
					$(".overlay .takeover-action").attr("data-takeover-targetIp",INTERSECTED.data.ip);
					bindTakeOverPopup();
//					$(".location-popup button").click(function() {
//						console.log("CLICKED: " + $(this).attr("data"));
//					});
					H.generic.init();
					INTERSECTED.currentHex = INTERSECTED.material.color.getHex();
					INTERSECTED.material.color.setHex( 0xffff00 );
				}
			} else {
//				if ( INTERSECTED ) {
//					INTERSECTED.material.color.setHex( INTERSECTED.currentHex );
//				}
//				INTERSECTED = null;
			}
		}
		
		//console.log(mouse.x + " - " + mouse.y)
	}

	var bindTakeOverPopup = function() {
		$("button[data-takeover-popup]").click(function() {
			var ip = $(this).attr('data-takeover-popup');
			console.log(currentLocation);
			console.log(ip);
			$(".overlay").show();
		});
	}
	
	var bindTakeOverAction = function() {
		$("button.takeover-action").click(function() {
			
			var url = $(this).attr("data-takeover-baseurl") + "?";
			var params = [];
			var troops = []; 
			$(".overlay input.troop").each(function() {
				var type = $(this).attr("id");
				var val = $(this).val();
				var troop = type + "___" + val;
				troops.push(troop);
			})
			var ceo = $(".overlay input#ceo").val();
			console.log("CEO - " + ceo);
			params.push("troops="+troops.join("-_-"));
			params.push("ceo="+ceo);
			params.push("sourceIp="+$(this).attr("data-takeover-sourceip"));
			params.push("targetIp="+$(this).attr("data-takeover-targetip"));
			url += params.join("&");
			
			$.post(url, function(data) {
				var alertClass = "success";
				var alertTitle = "Woohoo!";
				if (data.result == "ERROR") {
					alertClass = "danger";
					alertTitle = "Oh no!";
					var html = "<div class=\"alert alert-" + alertClass
					+ "\"><strong>" + alertTitle + "</strong> "
					+ data.message + "</div>";
					$(".result").html(html);
				} else if (data.result == "WARNING") {
					alertClass = "warning";
					alertTitle = "Err!";
					var html = "<div class=\"alert alert-" + alertClass
					+ "\"><strong>" + alertTitle + "</strong> "
					+ data.message + "</div>";
					$(".result").html(html);
				} else {
					var html = "<div class=\"alert alert-" + alertClass
					+ "\"><strong>" + alertTitle + "</strong> "
					+ data.message + "</div>";
					$(".result").html(html);
					for ( var i in troops) {
						var troopSplit = troops[i].split("___");
						var type = troopSplit[0];
						var val = troopSplit[1];
						var existingVal = $("#"+type).attr('max');
						var newVal = existingVal - val;
		
						$("#"+type).val(newVal);
						$("#"+type).attr('max',newVal);
						updateTakeoverUrl(type,newVal);
						console.log(type + " - " + val + " - " + existingVal + " - " + newVal);
					}
				}
				
			});
		});
	}
	var onWindowResize = function() {
		camera.left = window.innerWidth / - 2;
		camera.right = window.innerWidth / 2;
		camera.top = window.innerHeight / 2;
		camera.bottom = window.innerHeight / - 2;
		camera.updateProjectionMatrix();
		renderer.setSize( window.innerWidth, window.innerHeight );
		controls.handleResize();
	}
	//
	var animate = function() {
		requestAnimationFrame( animate );
		controls.update();
		stats.begin();
		render();
		stats.end();
	}
	var render = function() {
		var timer = Date.now() * 0.00005;
		//var timer = 147582623.4000;
		camera.position.x = Math.cos( timer ) * defaults.cameraHeight;
		camera.position.z = Math.sin( timer ) * defaults.cameraHeight;
		camera.lookAt( cubeCentre );
		
		
		
		renderer.render( scene, camera );
	}
	
	var loadPlayerLocations = function() {
		locations = H.data.locations;
		currentLocation = H.data.currentLocation;
	}
	
	var updateTakeoverUrl = function(type, val) {
		$('#'+type+"output").val(val);
	}
	return {
		init : init,
		updateTakeoverUrl : updateTakeoverUrl
	};
})();

window.H = H;

$(document).ready(function() {
	H.generic.init();
	H.map.init();
});