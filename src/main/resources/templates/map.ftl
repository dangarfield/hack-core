<!DOCTYPE html>
<html lang="en">
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<meta name="description" content="" />
<meta name="author" content="" />
<link rel="icon" href="/img/favicon.ico"></link>

<title><#if pageTitle??>${pageTitle}</#if> | Hack Core</title>

<style>
	body {
		font-family: Monospace;
		background-color: #f0f0f0;
		margin: 0px;
		overflow: hidden;
	}
	
	.location-popup {
		position: fixed;
		bottom: 0;
		left:0;
		cursor: pointer;
		height: 100px;
		z-index: 10000;
		width: 100%;
		text-align: center;
		background: black;
		color: white;
	}
	.overlay {
		position: fixed;
		top: 0;
		left:0;
		cursor: pointer;
		height: 50px;
		z-index: 10000;
		width: 100%;
		height: 100%;
		text-align: center;
		background: black;
		color: white;
	}
</style>
<body class="map">
	<script>
		var H = H || {};
		H.data = {};
		H.data.locations = ${locationsJson};
		H.data.currentLocation = ${locationJson};
		window.H = H;
		

	</script>
	
	
	<div class="overlay" style="display:none">
		<p class="heading"></p>
		<p>Troop Selection</p>
		<#list locations?first.defense as troop>
		    <p>
		    	<label for="${troop.type}">${troop.type}</label>
		    	<input type="range" min="0" max="${troop.noOfTroops}" value="${troop.noOfTroops}" id="${troop.type}" oninput="H.map.updateTakeoverUrl('${troop.type}',value)">
		    	<output for="${troop.type}" id="${troop.type}output">${troop.noOfTroops}</output>
		    </p>
		</#list>
		
		<button data-hide=".overlay">Close</button>
		<button class="takeover-action" data-hide=".overlays" data-takeover-baseUrl="/api/attack.takeover" data-takeover-sourceIp="${locations?first.ip}" data-takeover-targetIp="">Launch Take Over Attack</button>
	</div>
	<#include "snippets/scripts.ftl">
</body>
</html>
