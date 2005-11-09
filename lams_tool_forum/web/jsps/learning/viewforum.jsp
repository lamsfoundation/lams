<%@ include file="/includes/taglibs.jsp" %>

<html:errors property="error" />
<div align="center">
	<%@ include file="/jsps/learning/message/topiclist.jsp" %>
</div>
<br>
<c:set var="newtopic">
	<html:rewrite page="/learning/newTopic.do" />
</c:set> 
<c:set var="finish">
	<html:rewrite page="/learning/finish.do" />
</c:set> 
	
<html:button property="newtopic" onclick="javascript:location.href='${newtopic}';">
	<fmt:message key="label.newtopic"/>
</html:button>
<html:button property="refresh" >
	<fmt:message key="label.refresh"/>
</html:button>
<html:button property="finish" onclick="javascript:location.href='${finish}';">
	<fmt:message key="label.finish"/>
</html:button>