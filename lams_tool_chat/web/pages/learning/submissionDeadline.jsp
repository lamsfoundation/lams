<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:SubmissionDeadline title="${chatDTO.title}"
	toolSessionID="${param.toolSessionID}"
	submissionDeadline="${submissionDeadline}"
	finishSessionUrl="/learning/finishActivity.do?chatUserUID=${chatUserDTO.uid}"
	continueReflectUrl="/learning/openNotebook.do?chatUserUID=${chatUserDTO.uid}"
	isNotebookReeditEnabled="${chatUserDTO.finishedActivity and chatDTO.reflectOnActivity}"
	notebookInstructions="${chatDTO.reflectInstructions}"
	notebookEntry="${chatUserDTO.notebookEntry}"
	isContinueReflectButtonEnabled="${!chatUserDTO.finishedActivity and chatDTO.reflectOnActivity}"
	isLastActivity="${isLastActivity}"
	finishButtonLastActivityLabelKey="button.submit"
	finishButtonLabelKey="button.finish"
	continueReflectButtonLabelKey="button.continue"
	editButtonLabelKey="button.edit"/>


