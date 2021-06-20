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
		<c:when test="${createCar}"><legend>New car</legend></c:when>
		<c:otherwise><legend>Modify car</legend></c:otherwise>
		</c:choose>
		<table>
		<tr>
			<td><label for="model">Model</label></td>
			<td><input type="text" id="model" name="model" value="${selectedCar.model}" placeholder="Enter car model" size="30" pattern="${requestScope.carModelPattern}" title="Please enter letters or digits" required/></td>
		</tr>
		<tr>
			<td><label for="manufacturer">Manufacturer</label></td>
			<td>
				<tag:selector name="manufacturer" values="${manufacturerValues}" selectedValue="${selectedCar.manufacturer.label}" required="true"/>
			</td>
		</tr>
		<tr>
			<td><label for="qualityGrade">Quality grade</label></td>
			<td>
				<tag:selector name="qualityGrade" values="${qualityGradeValues}" selectedValue="${selectedCar.qualityGrade.label}" required="true"/>
			</td>
		</tr>
		<tr>
			<td><label for="cost">Rent cost</label></td>
			<td><input type="number" id="cost" name="cost" value="${selectedCar.cost}" min="0" size="18" title="Please enter rent cost" title="Please enter number" required/></td>
		</tr>
		<tr>
			<td><label for="productionDate">Produced</label></td>
			<td><input type="date" id="productionDate" name="productionDate" value="${selectedCar.productionDate}" placeholder="Enter production date" required/></td>
		</tr>
		<tr>
			<td><label for="color">Color</label></td>
			<td>
				<tag:selector name="color" values="${colorValues}" selectedValue="${selectedCar.color.label}" required="true"/>
			</td>
		</tr>
		<tr>
			<td><label for="style">Style</label></td>
			<td>
				<tag:selector name="style" values="${styleValues}" selectedValue="${selectedCar.style.label}" required="true"/>
			</td>
		</tr>
		</table>
		<hr>
		<c:choose>
		<c:when test="${createCar}">
			<input type="submit" value="Create" formaction="main?action=createModifyCar" formmethod="post"/>
		</c:when>
		<c:otherwise>
			<input type="submit" value="Modify" formaction="main?action=createModifyCar" formmethod="post"/>
		</c:otherwise>
		</c:choose>
		<input type="reset" value="Clear" />
		<input type="submit" value="Cancel" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>
</body>
</html>