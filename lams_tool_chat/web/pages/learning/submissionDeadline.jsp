<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:SubmissionDeadline title="${chatDTO.title}"
	toolSessionID="${param.toolSessionID}"
	submissionDeadline="${submissionDeadline}"
	finishSessionUrl="/learning/finishActivity.do?chatUserUID=${chatUserDTO.uid}"
	isLastActivity="${isLastActivity}"
	finishButtonLastActivityLabelKey="button.submit"
	finishButtonLabelKey="button.finish"/>


