<%@ include file="/includes/taglibs.jsp"%>

<h1>
	<fmt:message key="run.offline.message" />
</h1>
<c:set var="finish">
	<html:rewrite page="/learning/finish.do?toolSessionID=${param.toolSessionID}" />
</c:set>

<html:button property="finish" onclick="javascript:location.href='${finish}';" disabled="${finishedLock}" style="width:120px" styleClass="buttonStyle">
	<fmt:message key="label.finish" />
</html:button>
