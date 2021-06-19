<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="action" required="true" %>
<%@ attribute name="image" required="true" %>
<form>
	<button type="submit" width="24" height="24" formaction="main?action=${action}" formmethod="post">
			<img src="${image}">
	</button>
</form>