<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tv" uri="/WEB-INF/table-view.tld" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Order browsing form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	
	<form>
		<tv:table-view buttons="${buttonsMap}" queryHeader="${queryHeader}" queryData="${queryData}" order="orderNumber"/>
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
							<input type="submit" value="Select" formaction="main?action=reviewOrder&orderNumber=${number}" formmethod="post"/>
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
		<input type="submit" value="Next" formaction="main?action=nextPage" formmethod="post"/>
		<input type="submit" value="Previous" formaction="main?action=previousPage" formmethod="post"/>
		<input type="submit" value="First" formaction="main?action=firstPage" formmethod="post"/>
		<!-- <input type="submit" value="Last" formaction="main?action=lastPage" formmethod="post"/> -->
		<input type="submit" value="Back" formaction="main?action=back" formmethod="post" formnovalidate/>
	</form>
</body>
</html>