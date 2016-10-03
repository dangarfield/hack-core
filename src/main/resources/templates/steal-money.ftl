<!DOCTYPE html>
<html lang="en">
<#include "snippets/head.ftl">
<body>

	<#include "snippets/nav.ftl">

	<div class="container app">

		<div class="result">
		</div>
		
		<div class="">
			<h1>Make Money</h1>
			<ul>
				<li><strong>Money:</strong> ${player.money}</li>
				<li><strong>MONEY_ATTACK:</strong> ${player.researchOfType('MONEY_ATTACK').level}</li>
				<li><strong>RESEARCH_HIDE:</strong> ${player.researchOfType('RESEARCH_HIDE').level}</li>
			</ul>
			<ul>
				<#list targetLocations as location>
					<li>
						${location.ip} - Hide level: ${location.researchHideLevel}
						<button type="button" class="btn btn-primary btn-sm" data-action="/api/attack.scan?ip=${location.ip}">Scan</button>
						<button type="button" class="btn btn-primary btn-sm" data-action="/api/attack.steal-money?ip=${location.ip}">Attack</button>
					</li>
				</#list>
			</ul>
		</div>
	</div>
	<!-- /.container -->


	<#include "snippets/scripts.ftl">
</body>
</html>
