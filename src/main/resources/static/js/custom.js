var H = H || {};
	
H.testResearch = (function(){

    var init = function(refresh){
    	//console.log("testResearch init")
    	$("button[data-action]").click(function() {
    		//console.log("click");
    		var action = $(this).attr('data-action');
    		$.post(action, function( data ) {
    			var alertClass = "success";
    			var alertTitle = "Woohoo!";
    			if(data.result == "ERROR"){
    				alertClass = "danger";
    				alertTitle = "Oh no!";
    			}
    			if(data.result == "WARNING"){
    				alertClass = "warning";
    				alertTitle = "Err!";
    			}
    			var html = "<div class=\"alert alert-"+alertClass+"\"><strong>"+alertTitle+"</strong> "+data.message+"</div>"; 
    			$( ".result" ).html(html);
			});
		});
    };

    return {
        init: init
    };

})();


window.H = H;


$(document).ready(function() {
    H.testResearch.init();
});