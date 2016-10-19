<!DOCTYPE html>
<html lang="en">
<#include "snippets/head.ftl">
<body>

	<#include "snippets/nav.ftl">

	<div class="container app">

		<div class="result">
		</div>
		
		<div class="">
			<h1>Topic</h1>
				<#if isPlayerInSyndicate>
				<ul>
					<li><strong>Syndicate:</strong> <a href="/app/syn/${syndicate.id}">${syndicate.name}</a></li>
					<li><strong>Topic:</strong> ${topic.title}</li>
					<li><strong>Last Amended:</strong> ${topic.lastAmended?datetime}</li>
	
					
					<li><strong>Posts</strong></li>
					<ul>
						<#list topic.posts as post>
						<li>${post.content} <br/><span class="tag tag-pill tag-default">${post.playerName}</span></li>
						</#list>
					</ul>
					<li><strong>Reply</strong>
						<#if topic.locked>
						<ul><li>This topic is now locked</li></ul>
						<#else>
						<form class="syndicate-discussion-post form ajax" action="/api/topic.add-post" method="POST">
							<div class="form-group">
								<textarea class="form-control" id="syndicate-add-post" name="content" rows="6" placeholder="Reply here"></textarea>
							</div>
							<input type="hidden" name="topicId" value="${topic.id}">
							<button type="submit" class="btn btn-primary">Save post</button>
						</form>
						</#if>
					</li>
					
					<#if isAdminInSyndicate>
						<li><strong>Remove topic: </strong> <button type="button" class="btn btn-primary btn-sm" data-action="/api/topic.remove-topic?id=${topic.id}">Remove Topic</button></li>
					</#if>
				
				</ul>
				
				
				
				<#else>
				<h2><strong>Access Denied:</strong> You do not have access to this thread</h2>
				</#if>
				
				
			</ul>
			
			
		</div>
	</div>
	<!-- /.container -->


	<#include "snippets/scripts.ftl">
</body>
</html>
