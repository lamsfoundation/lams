<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:Notebook title="${chatDTO.title}"
	toolSessionID="${toolSessionID}"
	instructions="${chatDTO.reflectInstructions}"
	formModelAttribute="learningForm"
	hiddenInputs="chatUserUID"
	isNextActivityButtonSupported="true"
	finishButtonLabelKey="button.submit"
	nextActivityButtonLabelKey="button.finish"
	notebookLabelKey="heading.reflection"/>
			
			

