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
		<legend>Maintenance invoice</legend>
		<table>
		<tr>
			<td>Order ${maintenanceInvoice.leaseOrder}</td>
		</tr>
		<tr>
			<td>Signed on ${maintenanceInvoice.signTime} by ${maintenanceInvoice.manager}</td>
		</tr>
		<tr>
			<td>Bank account ${maintenanceInvoice.account}</td>
		</tr>
		<tr>
			<td>Due sum ${maintenanceInvoice.sum}</td>
		</tr>
		<tr><td>Maintenance to be done</td></tr>
		<tr><td>${maintenanceInvoice.repairs}</td></tr>
		</table>
		<hr>
		<input type="submit" value="Done" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>
</body>
</html>