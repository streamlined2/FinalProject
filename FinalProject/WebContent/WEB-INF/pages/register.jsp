<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="styles.css">
	<title>User registration form</title>
</head>
<body>
	<%@include file="common-controls.jsp" %>
	<fmt:message key="user.letterdigit.prompt" bundle="${localeBundle}" var="lettersDigitsPrompt"/>
	<fmt:message key="user.letter.prompt" bundle="${localeBundle}" var="lettersPrompt"/>
	
	<form>
	<fieldset>
		<legend><fmt:message key="user.personalia" bundle="${localeBundle}"/></legend>
		<table>
		<tr>
			<td><label for="firstname"><fmt:message key="user.firstname" bundle="${localeBundle}"/></label></td>
			<fmt:message key="user.firstname.prompt" bundle="${localeBundle}" var="firstnamePrompt"/>
			<td><input type="text" id="firstname" name="firstname" value="${param.firstname}" placeholder="${firstnamePrompt}" size="50" pattern="${requestScope.namePattern}" title="${lettersPrompt}" required/></td>
		</tr>
		<tr>
			<td><label for="lastname"><fmt:message key="user.lastname" bundle="${localeBundle}"/></label></td>
			<fmt:message key="user.lastname.prompt" bundle="${localeBundle}" var="lastnamePrompt"/>
			<td><input type="text" id="lastname" name="lastname" value="${param.lastname}" placeholder="${lastnamePrompt}" size="50" pattern="${requestScope.namePattern}" title="${lettersPrompt}" required/></td>
		</tr>
		<c:if test="${administrativeTasks}">
		<tr>
			<td><label for="role"><fmt:message key="user.role" bundle="${localeBundle}"/></label></td>
			<td>
				<select name="role" id="role">
					<option value="client"><fmt:message key="user.client" bundle="${localeBundle}"/></option>
					<option value="manager"><fmt:message key="user.manager" bundle="${localeBundle}"/></option>
					<option value="admin"><fmt:message key="user.administrator" bundle="${localeBundle}"/></option>
				</select>
			</td>
		</tr>
		</c:if>
		<tr>
			<td><label for="user"><fmt:message key="user.login" bundle="${localeBundle}"/></label></td>
			<fmt:message key="user.login.prompt" bundle="${localeBundle}" var="userPrompt"/>
			<td><input type="text" id="user" name="user" value="${param.user}" placeholder="${userPrompt}" size="30" required pattern="${requestScope.loginPattern}" title="${lettersDigitsPrompt}"/></td>
		</tr>
		<tr>
			<td><label for="password"><fmt:message key="user.password" bundle="${localeBundle}"/></label></td>
			<fmt:message key="user.password.prompt" bundle="${localeBundle}" var="passPrompt"/>
			<td><input type="password" id="password" name="password" value="${param.password}" placeholder="${passPrompt}" size="30" required pattern="${requestScope.passwordPattern}" title="${lettersDigitsPrompt}"/></td>
		</tr>
		<tr>
			<td><label for="password2"><fmt:message key="user.repeat.password" bundle="${localeBundle}"/></label></td>
			<fmt:message key="user.repeat.password.prompt" bundle="${localeBundle}" var="repeatPassPrompt"/>
			<td><input type="password" id="password2" name="password2" value="" placeholder="${repeatPassPrompt}" size="30" required pattern="${requestScope.passwordPattern}" title="${lettersDigitsPrompt}"/></td>
		</tr>
		</table>
		<hr>
		<fmt:message key="user.register.button" bundle="${localeBundle}" var="registerButton"/>
		<input type="submit" value="${registerButton}" formaction="main?action=register_new" formmethod="post"/>
		<fmt:message key="user.reset.button" bundle="${localeBundle}" var="resetButton"/>
		<input type="reset" value="${resetButton}" />
		<fmt:message key="user.back.button" bundle="${localeBundle}" var="backButton"/>
		<input type="submit" value="${backButton}" formaction="main?action=back" formmethod="post" formnovalidate/>
	</fieldset>
	</form>
</body>
</html>