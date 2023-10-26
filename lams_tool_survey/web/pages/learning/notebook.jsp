<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:Notebook title="${sessionMap.title}"
	toolSessionID="${sessionMap.toolSessionID}"
	instructions="${sessionMap.reflectInstructions}"
	formModelAttribute="messageForm"
	notebookLabelKey="title.reflection"
	isNextActivityButtonSupported="false"
	finishButtonLabelKey="label.finish"/>

