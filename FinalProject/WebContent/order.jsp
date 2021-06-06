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
		<legend>Order</legend>
		<table>
		<tr>
			<td><label for="selected_car">Selected car</label></td>
			<td>${sessionScope.selected_car.model}</td>
		</tr>
		<tr>
			<td><label for="passport">Passport</label></td>
			<td><input type="text" id="passport" name="passport" value="" placeholder="Enter passport number here" size="50" pattern="${requestScope.passportPattern}" title="Please enter letters or digits" required/></td>
		</tr>
		<tr>
			<td><label for="driver">Driver</label></td>
			<td><input type="checkbox" id="driver" name="driver" value="driver" checked/></td>
		</tr>
		<tr>
			<td><label for="startTime">Lease start time</label></td>
			<td><input type="datetime-local" id="startTime" name="startTime" value="" placeholder="Enter start time of leasing" required/></td>
		</tr>
		<tr>
			<td><label for="returnTime">Due time to return</label></td>
			<td><input type="datetime-local" id="returnTime" name="returnTime" value=""  placeholder="Enter due time to return" required/></td>
		</tr>
		</table>
		<hr>
		<input type="submit" value="Order" formaction="main?action=orderCar" formmethod="post"/>
		<input type="reset" value="Clear" />
		<input type="submit" value="Back" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>

</body>
</html>