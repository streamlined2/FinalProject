<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ tag body-content="scriptless" %>
<jsp:doBody var="body" scope="page"/>
${fn:toUpperCase(pageScope.body)}