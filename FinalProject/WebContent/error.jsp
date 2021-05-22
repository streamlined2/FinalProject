<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true" %>
<%@ page import="java.time.LocalDateTime" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error page</title>
	<style>
		.error { color:red }
	</style>
</head>
<body>
	<p><%=LocalDateTime.now() %></p>
	<h1  class="error">Error occured which program can't handle!</h1>
	<p><%=exception %></p>
	<p><%=pageContext.getException().getMessage() %></p>
	<p><a href="main">Click here to start anew</a></p>
</body>
</html>