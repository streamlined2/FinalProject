<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>User authentication form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	<fmt:message key="user.letterdigit.prompt" bundle="${localeBundle}" var="lettersDigitsPrompt"/>
	<form>

	<fieldset><legend><fmt:message key="user.credentials" bundle="${localeBundle}"/></legend>
		<label for="client"><fmt:message key="user.client" bundle="${localeBundle}"/></label>
		<input type="radio" id="client" name="role" value="client" checked/>
		<label for="manager"><fmt:message key="user.manager" bundle="${localeBundle}"/></label>
		<input type="radio" id="manager" name="role" value="manager"/>
		<label for="admin"><fmt:message key="user.administrator" bundle="${localeBundle}"/></label>
		<input type="radio" id="admin" name="role" value="admin"/>
		<table>
		<tr>
			<td><label for="user"><fmt:message key="user.login" bundle="${localeBundle}"/></label></td>
			<fmt:message key="user.login.prompt" bundle="${localeBundle}" var="userPrompt"/>
			<td><input type="text" id="user" name="user" value="" placeholder="${userPrompt}" size="30" required pattern="${requestScope.loginPattern}" title="${lettersDigitsPrompt}"/></td>
		</tr>
		<tr>
			<td><label for="password"><fmt:message key="user.password" bundle="${localeBundle}"/></label></td>
			<fmt:message key="user.password.prompt" bundle="${localeBundle}" var="passPrompt"/>
			<td><input type="password" id="password" name="password" value="" placeholder="${passPrompt}" size="30" required pattern="${requestScope.passwordPattern}" title="${lettersDigitsPrompt}"/></td>
		</tr>
		</table>
		<hr>
		<div>
		<fmt:message key="user.login.button" bundle="${localeBundle}" var="loginButton"/>
		<input type="submit" value="${loginButton}" formaction="main?action=login" formmethod="post">
		<fmt:message key="user.reset.button" bundle="${localeBundle}" var="resetButton"/>
		<input type="reset" value="${resetButton}" />
		<fmt:message key="user.register.button" bundle="${localeBundle}" var="registerButton"/>
		<input type="submit" value="${registerButton}" formaction="main?action=register" formmethod="post" formnovalidate/>
		</div>
	</fieldset>
	</form>
</body>
</html>