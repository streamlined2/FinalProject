<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form action="main?action=locale" method="post">
	<select name="locale" onchange="this.form.submit()">
		<c:forEach items="${applicationScope.availableLocales}" var="loc">
			<c:choose>
			<c:when test="${loc eq sessionScope.locale}">
			<option selected value="${loc.language}">${loc.displayName}</option></c:when>
			<c:otherwise><option value="${loc.language}">${loc.displayName}</option>	</c:otherwise>
			</c:choose>
		</c:forEach>
	</select>
</form>