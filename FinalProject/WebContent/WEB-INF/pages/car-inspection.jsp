<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Car inspection form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	<form>
	<fieldset>
		<legend><fmt:message key="car.inspection.title" bundle="${localeBundle}"/></legend>
		<table>
		<tr>
		<td><label for="perfect"><fmt:message key="car.inspection.perfect" bundle="${localeBundle}"/></label></td>
		<td><input type="radio" id="perfect" name="carEvaluationResult" value="perfectCondition" checked/></td>
		</tr>
		<tr>
		<td><label for="needsMaintenance"><fmt:message key="car.inspection.needs-maintenance" bundle="${localeBundle}"/></label></td>
		<td><input type="radio" id="needsMaintenance" name="carEvaluationResult" value="needsMaintenance"/></td>
		</tr>
		</table>
		<hr>
		<fmt:message key="car.inspection.proceed" bundle="${localeBundle}" var="proceedButton"/>
		<input type="submit" value="${proceedButton}" formaction="main?action=next" formmethod="post"/>
		<fmt:message key="car.inspection.back" bundle="${localeBundle}" var="backButton"/>
		<input type="submit" value="${backButton}" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>
</body>
</html>