<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tv" uri="/WEB-INF/table-view.tld" %>
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
		<tv:table-view buttons="${buttonsMap}" queryHeader="${queryHeader}" queryData="${queryData}" order="carNumber"/>
<!-- 	<table>
			<thead><tr>
				<th>&nbsp;</th>
				<c:forEach items="${queryHeader}" var="column">
					<th>${column}</th>
				</c:forEach>
			</tr></thead>
			<tbody>
				<c:set var="number" value="0"/>
				<c:forEach items="${queryData}" var="entity">
					<tr>
						<td>
							<input type="submit" value="Get" formaction="main?action=select_car&carNumber=${number}" formmethod="post"/>
						</td>
						<c:forEach items="${entity}" var="value">
							<td>${value}</td>
						</c:forEach>
					</tr>
					<c:set var="number" value="${number+1}"/>
				</c:forEach>
			</tbody>
		</table> -->
		<hr>
		<fmt:message key="form.dispatch.next" bundle="${localeBundle}" var="formDispatchNext"/>
		<input type="submit" value="${formDispatchNext}" formaction="main?action=nextPage" formmethod="post"/>
		<fmt:message key="form.dispatch.previous" bundle="${localeBundle}" var="formDispatchPrevious"/>
		<input type="submit" value="${formDispatchPrevious}" formaction="main?action=previousPage" formmethod="post"/>
		<fmt:message key="form.dispatch.first" bundle="${localeBundle}" var="formDispatchFirst"/>
		<input type="submit" value="${formDispatchFirst}" formaction="main?action=firstPage" formmethod="post"/>
		<fmt:message key="form.dispatch.last" bundle="${localeBundle}" var="formDispatchLast"/>
		<input type="submit" value="${formDispatchLast}" formaction="main?action=lastPage" formmethod="post"/>
		<fmt:message key="user.back.button" bundle="${localeBundle}" var="formDispatchBack"/>
		<input type="submit" value="${formDispatchBack}" formaction="main?action=back" formmethod="post" formnovalidate/>
	</form>
</body>
</html>