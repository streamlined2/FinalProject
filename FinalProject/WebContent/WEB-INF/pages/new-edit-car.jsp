<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
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
				<select name="manufacturer" id="manufacturer" required>
				<c:forEach items="${manufacturerValues}" var="item">
				<c:choose>
					<c:when test="${selectedCar.manufacturer.label eq item}">
						<option value="${item}" selected>${item}</option>
					</c:when>
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
				<c:forEach items="${qualityGradeValues}" var="item">
				<c:choose>
					<c:when test="${selectedCar.qualityGrade.label eq item}">
						<option value="${item}" selected>${item}</option>
                    </c:when>
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
				<c:forEach items="${colorValues}" var="item">
				<c:choose>
					<c:when test="${selectedCar.color.label eq item}">
						<option value="${item}" selected>${item}</option>
                    </c:when>
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
				<c:forEach items="${styleValues}" var="item">
				<c:choose>
					<c:when test="${selectedCar.style.label eq item}">
						<option value="${item}" selected>${item}</option>
                    </c:when>
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