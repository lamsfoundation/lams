<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:SubmissionDeadline title="${sessionMap.title}"
	toolSessionID="${sessionMap.toolSessionID}"
	submissionDeadline="${sessionMap.submissionDeadline}"
	finishSessionUrl="/learning/finish.do?sessionMapID=${sessionMapID}"
	isLastActivity="${sessionMap.isLastActivity}"/>


