<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.sessionMapID]}" />

<lams:PageLearner title="${generalLearnerFlowDTO.activityTitle}" toolSessionID="${sessionMap.toolSessionID}">
	<lams:DefineLater defineLaterMessageKey="error.defineLater" />
</lams:PageLearner>
