<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>User registration form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	
	<form>
		<table>
		<tr>
			<td><label for="firstname">Firstname</label></td>
			<td><input type="text" id="firstname" name="firstname" value="" placeholder="Enter firstname here" size="50" pattern="${requestScope.namePattern}" title="please enter letters only" required/></td>
		</tr>
		<tr>
			<td><label for="lastname">Lastname</label></td>
			<td><input type="text" id="lastname" name="lastname" value="" placeholder="Enter lastname here" size="50" pattern="${requestScope.namePattern}" title="please enter letters only" required/></td>
		</tr>
<!-- 
		<tr>
			<td><label for="passport">Passport</label></td>
			<td><input type="text" id="passport" name="passport" value="" placeholder="Enter passport number here" size="50" pattern="${requestScope.passportPattern}" title="please enter letters or digits" required/></td>
		</tr>
		<tr> -->
			<td><label for="role">Role</label></td>
			<td>
				<select name="role" id="role">
					<option value="client">Client</option>
					<option value="manager">Manager</option>
					<option value="admin">Administrator</option>
				</select>
			</td>
		</tr>
		<tr>
			<td><label for="user">Login</label></td>
			<td><input type="text" id="user" name="user" value="${param.user}" placeholder="Enter login here" size="30" required pattern="${requestScope.loginPattern}" title="please enter letters or digits"/></td>
		</tr>
		<tr>
			<td><label for="password">Password</label></td>
			<td><input type="password" id="password" name="password" value="${param.password}" placeholder="Enter password here" size="30" required pattern="${requestScope.passwordPattern}" title="please enter letters or digits"/></td>
		</tr>
		<tr>
			<td><label for="password2">Repeat password</label></td>
			<td><input type="password" id="password2" name="password2" value="${param.password}" placeholder="Confirm password here" size="30" required pattern="${requestScope.passwordPattern}" title="please enter letters or digits"/></td>
		</tr>
		</table>
		<hr>
		<input type="submit" value="Register" formevent="main?action=register_new" method="post"/>
		<input type="reset" value="Clear" />
	</form>
</body>
</html>