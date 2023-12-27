<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:SubmissionDeadline title="${notebookDTO.title}"
	toolSessionID="${messageForm.toolSessionID}"
	submissionDeadline="${notebookDTO.submissionDeadline}"
	finishSessionUrl="/learning/finishActivity.do?toolSessionID=${messageForm.toolSessionID}"
	isLastActivity="${isLastActivity}"
	finishButtonLastActivityLabelKey="button.submit"
	finishButtonLabelKey="button.finish"/>



