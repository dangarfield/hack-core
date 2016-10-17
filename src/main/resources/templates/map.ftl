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
	<div class="overlay takeover" style="display:none">
		<p class="heading"></p>
		<p>Troop Selection</p>
		<#list location.defense as troop>
		    <p>
		    	<label for="${troop.type}takeover">${troop.type}</label>
		    	<input class="troop" type="range" min="0" max="${troop.noOfTroops}" value="${troop.noOfTroops}" id="${troop.type}takeover" data-type="${troop.type}" oninput="H.map.updateTroopSliderOutput('${troop.type}takeover',value)">
		    	<output for="${troop.type}takeover" id="${troop.type}takeoveroutput">${troop.noOfTroops}</output>
		    </p>
		</#list>
		<label for="ceo">CEO</label>
		<input type="range" min="0" max="${player.ceoCount}" value="${player.ceoCount}" id="ceotakeover" oninput="H.map.updateTroopSliderOutput('ceotakeover',value)">
		<output for="ceo" id="ceotakeoveroutput">${player.ceoCount}</output>
		<br/>
		<button data-hide=".overlay">Close</button>
		<button class="takeover-action" data-hide=".overlays" data-takeover-baseUrl="/api/attack.takeover" data-takeover-sourceIp="${location.ip}" data-takeover-targetIp="">Launch Take Over Attack</button>
	</div>
	<div class="overlay defense" style="display:none">
		<p class="heading"></p>
		<p>Troop Selection</p>
		<#list location.defense as troop>
		    <p>
		    	<label for="${troop.type}">${troop.type}</label>
		    	<input class="troop" type="range" min="0" max="${troop.noOfTroops}" value="${troop.noOfTroops}" id="${troop.type}defense" data-type="${troop.type}" oninput="H.map.updateTroopSliderOutput('${troop.type}defense',value)">
		    	<output for="${troop.type}defense" id="${troop.type}defenseoutput">${troop.noOfTroops}</output>
		    </p>
		</#list>
		<br/>
		<button data-hide=".overlay">Close</button>
		<button class="defense-action" data-hide=".overlays" data-defense-baseUrl="/api/attack.defense" data-defense-sourceIp="${location.ip}" data-defense-targetIp="">Launch Defense</button>
	</div>
	<#include "snippets/scripts.ftl">
</body>
</html>
