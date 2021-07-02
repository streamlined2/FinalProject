<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Maintenance invoice form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	<form>
	<fieldset>
		<legend><fmt:message key="maintenance.invoice.demo.title" bundle="${localeBundle}"/></legend>
		<table>
		<tr>
			<td><fmt:message key="maintenance.invoice.demo.order" bundle="${localeBundle}"/>${maintenanceInvoice.leaseOrder}</td>
		</tr>
		<tr>
			<td><fmt:message key="maintenance.invoice.demo.signedon" bundle="${localeBundle}"/>${maintenanceInvoice.signTime}<fmt:message key="maintenance.invoice.demo.by" bundle="${localeBundle}"/>${maintenanceInvoice.manager}</td>
		</tr>
		<tr>
			<td><fmt:message key="maintenance.invoice.demo.bank-account" bundle="${localeBundle}"/>${maintenanceInvoice.account}</td>
		</tr>
		<tr>
			<td><fmt:message key="maintenance.invoice.demo.due-sum" bundle="${localeBundle}"/>${maintenanceInvoice.sum}</td>
		</tr>
		<tr><td><fmt:message key="maintenance.invoice.demo.to-be-done" bundle="${localeBundle}"/></td></tr>
		<tr><td>${maintenanceInvoice.repairs}</td></tr>
		</table>
		<hr>
		<fmt:message key="maintenance.invoice.demo.done" bundle="${localeBundle}" var="doneButton"/>
		<input type="submit" value="${doneButton}" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>
</body>
</html>