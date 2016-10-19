<!DOCTYPE html>
<html lang="en">
<#include "snippets/head.ftl">
<body>

	<#include "snippets/nav.ftl">

	<div class="container app">

		<div class="result">
		</div>
		
		<div class="">
			<h1>Syndicate</h1>
			
			<ul>
				<li><strong>ID:</strong> ${syndicate.id}</li>
				<li><strong>Name:</strong> ${syndicate.name}</li>
				<li><strong>Description:</strong> <#if syndicate.description??>${syndicate.description}<#else><i>Description not yet written</i></#if></li>
				<li><strong>Is player:</strong> <#if isPlayerInSyndicate>Yes <button type="button" class="btn btn-primary btn-sm" data-action="/api/syndicate.leave?playerId=${player.id}">Leave</button><#else>No</#if></li>
				<#if isPlayerInSyndicate>
				<li><strong>Topics</strong>
				<ul>
					<#list syndicate.topics?sort_by('lastAmended')?reverse as topic>
					<li><strong><a href="/app/syn/${syndicate.id}/topic/${topic.id}">${topic.title}</a></strong> - ${topic.lastAmended?datetime} - ${topic.locked?string('Locked','Open')}</li>
					</#list>
					<li><strong>New topic:</strong>
					<#if isAdminInSyndicate>
						<form class="syndicate-new-topic form-inline ajax" action="/api/topic.add-topic" method="POST">
							<div class="form-group">
								<input type="text" class="form-control" id="syndicate-disband" name="title" placeholder="Topic title">
							</div>
							<button type="submit" class="btn btn-primary">Add topic</button>
						</form>
					<#else>
						Only admins can create new topics
					</#if>
					</li>
				</ul>
				</li>
					
				<#else>
				<li><strong>Join: </strong>
					<#if syndicate.applicants?seq_contains(player.id)>
						You have already applied to join
					<#else>
						<button type="button" class="btn btn-primary btn-sm" data-action="/api/syndicate.join-request?id=${syndicate.id}">Request to join syndicate</button>
					</#if>
					

				</li>
				</#if>
				
				
				
				
				<li><strong>Is admin:</strong> ${isAdminInSyndicate?string('Yes','No')}</li>
				<#if isAdminInSyndicate && applicants?? && players??>
				<li><strong>Players</strong></li>
				<ul>
					<#list players as player>
					<li><strong>Player:</strong>
						${player.name} - IPs: ${player.locationIps?size}
						<button type="button" class="btn btn-primary btn-sm" data-action="/api/syndicate.leave?playerId=${player.id}">Kick out</button>
						<button type="button" class="btn btn-primary btn-sm" data-action="/api/syndicate.set-admin?playerId=${player.id}&admin=true">Make admin</button>
					</li>
					</#list>
				</ul>
				<li><strong>Admins</strong></li>
				<ul>
					<#list admins as admin>
					<li><strong>Player:</strong>
						${admin.name} - IPs: ${admin.locationIps?size}
						<button type="button" class="btn btn-primary btn-sm" data-action="/api/syndicate.set-admin?playerId=${admin.id}&admin=false">Revoke admin</button>
					</li>
					</#list>
				</ul>
				<li><strong>Applicants</strong></li>
				<ul>
					<#list applicants as applicant>
					<li><strong>Applicant:</strong>
						${applicant.name} - IPs: ${applicant.locationIps?size}
						<button type="button" class="btn btn-primary btn-sm" data-action="/api/syndicate.join-approval?playerId=${applicant.id}&approve=true">Approve</button>
						<button type="button" class="btn btn-primary btn-sm" data-action="/api/syndicate.join-approval?playerId=${applicant.id}&approve=false">Reject</button>
					</li>
					</#list>
				</ul>
				<li><strong>Description</strong>
					<form class="syndicate-description form ajax" action="/api/syndicate.edit-description" method="POST">
						<div class="form-group">
							<textarea class="form-control" id="syndicate-description" name="description" rows="6" placeholder="Edit me"><#if syndicate.description??>${syndicate.description}</#if></textarea>
						</div>
						<button type="submit" class="btn btn-primary">Save description</button>
					</form>
				</li>
				<li><strong>Disband:</strong>
					<form class="syndicate-disband form-inline ajax" action="/api/syndicate.disband" method="POST">
						<div class="form-group">
							<input type="text" class="form-control" id="syndicate-disband" name="disband" placeholder="To disband, type 'disband'">
						</div>
						<button type="submit" class="btn btn-primary">Disband syndicate</button>
					</form>
				</li>
				</#if>
			</ul>
			
			
		</div>
	</div>
	<!-- /.container -->


	<#include "snippets/scripts.ftl">
</body>
</html>
