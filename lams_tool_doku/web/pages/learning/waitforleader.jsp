<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:PageLearner title="${dokumaran.title}" toolSessionID="${sessionMap.toolSessionID}">
	<lams:WaitForLeader mode="${sessionMap.mode}" groupUsers="${groupUsers}"/>	
</lams:PageLearner>
