<!DOCTYPE html>
<html lang="en">
<#include "snippets/head.ftl">
<body>

	<#include "snippets/nav.ftl">

	<div class="container app">

		<div class="result">
		</div>
		
		<div class="">
			<h1>Steal Money</h1>
			<ul>
				<li><strong>Money:</strong> ${player.money}</li>
				<li><strong>MONEY_ATTACK:</strong> ${player.researchOfType('MONEY_ATTACK').level}</li>
				<li><strong>RESEARCH_HIDE:</strong> ${player.researchOfType('RESEARCH_HIDE').level}</li>
			</ul>
			<ul>
				<#list targetLocations as targetLocation>
					<li>
						${targetLocation.location.ip} - Hide level: ${targetLocation.location.researchHideLevel} - Cooldown: <#if targetLocation.cooldownTime??>${targetLocation.cooldownTime?string('HH:mm:ss')}<#else>None</#if>
						<button type="button" class="btn btn-primary btn-sm" data-action="/api/attack.scan?ip=${targetLocation.location.ip}">Scan</button>
						<button type="button" class="btn btn-primary btn-sm" data-action="/api/attack.steal-money?ip=${targetLocation.location.ip}">Attack</button>
					</li>
				</#list>
			</ul>
		</div>
	</div>
	<!-- /.container -->


	<#include "snippets/scripts.ftl">
</body>
</html>
