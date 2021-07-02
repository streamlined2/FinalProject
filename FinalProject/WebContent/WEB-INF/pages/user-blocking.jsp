<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>User blocking form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	<p>
	<form action="main?action=selectUser" method="post">
		<select name="role" id="role" onchange="this.form.submit()">
		    <c:forEach items="${roleValues}" var="item">
		    <c:choose>
		        <c:when test="${selectedRole eq item}">
		            <option value="${item}" selected>${item}</option>
		        </c:when>
		        <c:otherwise>
		            <option value="${item}">${item}</option>
		        </c:otherwise>
		    </c:choose>
		    </c:forEach>
		</select>
	</form>
	<p>
	<form>
		<table>
			<thead>
			<tr>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th><fmt:message key="user.blocking.form.login" bundle="${localeBundle}"/></th>
				<th><fmt:message key="user.blocking.form.first-name" bundle="${localeBundle}"/></th>
				<th><fmt:message key="user.blocking.form.last-name" bundle="${localeBundle}"/></th>
			</tr>
			</thead>
			<tbody>
				<fmt:message key="user.blocking.form.unblockButton" bundle="${localeBundle}" var="unblockButton"/>
				<fmt:message key="user.blocking.form.blockButton" bundle="${localeBundle}" var="blockButton"/>
				<c:set var="number" value="0"/>
				<c:forEach items="${pageItems}" var="entity">
					<tr>
						<td>
							<c:choose>
							<c:when test="${entity.blocked}">
								<input type="submit" value="${unblockButton}" formaction="main?action=blockUser&userNumber=${number}" formmethod="post"/>
							</c:when>
							<c:otherwise>
								<input type="submit" value="${blockButton}" formaction="main?action=blockUser&userNumber=${number}" formmethod="post"/>
							</c:otherwise>
							</c:choose>
						</td>
						<td>&nbsp;</td>
						<td>${entity.login}</td>
						<td>${entity.firstName}</td>
						<td>${entity.lastName}</td>
					</tr>
					<c:set var="number" value="${number+1}"/>
				</c:forEach>
			</tbody>
		</table>
		<hr>
		<fmt:message key="user.blocking.form.nextButton" bundle="${localeBundle}" var="nextButton"/>
		<input type="submit" value="${nextButton}" formaction="main?action=nextPage" formmethod="post"/>
		<fmt:message key="user.blocking.form.previousButton" bundle="${localeBundle}" var="previousButton"/>
		<input type="submit" value="${previousButton}" formaction="main?action=previousPage" formmethod="post"/>
		<fmt:message key="user.blocking.form.firstButton" bundle="${localeBundle}" var="firstButton"/>
		<input type="submit" value="${firstButton}" formaction="main?action=firstPage" formmethod="post"/>
		<fmt:message key="user.blocking.form.lastButton" bundle="${localeBundle}" var="lastButton"/>
		<input type="submit" value="${lastButton}" formaction="main?action=lastPage" formmethod="post"/>
		<fmt:message key="user.blocking.form.backButton" bundle="${localeBundle}" var="backButton"/>
		<input type="submit" value="${backButton}" formaction="main?action=back" formmethod="post" formnovalidate/>
	</form>
</body>
</html>