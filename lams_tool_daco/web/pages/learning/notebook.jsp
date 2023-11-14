<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="daco" value="${sessionMap.daco}" />
	
<lams:Notebook title="${daco.title}"
	toolSessionID="${sessionMap.toolSessionID}"
	formModelAttribute="messageForm"
	hiddenInputs="userId,sessionId,sessionMapID"
	instructions="${daco.reflectInstructions}"
	isNextActivityButtonSupported="true"
	isLastActivity="${sessionMap.isLastActivity}"
	notebookLabelKey="label.monitoring.notebook"
	finishButtonLabelKey="label.learning.submit"
	nextActivityButtonLabelKey="label.learning.finished"/>
