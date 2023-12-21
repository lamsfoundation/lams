<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:PageLearner title="${content.title}" toolSessionID="${toolSessionID}">
	<lams:WaitForLeader mode="learner" groupUsers="${groupUsers}"/>	
</lams:PageLearner>
