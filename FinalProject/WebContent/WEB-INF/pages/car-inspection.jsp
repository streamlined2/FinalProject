<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Car inspection form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	<form>
	<fieldset>
		<legend>Please check car condition</legend>
		<table>
		<tr>
		<td><label for="perfect">Perfect</label></td>
		<td><input type="radio" id="perfect" name="carEvaluationResult" value="perfectCondition" checked/></td>
		</tr>
		<tr>
		<td><label for="needsMaintenance">Needs maintenance</label></td>
		<td><input type="radio" id="needsMaintenance" name="carEvaluationResult" value="needsMaintenance"/></td>
		</tr>
		</table>
		<hr>
		<input type="submit" value="Proceed" formaction="main?action=next" formmethod="post"/>
		<input type="submit" value="Back" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>

</body>
</html>