<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Review form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	<form>
	<fieldset>
		<legend><fmt:message key="order.review.review-order" bundle="${localeBundle}"/></legend>
		<table>
		<tr>
			<td><label><fmt:message key="order.review.selected-order" bundle="${localeBundle}"/></label></td>
			<td>${selectedOrder}</td>
		</tr>
		<tr>
			<td><label for="reason"><fmt:message key="order.review.rejection-reason" bundle="${localeBundle}"/></label></td>
			<fmt:message key="order.review.rejection-placeholder" bundle="${localeBundle}" var="rejectionPlaceholder"/>
			<td><textarea id="reason" name="rejectionReason" rows="6" cols="60" placeholder="${rejectionPlaceholder}"  wrap="soft" value=""></textarea></td>
		</tr>
		</table>
		<hr>
		<fmt:message key="order.review.accept" bundle="${localeBundle}" var="acceptAction"/>
		<input type="submit" value="${acceptAction}" formaction="main?action=acceptOrder" formmethod="post"/>
		<fmt:message key="order.review.reject" bundle="${localeBundle}" var="rejectAction"/>
		<input type="submit" value="${rejectAction}" formaction="main?action=rejectOrder" formmethod="post"/>
		<fmt:message key="order.review.clear" bundle="${localeBundle}" var="clearAction"/>
		<input type="reset" value="${clearAction}" />
		<fmt:message key="order.review.back" bundle="${localeBundle}" var="backAction"/>
		<input type="submit" value="${backAction}" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>
</body>
</html>