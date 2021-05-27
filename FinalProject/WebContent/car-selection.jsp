<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
						<option value="BMW">BMW</option>
						<option value="DAIMLER">Daimler</option>
						<option value="FORD">Ford</option>
						<option value="GM">GM</option>
						<option value="HONDA">Honda</option>
						<option value="HYUNDAI">Hyundai</option>
						<option value="MAZDA">Mazda</option>
						<option value="NISSAN">Nissan</option>
						<option value="SKODA">Skoda</option>
						<option value="TESLA">Tesla</option>
						<option value="TOYOTA">Toyota</option>
						<option value="VOLKSWAGEN">Volkswagen</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><label for="qualityGrade">Quality grade</label></td>
				<td><input type="checkbox" id="qualityGrade" name="select2" value="qualityGrade"/></td>
				<td>
					<select name="qualityGrade" id="qualityGrade" required>
						<option value="BASIC">Basic</option>
						<option value="ECONOMY">Economy</option>
						<option value="PREMIUM">Premium</option>
						<option value="BUSINESS">Business</option>
						<option value="FIRSTCLASS">First class</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><label for="color">Color</label></td>
				<td><input type="checkbox" id="color" name="select3" value="color"/></td>
				<td>
					<select name="color" id="color">
						<option value="BLUE">Blue</option>
						<option value="BLACK">Black</option>
						<option value="GREEN">Green</option>
						<option value="RED">Red</option>
						<option value="YELLOW">Yellow</option>
						<option value="WHITE">White</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><label for="style">Style</label></td>
				<td><input type="checkbox" id="style" name="select4" value="style"/></td>
				<td>
					<select name="style" id="style">
						<option value="CONVERTIBLE">Convertible</option>
						<option value="COUPE">Coupe</option>
						<option value="HATCHBACK">Hatchback</option>
						<option value="MINIVAN">Minivan</option>
						<option value="SEDAN">Sedan</option>
						<option value="SPORTS_CAR">Sports car</option>
						<option value="STATION_WAGON">Station wagon</option>
						<option value="SUV">SUV</option>
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
		<input type="submit" value="Select" formevent="main?action=select_car" formmethod="post"/>
		<input type="reset" value="Clear" />
	</fieldset>
	</form>
</body>
</html>