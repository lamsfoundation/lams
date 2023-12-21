<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:Notebook title="${reflectTitle}"
	toolSessionID="${learningForm.toolSessionID}"
	instructions="${reflectInstructions}"
	formActionUrl="finishActivity.do"
	formModelAttribute="learningForm"
	hiddenInputs="toolSessionID,mindmapContent,mode"
	notebookLabelKey="label.notebookEntry"
	isNextActivityButtonSupported="true"
	isLastActivity="${isLastActivity}"
	finishButtonLabelKey="button.submit"
	nextActivityButtonLabelKey="button.finish"/>
		
	