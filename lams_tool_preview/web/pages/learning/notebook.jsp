<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:Notebook title="${sessionMap.title}"
	toolSessionID="${sessionMap.toolSessionId}"
	instructions="${sessionMap.reflectInstructions}"
	formActionUrl="submitReflection.do"
	isNextActivityButtonSupported="true"
	finishButtonLabelKey="label.finish"
	notebookLabelKey="title.reflection"/>