<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:Notebook title="${mcGeneralLearnerFlowDTO.activityTitle}"
	toolSessionID="${mcLearningForm.toolSessionID}"
	instructions="${mcGeneralLearnerFlowDTO.reflectionSubject}"
	formActionUrl="displayMc.do"
	formModelAttribute="mcLearningForm"
	hiddenInputs="toolContentID,toolSessionID,httpSessionID,userID,submitReflection"
	notebookLabelKey="label.reflection"
	isNextActivityButtonSupported="false"
	finishButtonLabelKey="button.endLearning"/>
