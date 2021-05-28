<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>
	<p>
	<table><th>
		<td>
				<form action="main?action=locale" method="post">
		 			<select name="locale" onchange="this.form.submit()">
						<option label="En" value="en">English</option>
						<option label="Uk" value="uk">Ukrainian</option>
					</select>
				</form>
		</td>
		<td>${sessionScope.locale.displayLanguage}</td>
		<td><form>
			<input type="submit" value="Logout" formaction="main?action=logout" formmethod="post"/>
		</form></td>
		<td>${sessionScope.user}</td>
		<td><div class="error">${sessionScope.error}</div></td>
	</th></table>
	<p>
</body>
</html>