<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="format" %>
<%@ attribute name="value" required="true" %>

<format:parseDate value="${value}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
<format:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}" type="both" dateStyle="medium" timeStyle="short"/>