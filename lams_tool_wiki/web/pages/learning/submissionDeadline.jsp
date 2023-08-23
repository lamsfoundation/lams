<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:SubmissionDeadline title="${wikiDTO.title}"
	toolSessionID="${learningForm.toolSessionID}"
	submissionDeadline="${submissionDeadline}"
	finishSessionUrl="/learning/finishActivity.do?toolSessionID=${learningForm.toolSessionID}&mode=${mode}"
	continueReflectUrl="/learning/openNotebook.do?toolSessionID=${learningForm.toolSessionID}&mode=${mode}"
	isNotebookReeditEnabled="${userDTO.finishedActivity and wikiDTO.reflectOnActivity}"
	notebookInstructions="${wikiDTO.reflectInstructions}"
	notebookEntry="${userDTO.notebookEntry}"
	isContinueReflectButtonEnabled="${!userDTO.finishedActivity and wikiDTO.reflectOnActivity}"
	isLastActivity="${isLastActivity}"
	finishButtonLastActivityLabelKey="button.submit"
	finishButtonLabelKey="button.finish"
	continueReflectButtonLabelKey="button.continue"
	editButtonLabelKey="button.edit"/>
