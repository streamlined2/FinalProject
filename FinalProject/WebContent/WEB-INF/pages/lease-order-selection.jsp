<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tv" uri="/WEB-INF/table-view.tld" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Order browsing form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	
	<form>
		<tv:table-view buttons="${buttonsMap}" queryHeader="${queryHeader}" queryData="${queryData}" order="orderNumber"/>
		<hr>
		<fmt:message key="lease.order.selection.next" bundle="${localeBundle}" var="nextButton"/>
		<input type="submit" value="${nextButton}" formaction="main?action=nextPage" formmethod="post"/>
		<fmt:message key="lease.order.selection.previous" bundle="${localeBundle}" var="previousButton"/>
		<input type="submit" value="${previousButton}" formaction="main?action=previousPage" formmethod="post"/>
		<fmt:message key="lease.order.selection.first" bundle="${localeBundle}" var="firstButton"/>
		<input type="submit" value="${firstButton}" formaction="main?action=firstPage" formmethod="post"/>
		<fmt:message key="lease.order.selection.last" bundle="${localeBundle}" var="lastButton"/>
		<input type="submit" value="${lastButton}" formaction="main?action=lastPage" formmethod="post"/>
		<fmt:message key="lease.order.selection.back" bundle="${localeBundle}" var="backButton"/>
		<input type="submit" value="${backButton}" formaction="main?action=back" formmethod="post" formnovalidate/>
	</form>
</body>
</html>