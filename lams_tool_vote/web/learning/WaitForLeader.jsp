<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:PageLearner title="${sessionMap.content.title}" toolSessionID="${sessionMap.toolSessionID}">
	<lams:WaitForLeader mode="learner" groupUsers="${groupUsers}"/>	
</lams:PageLearner>
