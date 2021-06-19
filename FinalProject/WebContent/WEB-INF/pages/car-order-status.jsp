<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Order status form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>

	<form>
	<fieldset>
		<legend>Car reservation status</legend>
		<table>
		<tr>
			<td><h2>Car ${selectedOrder.car} has been successfully reserved!</h2></td>
		</tr>
		<tr>
			<td>
				Your reservation starts from ${selectedOrder.startTime} and valid till ${selectedOrder.endTime}<br>
				<c:if test="${selectedOrder.driverPresent}">with driver assigned</c:if>
			</td>
		</tr>
		</table>
		<hr>
		<input type="submit" value="Repeat" formaction="main?action=select_car_criteria" formmethod="post"/>
	</fieldset>
	</form>

</body>
</html>