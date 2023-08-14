<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:Notebook title="${sessionMap.title}"
	toolSessionID="${sessionMap.toolSessionID}"
	instructions="${sessionMap.reflectInstructions}"
	formActionUrl="submitReflection.do"
	notebookLabelKey="title.reflection"
	isNextActivityButtonSupported="true"
	isLastActivity="${isLastActivity}"
	finishButtonLabelKey="button.submit"
	nextActivityButtonLabelKey="button.finish"/>
