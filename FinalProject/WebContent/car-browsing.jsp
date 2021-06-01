<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.practice.finalproject.model.analysis.Inspector" %>
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
			<c:forEach items="${query}" var="entity">
				<tr>
					<c:forEach items="${entity.iterator()}" var="value">
						<td>${value}</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</table>
		<hr>
		<input type="submit" value="Select" formaction="main?action=select_car" formmethod="post"/>
		<input type="reset" value="Clear"/>
		<input type="submit" value="Back" formaction="main?action=back" formmethod="post" formnovalidate/>
	</form>
</body>
</html>