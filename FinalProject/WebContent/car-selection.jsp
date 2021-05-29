<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>Car selection criteria form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	
	<form>
		<fieldset>
		<legend>Car criteria</legend>
			<fieldset>
			<legend>Select by</legend>
			<table>
			<tr>
				<td><label for="manufacturer">Manufacturer</label></td>
				<td><input type="checkbox" id="manufacturer" name="select1" value="manufacturer"/></td>
				<td>
					<select name="manufacturer" id="manufacturer" required>
					<c:forEach items="${requestScope.manufacturerValues}" var="item"><option value="${item}">${item}</option></c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td><label for="qualityGrade">Quality grade</label></td>
				<td><input type="checkbox" id="qualityGrade" name="select2" value="qualityGrade"/></td>
				<td>
					<select name="qualityGrade" id="qualityGrade" required>
					<c:forEach items="${requestScope.qualityGradeValues}" var="item"><option value="${item}">${item}</option></c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td><label for="color">Color</label></td>
				<td><input type="checkbox" id="color" name="select3" value="color"/></td>
				<td>
					<select name="color" id="color">
					<c:forEach items="${requestScope.colorValues}" var="item"><option value="${item}">${item}</option></c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td><label for="style">Style</label></td>
				<td><input type="checkbox" id="style" name="select4" value="style"/></td>
				<td>
					<select name="style" id="style">
					<c:forEach items="${requestScope.styleValues}" var="item"><option value="${item}">${item}</option></c:forEach>
					</select>
				</td>
			</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend>Order by</legend>
			<table>
			<tr>
				<td><label for="order1">Rent cost</label></td>
				<td><input type="checkbox" id="order1" name="order1" value="cost"/></td>
				<td>
					<select name="cost" id="order1">
						<option value="asc" >Ascending</option>
						<option value="desc">Descending</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><label for="order2">Model</label></td>
				<td><input type="checkbox" id="order2" name="order2" value="model"/></td>
				<td>
					<select name="model" id="order2">
						<option value="asc" >Ascending</option>
						<option value="desc">Descending</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><label for="order3">Production date</label></td>
				<td><input type="checkbox" id="order3" name="order3" value="productionDate"/></td>
				<td>
					<select name="productionDate" id="order3">
						<option value="asc" >Ascending</option>
						<option value="desc">Descending</option>
					</select>
				</td>
			</tr>
			</table>
		</fieldset>
		<hr>
		<table><tfoot>
		<td><input type="submit" value="Select" formevent="main?action=select_car" formmethod="post"/></td>
		<td><input type="reset" value="Clear" /></td>
		</tfoot></table>
	</fieldset>
	</form>
</body>
</html>