<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Order form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	<form>
	<fieldset>
		<legend><fmt:message key="order.form" bundle="${localeBundle}"/></legend>
		<table>
		<tr>
			<td><label><fmt:message key="order.selected-car" bundle="${localeBundle}"/></label></td>
			<td>${sessionScope.selectedCar.model}</td>
		</tr>
		<tr>
			<td><label for="passport"><fmt:message key="order.passport" bundle="${localeBundle}"/></label></td>
			<fmt:message key="order.passport.prompt" bundle="${localeBundle}" var="passportPrompt"/>
			<fmt:message key="user.letterdigit.prompt" bundle="${localeBundle}" var="passportEnterPrompt"/>
			<td><input type="text" id="passport" name="passport" value="" placeholder="${passportPrompt}" size="50" pattern="${requestScope.passportPattern}" title="${passportEnterPrompt}" required/></td>
		</tr>
		<tr>
			<td><label for="driver"><fmt:message key="order.driver" bundle="${localeBundle}"/></label></td>
			<td><input type="checkbox" id="driver" name="driver" value="driver" checked/></td>
		</tr>
		<tr>
			<td><label for="startTime"><fmt:message key="order.lease-start-time" bundle="${localeBundle}"/></label></td>
			<fmt:message key="order.lease-start-time-prompt" bundle="${localeBundle}" var="leaseStartTimePrompt"/>
			<td><input type="datetime-local" id="startTime" name="startTime" value="" placeholder="${leaseStartTimePrompt}" required/></td>
		</tr>
		<tr>
			<td><label for="returnTime"><fmt:message key="order.lease-due-time" bundle="${localeBundle}"/></label></td>
			<fmt:message key="order.lease-due-time-prompt" bundle="${localeBundle}" var="leaseDueTimePrompt"/>
			<td><input type="datetime-local" id="returnTime" name="returnTime" value=""  placeholder="${leaseDueTimePrompt}" required/></td>
		</tr>
		</table>
		<hr>
		<fmt:message key="order.action.order" bundle="${localeBundle}" var="orderAction"/>
		<input type="submit" value="${orderAction}" formaction="main?action=orderCar" formmethod="post"/>
		<fmt:message key="order.action.clear" bundle="${localeBundle}" var="clearAction"/>
		<input type="reset" value="${clearAction}" />
		<fmt:message key="order.action.back" bundle="${localeBundle}" var="backAction"/>
		<input type="submit" value="${backAction}" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>

</body>
</html>