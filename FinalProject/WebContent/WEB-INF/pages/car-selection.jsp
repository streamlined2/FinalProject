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
		<legend>Car criteria</legend>
			<fieldset>
			<legend>Select by</legend>
			<table>
			<tr>
				<td><label for="manufacturer">Manufacturer</label></td>
				<td><input type="checkbox" id="manufacturer" name="selectByManufacturer" value="manufacturer"/></td>
				<td>
					<tag:selector name="manufacturer" values="${manufacturerValues}" required="true"/>
				</td>
			</tr>
			<tr>
				<td><label for="qualityGrade">Quality grade</label></td>
				<td><input type="checkbox" id="qualityGrade" name="selectByQualityGrade" value="qualityGrade"/></td>
				<td>
					<tag:selector name="qualityGrade" values="${qualityGradeValues}" required="true"/>
				</td>
			</tr>
			<tr>
				<td><label for="color">Color</label></td>
				<td><input type="checkbox" id="color" name="selectByColor" value="color"/></td>
				<td>
					<tag:selector name="color" values="${colorValues}" required="true"/>
				</td>
			</tr>
			<tr>
				<td><label for="style">Style</label></td>
				<td><input type="checkbox" id="style" name="selectByStyle" value="style"/></td>
				<td>
					<tag:selector name="style" values="${styleValues}" required="true"/>
				</td>
			</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend>Order by</legend>
			<table>
			<opt:orderOption id="order1" label="Rent cost" checkName="orderByRentCost" checkValue="cost" optionName="costSort"/>
			<opt:orderOption id="order2" label="Model" checkName="orderByModel" checkValue="model" optionName="modelSort"/>
			<opt:orderOption id="order3" label="Production date" checkName="orderByProductionDate" checkValue="productionDate" optionName="productionDateSort"/>
			</table>
		</fieldset>
		<hr>
		<input type="submit" value="Select" formaction="main?action=confirm_car_criteria" formmethod="post"/>
		<input type="reset" value="Clear" />
	</fieldset>
	</form>
</body>
</html>