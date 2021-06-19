<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${!empty sessionScope.user}">
<form>
	<button type="submit" width="24" height="24" formaction="main?action=logout" formmethod="post">
			<img src="logoutbutton.png">
	</button>
</form>
</c:if>