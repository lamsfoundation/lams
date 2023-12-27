<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:SubmissionDeadline title="${title}"
	toolSessionID="${mcLearningForm.toolSessionID}"
	submissionDeadline="${submissionDeadline}"
	finishSessionUrl="/learning/displayMc.do?learnerFinished=true&toolContentID=${mcLearningForm.toolContentID}&toolSessionID=${mcLearningForm.toolSessionID}&httpSessionID=${mcLearningForm.httpSessionID}&userID=${mcLearningForm.userID}"
	isLastActivity="${isLastActivity}" />
