<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Admin start form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	<form>
	<fieldset>
		<legend>Administrative tasks</legend>
		<table>
		<tr>
		<td><label for="carManagement">Car management</label></td>
		<td><input type="radio" id="carManagement" name="adminTask" value="carManagement" checked/></td>
		</tr>
		<tr>
		<td><label for="userBlocking">User blocking</label></td>
		<td><input type="radio" id="userBlocking" name="adminTask" value="userBlocking"/></td>
		</tr>
		<tr>
		<td><label for="managerRegistration">Manager registration</label></td>
		<td><input type="radio" id="managerRegistration" name="adminTask" value="managerRegistration"/></td>
		</tr>
		</table>
		<hr>
		<input type="submit" value="Next" formaction="main?action=next" formmethod="post"/>
	</fieldset>
	</form>
</body>
</html>