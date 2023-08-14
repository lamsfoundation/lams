<!DOCTYPE html>
<%@ include file="/includes/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants"%>

<lams:Notebook title="${title}"
	toolSessionID="${nbLearnerForm.toolSessionID}"
	instructions="${reflectInstructions}"
	formActionUrl="finish.do"
	formModelAttribute="nbLearnerForm"
	hiddenInputs="toolSessionID,mode"
	notebookLabelKey="titleHeading.reflection"
	isNextActivityButtonSupported="true"
	isLastActivity="${isLastActivity}"
	finishButtonLabelKey="button.submit"
	nextActivityButtonLabelKey="button.finish"
	isNbTool="true"/>

