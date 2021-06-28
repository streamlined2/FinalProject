<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Order status form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>

	<form>
	<fieldset>
		<legend><fmt:message key="car.order.form" bundle="${localeBundle}"/></legend>
		<table>
		<tr>
			<td><h2>
				<fmt:message key="car.order.message1" bundle="${localeBundle}"/>
				${selectedOrder.car}
				<fmt:message key="car.order.message2" bundle="${localeBundle}"/>
			</h2></td>
		</tr>
		<tr>
			<td>
				 <fmt:message key="car.order.message3" bundle="${localeBundle}"/>
				 <tags:formatLocalDateTime value="${selectedOrder.startTime}"/>
				 <fmt:message key="car.order.message4" bundle="${localeBundle}"/>
				 <tags:formatLocalDateTime value="${selectedOrder.endTime}"/>
				 <br>
				<c:if test="${selectedOrder.driverPresent}"><fmt:message key="car.order.message5" bundle="${localeBundle}"/></c:if>
			</td>
		</tr>
		</table>
		<hr>
		<fmt:message key="car.order.repeat" bundle="${localeBundle}" var="repeatOrder"/>
		<input type="submit" value="${repeatOrder}" formaction="main?action=select_car_criteria" formmethod="post"/>
	</fieldset>
	</form>

</body>
</html>