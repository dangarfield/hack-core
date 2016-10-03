<!DOCTYPE html>
<html lang="en">
<#include "snippets/head.ftl">
<body>

	<#include "snippets/nav.ftl">

	<div class="container app">

		<div class="result">
		</div>
		
		<div class="">
			<h1>App</h1>
			
			
			<ul>
				<li><strong>Name:</strong> ${player.name}</li>
				<li><strong>Email:</strong> ${player.email}</li>
				<li><strong>Money:</strong> ${player.money}</li>
				<li><strong>Research</strong></li>
				<ul>
					
					<#-- <li>${player.researchOfType('ONE')}</li> -->
					
					<#list player.researches as research>
						<li><strong>Research:</strong> ${research.type} - Level: ${research.level} <button type="button" class="btn btn-primary btn-sm" data-action="/api/start.training?type=${research.type}">Upgrade</button></li>
						<ul>
						<#list research.currentlyTraining as trainingResearch>
							<li><strong>Currently Training:</strong> Start: ${trainingResearch.startTime?datetime} - End: ${trainingResearch.endTime?datetime}</li>
							
						</#list>
						</ul>
					</#list>
				</ul>
				<li><strong>Locations</strong></li>
				<ul>
					<#list player.locationIps as ip>
						<li><strong>Location:</strong> ${ip}</li>
					</#list>
				</ul>
				<li><strong>Location Details</strong></li>
				<ul>
					<#list locations as location>
						<li><strong>Location:</strong> ${location.ip} - [${location.coord.x},${location.coord.y}]</li>
						<ul>
						<#list location.troopsOwn as troop>
							<li><strong>Troops:</strong> ${troop.type} - ${troop.noOfTroops}</li>
						</#list>
						</ul>
					</#list>
				</ul>
			</ul>
		</div>
	</div>
	<!-- /.container -->


	<#include "snippets/scripts.ftl">
</body>
</html>
