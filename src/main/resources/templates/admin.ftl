<!DOCTYPE html>
<html lang="en">
<#include "snippets/head.ftl">
<body>

	<#include "snippets/nav.ftl">

	<div class="container app">

		<div class="result">
		</div>
		
		<div class="">
			<h1>Admin</h1>
			
			<ul>
				<li><button type="button" class="btn btn-primary btn-sm" data-action="/admin/api/data.remove-all">Clear Data</button></li>
				<li><button type="button" class="btn btn-primary btn-sm" data-action="/admin/api/player.create-test-users?no=10">Create 10 test users</button></li>
				<li><button type="button" class="btn btn-primary btn-sm" data-action="/admin/api/player.create-dg-user">Create d@g.com user</button></li>
				<li><button type="button" class="btn btn-primary btn-sm" data-action="/admin/api/restart.akka-jobs">Restart Akka Jobs</button></li>
				<li><button type="button" class="btn btn-primary btn-sm" data-action="/admin/api/passive-money.start">Trigger Passive Money Collection</button></li>
			</ul>
		</div>
	</div>
	<!-- /.container -->


	<#include "snippets/scripts.ftl">
</body>
</html>
