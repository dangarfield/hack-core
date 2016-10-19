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
				
				<li>
					<strong>Syndicate:</strong>
					<#if player.syndicateId??>
						<a href="/app/syn/${player.syndicateId}">${player.syndicateName}</a> <button type="button" class="btn btn-primary btn-sm" data-action="/api/syndicate.leave?name=${player.id}">Leave syndicate</button>
					<#else>
						<form class="syndicate-create form-inline ajax" action="/api/syndicate.create" method="POST">
							<div class="form-group">
								<!--<label for="recruitment-no">Example label</label>-->
								<input type="text" class="form-control" id="syndicate-name" name="name" placeholder="My Syndicate">
							</div>
							<button type="submit" class="btn btn-primary">Create syndicate</button>
						</form>
					</#if>
				</li>
				<li><strong>Email:</strong> ${player.email}</li>
				<li><strong>Money:</strong> ${player.money}</li>
				<li><strong>Research</strong></li>
				<ul>
					
					<#-- <li>${player.researchOfType('ONE')}</li> -->
					
					<#list player.researches?sort_by("type") as research>
						<li><strong>Research:</strong> ${research.type} - Level: ${research.level} <button type="button" class="btn btn-primary btn-sm" data-action="/api/research.start?type=${research.type}">Upgrade</button></li>
						<ul>
						<#list research.currentlyTraining as trainingResearch>
							<li><strong>Currently Training:</strong> Finished in: ${(trainingResearch.endTime?long - now?long)?number_to_date?string('HH:mm:ss')}</li>
							
						</#list>
						</ul>
					</#list>
				</ul>
				<li><strong>Steal Cooldown</strong> (${player.logs.stealMoneyAttackCooldown?size})</li>
				<ul>
					<#list player.logs.stealMoneyAttackCooldown as targetLocation>
						<li><strong>Target:</strong> ${targetLocation.targetIp} - Cooldown time: ${(targetLocation.time?long - now?long)?number_to_date?string('HH:mm:ss')}</li>
					</#list>
				</ul>
				<li><strong>Takeover Results</strong> (${player.logs.takeoverLogs?size})</li>
				<ul>
					<#list player.logs.takeoverLogs as takeover>
						<li><strong>Battle Report:</strong> ${takeover}</li>
					</#list>
				</ul>
				<li><strong>CEOs</strong> (${player.ceoCount})</li>
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
						<#list location.defenseIn as troop>
							<li><strong>Defense In:</strong> ${troop.type} - ${troop.noOfTroops} from ${troop.source} to ${troop.target} <button type="button" class="btn btn-primary btn-sm" data-action="/api/attack.defense-recall?sourceIp=${troop.source}&targetIp=${troop.target}&types=${troop.type}">Recall this</button> <button type="button" class="btn btn-primary btn-sm" data-action="/api/attack.defense-recall?sourceIp=${troop.source}&targetIp=${troop.target}&types=FIREWALL&types=PHYSICAL&types=DATA">Recall all from ${troop.source}</button></li>
						</#list>
						<#list location.defenseOut as troop>
							<li><strong>Defense Out:</strong> ${troop.type} - ${troop.noOfTroops} from ${troop.source} to ${troop.target} <button type="button" class="btn btn-primary btn-sm" data-action="/api/attack.defense-recall?sourceIp=${troop.source}&targetIp=${troop.target}&types=${troop.type}">Recall this</button> <button type="button" class="btn btn-primary btn-sm" data-action="/api/attack.defense-recall?sourceIp=${troop.source}&targetIp=${troop.target}&types=FIREWALL&types=PHYSICAL&types=DATA">Recall all at ${troop.target}</button></li>
						</#list>
						<#list location.defenseTransitIn as troop>
							<li><strong>Defense Transit In:</strong> ${troop.type} - ${troop.noOfTroops} from ${troop.source} to ${troop.target} in ${(troop.arrival?long - now?long)?number_to_date?string('HH:mm:ss')}</li>
						</#list>
						<#list location.defenseTransitOut as troop>
							<li><strong>Defense Transit Out:</strong> ${troop.type} - ${troop.noOfTroops} from ${troop.source} to ${troop.target} in ${(troop.arrival?long - now?long)?number_to_date?string('HH:mm:ss')}</li>
						</#list>
						<#list location.attackTransitIn as troop>
							<li><strong>Attack Transit In:</strong> ${troop.type} - ${troop.noOfTroops} from ${troop.source} to ${troop.target} in ${(troop.arrival?long - now?long)?number_to_date?string('HH:mm:ss')}</li>
						</#list>
						<#list location.attackTransitOut as troop>
							<li><strong>Attack Transit Out:</strong> ${troop.type} - ${troop.noOfTroops} from ${troop.source} to ${troop.target} in ${(troop.arrival?long - now?long)?number_to_date?string('HH:mm:ss')}</li>
						</#list>
						<#list location.returning as troop>
							<li><strong>Returning:</strong> ${troop.type} - ${troop.noOfTroops} from ${troop.source} to ${troop.target} in ${(troop.arrival?long - now?long)?number_to_date?string('HH:mm:ss')}</li>
						</#list>
						<#list location.recruiting as troop>
							<li><strong>Recruiting:</strong> ${troop.type} - ${troop.noOfTroops} (every ${troop.recruitmentTime} seconds)</li>
						</#list>
						</ul>
					</#list>
				</ul>
				<li><strong>Missions</strong> (Level ${player.researchOfType('MISSIONS_LEVEL').level})</li>
				<ul>
					<#list missionTypes as missionType>
						<li><strong>Mission:</strong> ${missionType} - ${missionType?index+1}
						<#if missionType?index+1 <= player.researchOfType('MISSIONS_LEVEL').level>
							
							<#if player.missionOfType(missionType)??>
								<button type="button" class="btn btn-primary btn-sm" data-action="/api/mission.start?type=${missionType}" disabled="disabled">Currently Learning</button>
								Complete in ${(player.missionOfType(missionType).endTime?long - now?long)?number_to_date?string('HH:mm:ss')}
							<#else>
								<button type="button" class="btn btn-primary btn-sm" data-action="/api/mission.start?type=${missionType}">Start Mission</button>
							</#if>
						</#if>
  						</li>
					</#list>
				</ul>
				<li><strong>Mission Logs</strong> (${player.logs.missionLogs?size})</li>
				<ul>
					<#list player.logs.missionLogs as log>
						<li><strong>${log.type}:</strong> ${log.message} - ${log.time?datetime}</li>
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
