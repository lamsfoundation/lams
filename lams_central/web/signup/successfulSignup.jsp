<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-html" prefix="html" %>

<h1 align="center">
	<img src="<lams:LAMSURL/>/images/css/lams_login.gif" 
		alt="" width="186" height="90" ></img>
</h1>

<p>&nbsp;</p>

<c:if test="${not empty error}">
	<p class="warning"><fmt:message key="success.errors"/>, <c:out value="${error}" /></p>
</c:if>

<p>
	<fmt:message key="success.msg1"/>: <a href="<lams:LAMSURL />"><fmt:message key="success.login"/></a>.
</p>