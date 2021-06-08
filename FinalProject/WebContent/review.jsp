<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Review form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	<form>
	<fieldset>
		<legend>Review order</legend>
		<table>
		<tr>
			<td><label for="selected_order">Selected order</label></td>
			<td>${sessionScope.selected_order}</td>
		</tr>
		<tr>
			<td><label for="reason">Reason for rejection</label></td>
			<td><textarea id="reason" name="rejectionReason" rows="6" cols="60" placeholder="Please enter reason for rejection here"  wrap="soft" value="" required></textarea></td>
		</tr>
		</table>
		<hr>
		<input type="submit" value="Accept" formaction="main?action=acceptOrder" formmethod="post"/>
		<input type="submit" value="Reject" formaction="main?action=rejectOrder" formmethod="post"/>
		<input type="reset" value="Clear" />
		<input type="submit" value="Back" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>

</body>
</html>