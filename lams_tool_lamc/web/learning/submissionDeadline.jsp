<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:SubmissionDeadline title="${title}"
	toolSessionID="${mcLearningForm.toolSessionID}"
	submissionDeadline="${submissionDeadline}"
	finishSessionUrl="/learning/displayMc.do?learnerFinished=true&toolContentID=${mcLearningForm.toolContentID}&toolSessionID=${mcLearningForm.toolSessionID}&httpSessionID=${mcLearningForm.httpSessionID}&userID=${mcLearningForm.userID}"
	continueReflectUrl="/learning/displayMc.do?forwardtoReflection=true&toolContentID=${mcLearningForm.toolContentID}&toolSessionID=${mcLearningForm.toolSessionID}&httpSessionID=${mcLearningForm.httpSessionID}&userID=${mcLearningForm.userID}"
	isNotebookReeditEnabled="false"
	isContinueReflectButtonEnabled="${mcGeneralLearnerFlowDTO.reflection == 'true'}"
	isLastActivity="${isLastActivity}" />
