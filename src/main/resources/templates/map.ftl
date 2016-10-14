<!DOCTYPE html>
<html lang="en">
<#include "snippets/head.ftl">

<title><#if pageTitle??>${pageTitle}</#if> | Hack Core</title>

<style>
	body {
		font-family: Monospace;
		background-color: #f0f0f0;
		margin: 0px;
		overflow: hidden;
		padding: 0;
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
		top: 50px;
		left:0;
		cursor: pointer;
		z-index: 10000;
		width: 100%;
		height: 100%;
		text-align: center;
		background: black;
		color: white;
	}
	.selector {
		position: fixed;
		top: 0;
		left:0;
		cursor: pointer;
		height: 50px;
		z-index: 10000;
		width: 100%;
		height: 50px;
		text-align: center;
		background: black;
		color: white;
	}
	.selector a {
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
	
	<div class="selector">
		<span class="prev">
		<#if prev??><a href="/app/map?ip=${prev}">Prev</a></#if>
		</span>
		<span class="current">${location.ip}</span>
		<span class="next">
		<#if next??><a href="/app/map?ip=${next}">Next</a></#if>
		</span>
	</div>
	<div class="overlay" style="display:none">
		<p class="heading"></p>
		<p>Troop Selection</p>
		<#list location.defense as troop>
		    <p>
		    	<label for="${troop.type}">${troop.type}</label>
		    	<input class="troop" type="range" min="0" max="${troop.noOfTroops}" value="${troop.noOfTroops}" id="${troop.type}" oninput="H.map.updateTakeoverUrl('${troop.type}',value)">
		    	<output for="${troop.type}" id="${troop.type}output">${troop.noOfTroops}</output>
		    </p>
		</#list>
		<label for="ceo">CEO</label>
		<input type="range" min="0" max="${player.ceoCount}" value="${player.ceoCount}" id="ceo" oninput="H.map.updateTakeoverUrl('ceo',value)">
		<output for="ceo" id="ceooutput">${player.ceoCount}</output>
		<br/>
		<button data-hide=".overlay">Close</button>
		<button class="takeover-action" data-hide=".overlays" data-takeover-baseUrl="/api/attack.takeover" data-takeover-sourceIp="${locations?first.ip}" data-takeover-targetIp="">Launch Take Over Attack</button>
	</div>
	<#include "snippets/scripts.ftl">
</body>
</html>
