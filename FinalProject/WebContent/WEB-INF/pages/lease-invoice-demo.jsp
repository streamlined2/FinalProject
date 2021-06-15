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
		<legend>Lease invoice</legend>
		<table>
		<tr>
			<td>Order ${leaseInvoice.leaseOrder}</td>
		</tr>
		<tr>
			<td>Signed on ${leaseInvoice.signTime} by ${leaseInvoice.manager}</td>
		</tr>
		<tr>
			<td>Bank account ${leaseInvoice.account}</td>
		</tr>
		<tr>
			<td>Due sum ${leaseInvoice.sum}</td>
		</tr>
		</table>
		<hr>
		<input type="submit" value="Done" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>
</body>
</html>