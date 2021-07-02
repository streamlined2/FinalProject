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
		<legend><fmt:message key="new.lease.invoice.title" bundle="${localeBundle}"/></legend>
		<table>
		<tr>
			<td><label><fmt:message key="new.lease.invoice.order" bundle="${localeBundle}"/></label></td>
			<td>${orderReview.leaseOrder}</td>
		</tr>
		<tr>
			<td><label for="account"><fmt:message key="new.lease.invoice.account" bundle="${localeBundle}"/></label></td>
			<fmt:message key="new.lease.invoice.account-prompt" bundle="${localeBundle}" var="accountPrompt"/>
			<td><input type="text" id="account" name="account" value="" size="34" required pattern="${requestScope.accountPattern}" title="${accountPrompt}"/></td>
		</tr>
		<tr>
			<td><label for="invoiceSum"><fmt:message key="new.lease.invoice.sum" bundle="${localeBundle}"/></label></td>
			<fmt:message key="new.lease.invoice.sum-title" bundle="${localeBundle}" var="sumTitle"/>
			<td><input type="number" id="invoiceSum" name="invoiceSum" value="${initialInvoiceSum}" defaultValue="${initialInvoiceSum}" min="0" size="18" title="${sumTitle}" required/></td>
		</tr>
		</table>
		<hr>
		<fmt:message key="new.lease.invoice.send" bundle="${localeBundle}" var="sendButton"/>
		<input type="submit" value="${sendButton}" formaction="main?action=sendLeaseInvoice" formmethod="post"/>
		<fmt:message key="new.lease.invoice.clear" bundle="${localeBundle}" var="clearButton"/>
		<input type="reset" value="${clearButton}" />
	</fieldset>
	</form>
</body>
</html>