<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Lease invoice form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	<form>
	<fieldset>
		<legend><fmt:message key="lease.invoice.demo.form" bundle="${localeBundle}"/></legend>
		<table>
		<tr>
			<td><fmt:message key="lease.invoice.demo.order" bundle="${localeBundle}"/>${leaseInvoice.leaseOrder}</td>
		</tr>
		<tr>
			<td><fmt:message key="lease.invoice.demo.signed-on" bundle="${localeBundle}"/>${leaseInvoice.signTime}<fmt:message key="lease.invoice.demo.by" bundle="${localeBundle}"/>${leaseInvoice.manager}</td>
		</tr>
		<tr>
			<td><fmt:message key="lease.invoice.demo.bank-account" bundle="${localeBundle}"/>${leaseInvoice.account}</td>
		</tr>
		<tr>
			<td><fmt:message key="lease.invoice.demo.due-sum" bundle="${localeBundle}"/>${leaseInvoice.sum}</td>
		</tr>
		</table>
		<hr>
		<fmt:message key="lease.invoice.demo.done" bundle="${localeBundle}" var="leaseInvoiceDone"/>
		<input type="submit" value="${leaseInvoiceDone}" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>
</body>
</html>