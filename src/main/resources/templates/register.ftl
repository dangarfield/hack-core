<!DOCTYPE html>
<html lang="en">
<#include "snippets/head.ftl">
<body>

	<#include "snippets/nav.ftl">
		
	<div class="container">

		<div class="starter-template">
			
			<h1>Register</h1>
			
			<#if bindingResult??>
				<#list bindingResult.getFieldErrors() as error>
					<div class="alert alert-danger" role="alert">
				    <Strong>Error!</strong> ${error.getDefaultMessage()}
				    </div>
				</#list>
			</#if>
			
			<!--Logged user: <span sec:authentication="name">Bob</span>-->
			
			<form action="/register" method="post">
				<div class="form-group">
					<label for="register-name">Name</label>
					<input type="text" class="form-control" id="register-name" name="name" placeholder="Enter name"	<#if registrationForm.name ??>value="${registrationForm.name}"</#if>/>
				</div>
				
				<div class="form-group">
					<label for="register-email">Email address</label>
					<input type="email" class="form-control" id="register-email" name="email" placeholder="Enter email" <#if registrationForm.email ??>value="${registrationForm.email}"</#if>/>
				</div>
				<div class="form-group">
					<label for="register-password">Password</label> <input type="password" class="form-control" id="register-password" name="password" placeholder="Password" />
				</div>
				<div class="form-group">
					<label for="register-password-confirm">Password</label> <input type="password" class="form-control" id="register-password-confirm" name="passwordConfirm" placeholder="Password Confirm" />
				</div>
				<button type="submit" class="btn btn-primary">Register</button>
			</form>
			<a href="/login">Login</a>
		</div>

	</div>
	<!-- /.container -->

	<#include "snippets/scripts.ftl">
	
</body>
</html>
