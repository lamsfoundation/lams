<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:PageLearner title="${content.title}" toolSessionID="${toolSessionID}">
	<lams:WaitForLeader mode="${mode}" groupUsers="${groupUsers}"/>	
</lams:PageLearner>