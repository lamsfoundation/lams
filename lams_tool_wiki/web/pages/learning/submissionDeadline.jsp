<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:SubmissionDeadline title="${wikiDTO.title}"
	toolSessionID="${learningForm.toolSessionID}"
	submissionDeadline="${submissionDeadline}"
	finishSessionUrl="/learning/finishActivity.do?toolSessionID=${learningForm.toolSessionID}&mode=${mode}"
	isLastActivity="${isLastActivity}"
	finishButtonLastActivityLabelKey="button.submit"
	finishButtonLabelKey="button.finish"/>
