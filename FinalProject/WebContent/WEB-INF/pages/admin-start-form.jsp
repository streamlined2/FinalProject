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
		<legend><fmt:message key="admin.start.form.title" bundle="${localeBundle}"/></legend>
		<table>
		<tr>
		<td><label for="carManagement"><fmt:message key="admin.start.form.car-management" bundle="${localeBundle}"/></label></td>
		<td><input type="radio" id="carManagement" name="adminTask" value="carManagement" checked/></td>
		</tr>
		<tr>
		<td><label for="userBlocking"><fmt:message key="admin.start.form.user-blocking" bundle="${localeBundle}"/></label></td>
		<td><input type="radio" id="userBlocking" name="adminTask" value="userBlocking"/></td>
		</tr>
		<tr>
		<td><label for="managerRegistration"><fmt:message key="admin.start.form.manager-registration" bundle="${localeBundle}"/></label></td>
		<td><input type="radio" id="managerRegistration" name="adminTask" value="managerRegistration"/></td>
		</tr>
		</table>
		<hr>
		<fmt:message key="admin.start.form.nextButton" bundle="${localeBundle}" var="nextButton"/>
		<input type="submit" value="${nextButton}" formaction="main?action=next" formmethod="post"/>
	</fieldset>
	</form>
</body>
</html>