<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tv" uri="/WEB-INF/table-view.tld" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Car management form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	
	<form>
		<tv:table-view buttons="${buttonsMap}" queryHeader="${queryHeader}" queryData="${queryData}" order="carNumber"/>
		<hr>
		<fmt:message key="car.management.newButton" bundle="${localeBundle}" var="newButton"/>
		<input type="submit" value="${newButton}" formaction="main?action=newCar" formmethod="post"/>
		<fmt:message key="car.management.nextButton" bundle="${localeBundle}" var="nextButton"/>
		<input type="submit" value="${nextButton}" formaction="main?action=nextPage" formmethod="post"/>
		<fmt:message key="car.management.previousButton" bundle="${localeBundle}" var="previousButton"/>
		<input type="submit" value="${previousButton}" formaction="main?action=previousPage" formmethod="post"/>
		<fmt:message key="car.management.firstButton" bundle="${localeBundle}" var="firstButton"/>
		<input type="submit" value="${firstButton}" formaction="main?action=firstPage" formmethod="post"/>
		<fmt:message key="car.management.lastButton" bundle="${localeBundle}" var="lastButton"/>
		<input type="submit" value="${lastButton}" formaction="main?action=lastPage" formmethod="post"/>
		<fmt:message key="car.management.backButton" bundle="${localeBundle}" var="backButton"/>
		<input type="submit" value="${backButton}" formaction="main?action=back" formmethod="post" formnovalidate/>
	</form>
</body>
</html>