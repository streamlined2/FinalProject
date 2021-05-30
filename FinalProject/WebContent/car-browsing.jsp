<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Car browsing form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	
	<form>
		<table>
		<c:forEach items="${sessionScope.query}" var="entity">
			<tr>
				<td>${entity}</td>
			</tr>
		</c:forEach>
		</table>
		<hr>
		<table><tr>
		<td><input type="submit" value="Select" formevent="main?action=select_car" formmethod="post"/></td>
		<td><input type="reset" value="Clear"/></td>
		<td><input type="submit" value="Back" formevent="main?action=back" formmethod="post" formnovalidate/></td>
		</tr></table>
	</form>
</body>
</html>