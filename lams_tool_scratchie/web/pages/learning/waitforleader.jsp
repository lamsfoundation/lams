<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:PageLearner title="${scratchie.title}" toolSessionID="${toolSessionID}">
	<lams:WaitForLeader mode="${mode}" groupUsers="${groupUsers}"/>	
</lams:PageLearner>