<%@ include file="/includes/taglibs.jsp"%>
<c:set var="mode" value="${sessionScope.mode}" />
<div align="center">
	<%@ include file="/jsps/learning/message/topiclist.jsp"%>

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
	<c:if test='${mode != "teacher"}'>
		<html:button property="newtopic" onclick="javascript:location.href='${newtopic}';" disabled="${finishedLock}" style="width:120px" styleClass="buttonStyle">
			<fmt:message key="label.newtopic" />
		</html:button>
	</c:if>
	<html:button property="refresh" onclick="javascript:location.href='${refresh}';" disabled="${finishedLock}" style="width:120px" styleClass="buttonStyle">
		<fmt:message key="label.refresh" />
	</html:button>
	<c:if test='${mode != "teacher"}'>
		<html:button property="finish" onclick="javascript:location.href='${finish}';" disabled="${finishedLock}" style="width:120px" styleClass="buttonStyle">
			<fmt:message key="label.finish" />
		</html:button>
	</c:if>

</div>
