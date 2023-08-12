<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<fmt:message key='activity.title' var="title"/>
<lams:PageLearner title="${title}" toolSessionID="${toolSessionID}">
	<lams:DefineLater defineLaterMessageKey="message.defineLaterSet" />
</lams:PageLearner>
