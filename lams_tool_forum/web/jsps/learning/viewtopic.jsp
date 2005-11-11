<%@ include file="/includes/taglibs.jsp" %>

<html:errors property="error" />
<c:set var="backToForum">
	<html:rewrite page="/learning/viewForum.do?toolSessionID=${sessionScope.toolSessionID}" />
</c:set> 
<html:button property="backToForum" onclick="javascript:location.href='${backToForum}';">
	<fmt:message key="label.back.to.forum"/>
</html:button>
<br>
<%@ include file="/jsps/learning/message/topicview.jsp" %>
