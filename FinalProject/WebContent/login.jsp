<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<style>
		.error { color:red }
	</style>
	<title>User authentication form</title>
</head>
<body>
	<p><div class="error">${sessionScope.error}</div><p>
	<hr>
	<form action="main" method="post">
		<label for="client">Client</label>
		<input type="radio" id="client" name="role" value="client" checked/>
		<label for="manager">Manager</label>
		<input type="radio" id="manager" name="role" value="manager"/>
		<label for="admin">Administrator</label>
		<input type="radio" id="admin" name="role" value="admin"/><br>

		<div>
		<table>
		<tr>
			<td><label for="user">Login</label></td>
			<td><input type="text" id="user" name="user" value="" placeholder="Enter login here" size="30" required pattern="${requestScope.loginPattern}" title="please enter letters or digits"/></td>
		</tr>
		<tr>
			<td><label for="password">Password</label></td>
			<td><input type="password" id="password" name="password" value="" placeholder="Enter password here" size="30" required pattern="${requestScope.passwordPattern}" title="please enter letters or digits"/></td>
		</tr>
		</table>
		<hr>
		</div>
		<div>
			<input type="submit" value="Login" formaction="main?action=login" formmethod="post"/>
			<input type="reset" value="Clear" />
			<input type="submit" value="Register" formaction="main?action=register" formmethod="post"/>
		</div>
	</form>
</body>
</html>