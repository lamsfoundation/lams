<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:PageLearner title="${content.title}" toolSessionID="${sessionMap.toolSessionID}" >
	<lams:WaitForLeader mode="${mode}" groupUsers="${groupUsers}"/>	
</lams:PageLearner>

