<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Notification of rejection</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	<form>
	<fieldset>
		<legend><fmt:message key="rejection.notification.title" bundle="${localeBundle}"/></legend>
		<table>
		<tr><td><h2><fmt:message key="rejection.notification.order" bundle="${localeBundle}"/>${orderReview.leaseOrder}<fmt:message key="rejection.notification.cancelled" bundle="${localeBundle}"/></h2></td></tr>
		<tr><td><fmt:message key="rejection.notification.by" bundle="${localeBundle}"/>${orderReview.manager}</td></tr>
		<tr><td><fmt:message key="rejection.notification.on" bundle="${localeBundle}"/>${orderReview.reviewTime}</td></tr>
		<tr><td><fmt:message key="rejection.notification.because-reason" bundle="${localeBundle}"/>${orderReview.reasonNote}</td></tr>
		</table>
		<hr>
		<fmt:message key="rejection.notification.back" bundle="${localeBundle}" var="backButton"/>
		<input type="submit" value="${backButton}" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>
</body>
</html>