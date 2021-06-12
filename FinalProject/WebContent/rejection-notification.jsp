<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Notification of rejection</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	<form>
	<fieldset>
		<legend>Notification of rejection </legend>
		<table>
		<tr><td><h2>Order ${orderReview.leaseOrder} has been cancelled</h2></td>	</tr>
		<tr><td>by ${orderReview.manager}</td></tr>
		<tr><td>on ${orderReview.reviewTime}</td></tr>
		<tr><td>because of this given reason:</td></tr>
		<tr><td>${orderReview.reasonNote}</td></tr>
		</table>
		<hr>
		<input type="submit" value="Back" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>

</body>
</html>