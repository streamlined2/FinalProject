<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<!DOCTYPE html>
<html>
<body>
	<fmt:setLocale value="${sessionScope.locale}"/>
	<fmt:setBundle basename="resources.messages" var="localeBundle"/>
	<table><tr>
		<td style="width:5%"><tags:selectLocale/></td>
		<td><div class="message">${sessionScope.message}</div></td>
		<td><div class="error">${sessionScope.error}</div></td>
		<td style="font-family: verdana; width:10%"><tags:currentUser/></td>
		<td style="width:5%"><tags:logoutButton action="logout" image="logoutbutton.png"/></td>
	</tr></table>
</body>
</html>