<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tag" uri="/WEB-INF/optionselector.tld"%>
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
			<tr>
				<td><label for="order1">Rent cost</label></td>
				<td><input type="checkbox" id="order1" name="orderByRentCost" value="cost"/></td>
				<td>
					<select name="costSort" id="order1">
						<option value="asc" >Ascending</option>
						<option value="desc">Descending</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><label for="order2">Model</label></td>
				<td><input type="checkbox" id="order2" name="orderByModel" value="model"/></td>
				<td>
					<select name="modelSort" id="order2">
						<option value="asc" >Ascending</option>
						<option value="desc">Descending</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><label for="order3">Production date</label></td>
				<td><input type="checkbox" id="order3" name="orderByProductionDate" value="productionDate"/></td>
				<td>
					<select name="productionDateSort" id="order3">
						<option value="asc" >Ascending</option>
						<option value="desc">Descending</option>
					</select>
				</td>
			</tr>
			</table>
		</fieldset>
		<hr>
		<input type="submit" value="Select" formaction="main?action=confirm_car_criteria" formmethod="post"/>
		<input type="reset" value="Clear" />
	</fieldset>
	</form>
</body>
</html>