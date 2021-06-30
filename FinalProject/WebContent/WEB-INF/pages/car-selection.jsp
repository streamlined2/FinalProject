<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tag" uri="/WEB-INF/optionselector.tld"%>
<%@ taglib prefix="opt" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Car selection criteria form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	
	<form event="main?action=confirm_car_criteria" method="post">
		<fieldset>
		<legend><fmt:message key="car.criteria" bundle="${localeBundle}"/></legend>
			<fieldset>
			<legend><fmt:message key="car.select-by" bundle="${localeBundle}"/></legend>
			<table>
			<tr>
				<td><label for="manufacturer"><fmt:message key="car.manufacturer" bundle="${localeBundle}"/></label></td>
				<td><input type="checkbox" id="manufacturer" name="selectByManufacturer" value="manufacturer"/></td>
				<td>
					<tag:selector name="manufacturer" values="${manufacturerValues}" required="true"/>
				</td>
			</tr>
			<tr>
				<td><label for="qualityGrade"><fmt:message key="car.quality-grade" bundle="${localeBundle}"/></label></td>
				<td><input type="checkbox" id="qualityGrade" name="selectByQualityGrade" value="qualityGrade"/></td>
				<td>
					<tag:selector name="qualityGrade" values="${qualityGradeValues}" required="true"/>
				</td>
			</tr>
			<tr>
				<td><label for="color"><fmt:message key="car.color" bundle="${localeBundle}"/></label></td>
				<td><input type="checkbox" id="color" name="selectByColor" value="color"/></td>
				<td>
					<tag:selector name="color" values="${colorValues}" required="true"/>
				</td>
			</tr>
			<tr>
				<td><label for="style"><fmt:message key="car.style" bundle="${localeBundle}"/></label></td>
				<td><input type="checkbox" id="style" name="selectByStyle" value="style"/></td>
				<td>
					<tag:selector name="style" values="${styleValues}" required="true"/>
				</td>
			</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend><fmt:message key="car.order-by" bundle="${localeBundle}"/></legend>
			<table>
			<fmt:message key="car.rent-cost" bundle="${localeBundle}" var="carRentCost"/>
			<opt:orderOption id="order1" label="${carRentCost}" checkName="orderByRentCost" checkValue="cost" optionName="costSort"/>
			<fmt:message key="car.model" bundle="${localeBundle}" var="carModel"/>
			<opt:orderOption id="order2" label="${carModel}" checkName="orderByModel" checkValue="model" optionName="modelSort"/>
			<fmt:message key="car.production-date" bundle="${localeBundle}" var="carProductionDate"/>
			<opt:orderOption id="order3" label="${carProductionDate}" checkName="orderByProductionDate" checkValue="productionDate" optionName="productionDateSort"/>
			</table>
		</fieldset>
		<hr>
		<fmt:message key="car.action.select" bundle="${localeBundle}" var="carActionSelect"/>
		<input type="submit" value="${carActionSelect}" formaction="main?action=confirm_car_criteria" formmethod="post"/>
		<fmt:message key="car.action.clear" bundle="${localeBundle}" var="carActionClear"/>
		<input type="reset" value="${carActionClear}" />
	</fieldset>
	</form>
</body>
</html>