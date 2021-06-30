<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt-prefix" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="checkName" required="true" %>
<%@ attribute name="checkValue" required="true" %>
<%@ attribute name="optionName" required="true" %>
<fmt-prefix:setLocale value="${locale}"/>
<fmt-prefix:setBundle basename="resources.messages" var="localeBundle"/>
<tr>
	<td><label for="${id}">${label}</label></td>
	<td><input type="checkbox" id="${id}" name="${checkName}" value="${checkValue}"/></td>
	<td>
		<select name="${optionName}" id="${id}">
			<option value="asc" ><fmt-prefix:message key="sort.option.ascending" bundle="${localeBundle}"/></option>
			<option value="desc"><fmt-prefix:message key="sort.option.descending" bundle="${localeBundle}"/></option>
		</select>
	</td>
</tr>
