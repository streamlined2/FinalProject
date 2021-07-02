<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Car maintenance form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	<form>
	<fieldset>
		<legend><fmt:message key="maintenance.charge.title" bundle="${localeBundle}"/></legend>
		<table>
		<tr>
			<td><label for="account"><fmt:message key="maintenance.charge.account" bundle="${localeBundle}"/></label></td>
			<fmt:message key="maintenance.charge.account-prompt" bundle="${localeBundle}" var="accountPrompt"/>
			<td><input type="text" id="account" name="account" value="" size="34" required pattern="${requestScope.accountPattern}" title="${accountPrompt}"/></td>
		</tr>
		<tr>
			<td><label for="invoiceSum"><fmt:message key="maintenance.charge.invoiceSum" bundle="${localeBundle}"/></label></td>
			<fmt:message key="maintenance.charge.sum-prompt" bundle="${localeBundle}" var="sumPrompt"/>
			<td><input type="number" id="invoiceSum" name="invoiceSum" value="0" defaultValue="0" min="0" size="18" title="${sumPrompt}" required/></td>
		</tr>
		<tr>
			<td><label for="repairs"><fmt:message key="maintenance.charge.repairs-list" bundle="${localeBundle}"/></label></td>
			<fmt:message key="maintenance.charge.repairs-list.prompt" bundle="${localeBundle}" var="repairsPrompt"/>
			<td><textarea id="repairs" name="repairs" rows="6" cols="60" placeholder="${repairsPrompt}"  wrap="soft" value="" required></textarea></td>
		</tr>
		</table>
		<hr>
		<fmt:message key="maintenance.charge.send" bundle="${localeBundle}" var="buttonSend"/>
		<input type="submit" value="${buttonSend}" formaction="main?action=sendMaintenanceInvoice" formmethod="post"/>
	</fieldset>
	</form>
</body>
</html>