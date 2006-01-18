<%@ include file="/includes/taglibs.jsp" %>

<html:errors property="error" />
<div align="center">
	<%@ include file="/jsps/learning/message/topiclist.jsp" %>
</div>
<br>
<c:set var="newtopic">
	<html:rewrite page="/learning/newTopic.do" />
</c:set> 
<c:set var="refresh">
	<html:rewrite page="/learning/viewForum.do?toolSessionID=${param.toolSessionID}" />
</c:set> 
<c:set var="finish">
	<html:rewrite page="/learning/finish.do?toolSessionID=${param.toolSessionID}" />
</c:set> 
	
<html:button property="newtopic" onclick="javascript:location.href='${newtopic}';" disabled="${finishedLock}" >
	<fmt:message key="label.newtopic"/>
</html:button>
<html:button property="refresh" onclick="javascript:location.href='${refresh}';" disabled="${finishedLock}" >
	<fmt:message key="label.refresh"/>
</html:button>
<html:button property="finish" onclick="javascript:location.href='${finish}';" disabled="${finishedLock}" >
	<fmt:message key="label.finish"/>
</html:button>