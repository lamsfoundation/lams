<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:Notebook title="${sessionMap.title}"
	toolSessionID="${sessionMap.toolSessionID}"
	instructions="${sessionMap.reflectInstructions}"
	formActionUrl="submitReflection.do"
	isNextActivityButtonSupported="false"
	finishButtonLabelKey="label.button.submit"/>
