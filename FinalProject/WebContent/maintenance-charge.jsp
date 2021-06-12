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
		<legend>Charge for maintenance</legend>
		<table>
		<tr>
			<td><label for="account">Account</label></td>
			<td><input type="text" id="account" name="account" value="" size="34" required pattern="${requestScope.accountPattern}" title="Please enter up to 34 letters and digits of bank account"/></td>
		</tr>
		<tr>
			<td><label for="invoiceSum">Sum</label></td>
			<td><input type="number" id="invoiceSum" name="invoiceSum" value="0" defaultValue="0" min="0" size="18" title="Please enter due sum" required/></td>
		</tr>
		<tr>
			<td><label for="repairs">List of repairs</label></td>
			<td><textarea id="repairs" name="repairs" rows="6" cols="60" placeholder="Please enter reasons for repair here"  wrap="soft" value="" required></textarea></td>
		</tr>
		</table>
		<hr>
		<input type="submit" value="Send" formaction="main?action=sendMaintenanceInvoice" formmethod="post"/>
		<input type="submit" value="Cancel" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>
</body>
</html>