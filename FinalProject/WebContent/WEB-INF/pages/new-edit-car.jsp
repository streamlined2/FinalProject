<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib prefix="tag" uri="/WEB-INF/optionselector.tld"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>New/modification car form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	
	<form>
	<fieldset>
		<c:choose>
		<c:when test="${createCar}"><legend><fmt:message key="new.edit.car.new-car" bundle="${localeBundle}"/></legend></c:when>
		<c:otherwise><legend><fmt:message key="new.edit.car.modify-car" bundle="${localeBundle}"/></legend></c:otherwise>
		</c:choose>
		<table>
		<tr>
			<td><label for="model"><fmt:message key="new.edit.car.model" bundle="${localeBundle}"/></label></td>
			<fmt:message key="new.edit.car.model-prompt" bundle="${localeBundle}" var="modelPrompt"/>
			<fmt:message key="new.edit.car.model-title" bundle="${localeBundle}" var="modelTitle"/>
			<td><input type="text" id="model" name="model" value="${selectedCar.model}" placeholder="${modelPrompt}" size="30" pattern="${requestScope.carModelPattern}" title="modelTitle" required/></td>
		</tr>
		<tr>
			<td><label for="manufacturer"><fmt:message key="new.edit.car.manufacturer" bundle="${localeBundle}"/></label></td>
			<td>
				<tag:selector name="manufacturer" values="${manufacturerValues}" selectedValue="${selectedCar.manufacturer.label}" required="true"/>
			</td>
		</tr>
		<tr>
			<td><label for="qualityGrade"><fmt:message key="new.edit.car.qualityGrade" bundle="${localeBundle}"/></label></td>
			<td>
				<tag:selector name="qualityGrade" values="${qualityGradeValues}" selectedValue="${selectedCar.qualityGrade.label}" required="true"/>
			</td>
		</tr>
		<tr>
			<td><label for="cost"><fmt:message key="new.edit.car.rentCost" bundle="${localeBundle}"/></label></td>
			<fmt:message key="new.edit.car.rentCost-prompt" bundle="${localeBundle}" var="rentCostPrompt"/>
			<fmt:message key="new.edit.car.rentCost-title" bundle="${localeBundle}" var="rentCostTitle"/>
			<td><input type="number" id="cost" name="cost" value="${selectedCar.cost}" min="0" size="18" title="${rentCostPrompt}" title="${rentCostTitle}" required/></td>
		</tr>
		<tr>
			<td><label for="productionDate"><fmt:message key="new.edit.car.produced" bundle="${localeBundle}"/></label></td>
			<fmt:message key="new.edit.car.produced-prompt" bundle="${localeBundle}" var="producedPrompt"/>
			<td><input type="date" id="productionDate" name="productionDate" value="${selectedCar.productionDate}" placeholder="${producedPrompt}" required/></td>
		</tr>
		<tr>
			<td><label for="color"><fmt:message key="new.edit.car.color" bundle="${localeBundle}"/></label></td>
			<td>
				<tag:selector name="color" values="${colorValues}" selectedValue="${selectedCar.color.label}" required="true"/>
			</td>
		</tr>
		<tr>
			<td><label for="style"><fmt:message key="new.edit.car.style" bundle="${localeBundle}"/></label></td>
			<td>
				<tag:selector name="style" values="${styleValues}" selectedValue="${selectedCar.style.label}" required="true"/>
			</td>
		</tr>
		</table>
		<hr>
		<c:choose>
		<c:when test="${createCar}">
			<fmt:message key="new.edit.car.createButton" bundle="${localeBundle}" var="createButton"/>
			<input type="submit" value="${createButton}" formaction="main?action=createModifyCar" formmethod="post"/>
		</c:when>
		<c:otherwise>
			<fmt:message key="new.edit.car.modifyButton" bundle="${localeBundle}" var="modifyButton"/>
			<input type="submit" value="${modifyButton}" formaction="main?action=createModifyCar" formmethod="post"/>
		</c:otherwise>
		</c:choose>
		<fmt:message key="new.edit.car.clearButton" bundle="${localeBundle}" var="clearButton"/>
		<input type="reset" value="${clearButton}" />
		<fmt:message key="new.edit.car.cancelButton" bundle="${localeBundle}" var="cancelButton"/>
		<input type="submit" value="${cancelButton}" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>
</body>
</html>