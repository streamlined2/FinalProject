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
		<h2>Car was successfully reserved!</h2>
		<hr>
		<input type="submit" value="Repeat" formaction="main?action=select_car_criteria" formmethod="post"/>
	</form>

</body>
</html>