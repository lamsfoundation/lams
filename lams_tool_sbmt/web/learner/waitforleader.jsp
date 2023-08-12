<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:PageLearner title="${sessionMap.title}" toolSessionID="${sessionMap.toolSessionID}">
	<lams:WaitForLeader mode="${sessionMap.mode}" groupUsers="${groupUsers}" isUserIdPropertyCapitalized="true"/>	
</lams:PageLearner>
