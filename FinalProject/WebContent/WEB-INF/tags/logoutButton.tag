<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ attribute name="action" required="true" %>
<%@ attribute name="image" required="true" %>
<c:if test="${!empty sessionScope.user}">
<form>
	<tags:actionButton action="${action}" image="${image}"/>
</form>
</c:if>