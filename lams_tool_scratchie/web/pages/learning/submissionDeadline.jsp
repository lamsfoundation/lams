<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:SubmissionDeadline title="${sessionMap.title}"
	toolSessionID="${sessionMap.toolSessionID}"
	submissionDeadline="${sessionMap.submissionDeadline}"
	finishSessionUrl="/learning/finish.do?sessionMapID=${sessionMapID}"
	continueReflectUrl="/learning/newReflection.do?sessionMapID=${sessionMapID}"
	isNotebookReeditEnabled="${sessionMap.userFinished and sessionMap.reflectOn && empty sessionMap.submissionDeadline}"
	notebookInstructions="${sessionMap.reflectInstructions}"
	notebookEntry="${sessionMap.reflectEntry}"
	isContinueReflectButtonEnabled="${sessionMap.reflectOn && (not sessionMap.userFinished) && empty sessionMap.submissionDeadline}"
	isLastActivity="${sessionMap.isLastActivity}"
	deadlineAlertLabelKey="label.sorry.the.deadline.has.passed" />


