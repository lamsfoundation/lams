<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<fmt:message key='activity.title' var="title"/>
<lams:PageLearner title="${title}" toolSessionID="${sessionMap.toolSessionID}">
	<lams:DefineLater />
</lams:PageLearner>
