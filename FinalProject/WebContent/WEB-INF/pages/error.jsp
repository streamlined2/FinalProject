<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" href="styles.css">
	<meta charset="UTF-8">
	<title>Error page</title>
</head>
<body>
	<p><%=LocalDateTime.now() %></p>
	<h1 class="error">Error occured which program can't handle!</h1>
	<c:if test="${!empty pageContext.exception}">
		<p>
		<tags:upperCase>
			${pageContext.exception.message}
		</tags:upperCase>
	</c:if>
	<p><a href="main">Click here to start anew</a></p>
</body>
</html>