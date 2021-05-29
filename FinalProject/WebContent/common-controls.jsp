<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<body>
	<table><th>
		<td><c:if test="${!empty sessionScope.user}">
		<form><button type="submit" width="24" height="24" formaction="main?action=logout" formmethod="post">
			<img src="logoutbutton.png">
		</button></form>
		</c:if></td>
		<td><form action="main?action=locale" method="post">
 			<select name="locale" onchange="this.form.submit()">
				<c:forEach items="${applicationScope.availableLocales}" var="loc">
					<c:choose>
					<c:when test="${loc eq sessionScope.locale}">
						<option selected value="${loc.language}">${loc.displayName}</option></c:when>
					<c:otherwise><option value="${loc.language}">${loc.displayName}</option>	</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
		</form></td>
		<td><div class="message">${sessionScope.message}</div></td>
		<td><div class="error">${sessionScope.error}</div></td>
	</th></table>
</body>
</html>