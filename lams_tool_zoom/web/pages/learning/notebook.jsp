<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:Notebook title="${contentDTO.title}"
	toolSessionID="${learningForm.toolSessionID}"
	instructions="${contentDTO.reflectInstructions}"
	isNextActivityButtonSupported="true"
	isLastActivity="${isLastActivity}"
	formModelAttribute="learningForm"
	hiddenInputs="toolSessionID"
	notebookLabelKey="label.notebookEntry"
	finishButtonLabelKey="button.submit"
	nextActivityButtonLabelKey="button.finish"/>
