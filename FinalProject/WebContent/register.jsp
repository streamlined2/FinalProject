<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<style>
		.error { color:red }
	</style>
	<title>User registration form</title>
</head>
<body>
	<div class="error">${sessionScope.error}</div><br>
	<hr>
	<form action="main?action=login" method="post">
		<table>
		<tr>
			<td><label for="firstname">Firstname</label></td>
			<td><input type="text" id="firstname" name="firstname" value="" placeholder="Enter firstname here" size="50" pattern="[A-Za-zРђ-РЇР°-СЏРЃС‘Р„С”Р†С–Р‡С—]+" title="please enter letters only" required/></td>
		</tr>
		<tr>
			<td><label for="lastname">Lastname</label></td>
			<td><input type="text" id="lastname" name="lastname" value="" placeholder="Enter lastname here" size="50" pattern="[A-Za-zРђ-РЇР°-СЏРЃС‘Р„С”Р†С–Р‡С—]+" title="please enter letters only" required/></td>
		</tr>
		<tr>
			<td><label for="passport">Passport</label></td>
			<td><input type="text" id="passport" name="passport" value="" placeholder="Enter passport number here" size="50" pattern="[ 0-9A-Za-zРђ-РЇР°-СЏРЃС‘Р„С”Р†С–Р‡С—]+" title="please enter letters or digits" required/></td>
		</tr>
		<tr>
			<td><label for="user">Login</label></td>
			<td><input type="text" id="user" name="user" value="${param.user}" placeholder="Enter login here" size="30" required pattern="[0-9A-Za-zРђ-РЇР°-СЏРЃС‘Р„С”Р†С–Р‡С—]+" title="please enter letters or digits"/></td>
		</tr>
		<tr>
			<td><label for="password">Password</label></td>
			<td><input type="password" id="password" name="password" value="${param.password}" placeholder="Enter password here" size="30" required pattern="[0-9A-Za-zРђ-РЇР°-СЏРЃС‘Р„С”Р†С–Р‡С—]+" title="please enter letters or digits"/></td>
		</tr>
		<tr>
			<td><label for="password2">Repeat password</label></td>
			<td><input type="password" id="password2" name="password2" value="${param.password}" placeholder="Confirm password here" size="30" required pattern="[0-9A-Za-zРђ-РЇР°-СЏРЃС‘Р„С”Р†С–Р‡С—]+" title="please enter letters or digits"/></td>
		</tr>
		</table>
		<hr>
		<input type="submit" value="Register"/>
		<input type="reset" value="Clear" />
	</form>
</body>
</html>