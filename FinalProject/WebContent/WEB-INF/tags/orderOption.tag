<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="id" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="checkName" required="true" %>
<%@ attribute name="checkValue" required="true" %>
<%@ attribute name="optionName" required="true" %>
<tr>
	<td><label for="${id}">${label}</label></td>
	<td><input type="checkbox" id="${id}" name="${checkName}" value="${checkValue}"/></td>
	<td>
		<select name="${optionName}" id="${id}">
			<option value="asc" >Ascending</option>
			<option value="desc">Descending</option>
		</select>
	</td>
</tr>
