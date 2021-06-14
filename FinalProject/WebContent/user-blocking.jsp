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
				<option value="client"><fmt:message key="user.client" bundle="${localeBundle}"/></option>
				<option value="manager"><fmt:message key="user.manager" bundle="${localeBundle}"/></option>
				<option value="admin"><fmt:message key="user.administrator" bundle="${localeBundle}"/></option>
			</select>
	</form>
	<p>
	<form>
		<table>
			<tbody>
				<c:set var="number" value="0"/>
				<c:forEach items="${pageItems}" var="entity">
					<tr>
						<td>
							<c:choose>
							<c:when test="${entity.blocked}">
								<input type="submit" value="Unblock" formaction="main?action=blockUser&userNumber=${number}" formmethod="post"/>
							</c:when>
							<c:otherwise>
								<input type="submit" value="Block" formaction="main?action=blockUser&userNumber=${number}" formmethod="post"/>
							</c:otherwise>
							</c:choose>
						</td>
						<td>${entity}</td>
					</tr>
					<c:set var="number" value="${number+1}"/>
				</c:forEach>
			</tbody>
		</table>
		<hr>
		<input type="submit" value="Next" formaction="main?action=nextPage" formmethod="post"/>
		<input type="submit" value="Previous" formaction="main?action=previousPage" formmethod="post"/>
		<input type="submit" value="First" formaction="main?action=firstPage" formmethod="post"/>
		<!-- <input type="submit" value="Last" formaction="main?action=lastPage" formmethod="post"/> -->
		<input type="submit" value="Back" formaction="main?action=back" formmethod="post" formnovalidate/>
	</form>
</body>
</html>