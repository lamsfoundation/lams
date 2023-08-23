<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:SubmissionDeadline title="${mindmapDTO.title}"
	toolSessionID="${learningForm.toolSessionID}"
	submissionDeadline="${submissionDeadline}"
	finishSessionUrl="/learning/finishActivity.do?toolSessionID=${learningForm.toolSessionID}"
	continueReflectUrl=""
	isNotebookReeditEnabled="false"
	isContinueReflectButtonEnabled="false"
	isLastActivity="${isLastActivity}"
	finishButtonLastActivityLabelKey="button.submit"
	finishButtonLabelKey="button.finish"/>
