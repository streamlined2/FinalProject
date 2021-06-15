<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Manager start form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	<form>
	<fieldset>
		<legend>Managerial tasks</legend>
		<table>
		<tr>
		<td><label for="approvement">Approve order</label></td>
		<td><input type="radio" id="approvement" name="managerTask" value="orderApprovement" checked/></td>
		</tr>
		<tr>
		<td><label for="checkIn">Receive and inspect car</label></td>
		<td><input type="radio" id="checkIn" name="managerTask" value="carReception"/></td>
		</tr>
		</table>
		<hr>
		<input type="submit" value="Next" formaction="main?action=next" formmethod="post"/>
	</fieldset>
	</form>

</body>
</html>