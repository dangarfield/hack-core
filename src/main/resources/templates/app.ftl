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
					
					<#list player.researches?sort_by("type") as research>
						<li><strong>Research:</strong> ${research.type} - Level: ${research.level} <button type="button" class="btn btn-primary btn-sm" data-action="/api/research.start?type=${research.type}">Upgrade</button></li>
						<ul>
						<#list research.currentlyTraining as trainingResearch>
							<li><strong>Currently Training:</strong> Start: ${trainingResearch.startTime?datetime} - End: ${trainingResearch.endTime?datetime}</li>
							
						</#list>
						</ul>
					</#list>
				</ul>
				<li><strong>Steal Cooldown</strong> (${player.stealMoneyAttackCooldown?size})</li>
				<ul>
					<#list player.stealMoneyAttackCooldown as targetLocation>
						<li><strong>Target:</strong> ${targetLocation.targetIp} - Cooldown time: ${(targetLocation.time?long - .now?long)?number_to_date?string('HH:mm:ss')}</li>
					</#list>
				</ul>
				<li><strong>Locations</strong> (${player.locationIps?size})</li>
				<ul>
					<#list player.locationIps as ip>
						<li><strong>Location:</strong> ${ip}</li>
					</#list>
				</ul>
				<li><strong>Location Details</strong> (${locations?size})</li>
				<ul>
					<#list locations as location>
						<li><strong>Location:</strong> ${location.ip} - [${location.coord.x},${location.coord.y}]</li>
						<ul>
						<#list location.defense as troop>
							<li><strong>Defense:</strong> ${troop.type} - ${troop.noOfTroops}</li>
						</#list>
						<#list location.attackOut as troop>
							<li><strong>Attack Out:</strong> ${troop.type} - ${troop.noOfTroops}</li>
						</#list>
						<#list location.attackIn as troop>
							<li><strong>Attack In:</strong> ${troop.type} - ${troop.noOfTroops}</li>
						</#list>
						<#list location.recruiting as troop>
							<li><strong>Recruiting:</strong> ${troop.type} - ${troop.noOfTroops} (every ${troop.recruitmentTime} seconds)</li>
						</#list>
						</ul>
					</#list>
				</ul>
			</ul>
			
			<form class="recruitment form-inline ajax" action="/api/recruitment.start" method="POST">
				<p><strong>Recruitment</strong></p>
				<div class="form-group">
					<!--<label for="recruitment-ip">Example select</label>-->
					<select class="form-control" id="recruitment-ip" name="ip">
						<#list locations as location>
							<option>${location.ip}</option>
						</#list>
					</select>
				</div>
				<div class="form-group">
					<!--<label for="recruitment-type">Example select</label>-->
					<select class="form-control" id="recruitment-type" name="type">
						<#list troopTypes as troopType>
							<option>${troopType}</option>
						</#list>
					</select>
				</div>
				<div class="form-group">
					<!--<label for="recruitment-no">Example label</label>-->
					<input type="text" class="form-control" id="recruitment-no" name="no" value="10">
				</div>
				<button type="submit" class="btn btn-primary">Recruit</button>
			</form>
		</div>
	</div>
	<!-- /.container -->


	<#include "snippets/scripts.ftl">
</body>
</html>
