<!DOCTYPE html>
<html lang="en">
<#include "snippets/head.ftl">
<body>

	<#include "snippets/nav.ftl">

	<div class="container">

		<div class="starter-template">
			<h1>Login</h1>
			<form action="/login" method="post">
				<div class="form-group">
					<label for="login-email">Email address</label>
					<input type="email" class="form-control" id="login-email" name="email" placeholder="Enter email" />
				</div>
				<div class="form-group">
					<label for="login-password">Password</label> <input type="password" class="form-control" id="login-password" name="password" placeholder="Password" />
				</div>
				<button type="submit" class="btn btn-primary">Login</button>
			</form>
			
			<a href="/register">Register</a>
		</div>

	</div>
	<!-- /.container -->


	<#include "snippets/scripts.ftl">
</body>
</html>
