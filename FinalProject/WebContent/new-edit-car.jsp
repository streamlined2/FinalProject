<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		<legend>${createCar?'New car':'Modify car'}</legend>
		<table>
		<tr>
			<td><label for="model">Model</label></td>
			<td><input type="text" id="model" name="model" value="${selectedCar.model}" placeholder="Enter car model" size="30" pattern="${requestScope.carModelPattern}" title="Please enter letters or digits" required/></td>
		</tr>
		<tr>
			<td><label for="manufacturer">Manufacturer</label></td>
			<td>
					<select name="manufacturer" id="manufacturer" required>
					<c:forEach items="${requestScope.manufacturerValues}" var="item">
					<c:choose>
						<c:when test="${selectedCar.manufacturer eq item}"></c:when>
							<option value="${item}" selected>${item}</option>
						<c:otherwise>
							<option value="${item}">${item}</option>
						</c:otherwise>
					</c:choose>
					</c:forEach>
					</select>
			</td>
		</tr>
		<tr>
			<td><label for="qualityGrade">Quality grade</label></td>
			<td>
				<select name="qualityGrade" id="qualityGrade" required>
				<c:forEach items="${requestScope.qualityGradeValues}" var="item">
				<c:choose>
					<c:when test="${selectedCar.qualityGrade eq item}"></c:when>
						<option value="${item}" selected>${item}</option>
					<c:otherwise>
						<option value="${item}">${item}</option>
					</c:otherwise>
				</c:choose>
				</c:forEach>
				</select>
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
				<select name="color" id="color">
				<c:forEach items="${requestScope.colorValues}" var="item">
				<c:choose>
					<c:when test="${selectedCar.color eq item}"></c:when>
						<option value="${item}" selected>${item}</option>
					<c:otherwise>
						<option value="${item}">${item}</option>
					</c:otherwise>
				</c:choose>
				</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td><label for="style">Style</label></td>
			<td>
				<select name="style" id="style">
				<c:forEach items="${requestScope.styleValues}" var="item">
				<c:choose>
					<c:when test="${selectedCar.style eq item}"></c:when>
						<option value="${item}" selected>${item}</option>
					<c:otherwise>
						<option value="${item}">${item}</option>
					</c:otherwise>
				</c:choose>
				</c:forEach>
				</select>
			</td>
		</tr>
		</table>
		<hr>
		<input type="submit" value="${createCar?'Create':'Modify'}" formaction="main?action=createModifyCar" formmethod="post"/>
		<input type="reset" value="Clear" />
		<input type="submit" value="Back" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>
</body>
</html>