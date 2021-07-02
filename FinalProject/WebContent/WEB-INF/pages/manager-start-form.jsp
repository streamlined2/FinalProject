<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Manager start form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	<form>
	<fieldset>
		<legend><fmt:message key="manager.start.title" bundle="${localeBundle}"/></legend>
		<table>
		<tr>
		<td><label for="approvement"><fmt:message key="manager.start.approve" bundle="${localeBundle}"/></label></td>
		<td><input type="radio" id="approvement" name="managerTask" value="orderApprovement" checked/></td>
		</tr>
		<tr>
		<td><label for="checkIn"><fmt:message key="manager.start.receive" bundle="${localeBundle}"/></label></td>
		<td><input type="radio" id="checkIn" name="managerTask" value="carReception"/></td>
		</tr>
		</table>
		<hr>
		<fmt:message key="manager.start.next" bundle="${localeBundle}" var="nextButton"/>
		<input type="submit" value="${nextButton}" formaction="main?action=next" formmethod="post"/>
	</fieldset>
	</form>

</body>
</html>