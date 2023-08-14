<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:Notebook title="${generalLearnerFlowDTO.activityTitle}"
	toolSessionID="${qaLearningForm.toolSessionID}"
	instructions="${generalLearnerFlowDTO.reflectionSubject}"
	formActionUrl="submitReflection.do"
	formModelAttribute="qaLearningForm"
	hiddenInputs="toolSessionID,userID,sessionMapID,totalQuestionCount"
	notebookLabelKey="label.reflection"
	isNextActivityButtonSupported="true"
	isLastActivity="${sessionMap.isLastActivity}"
	finishButtonLabelKey="button.submit"
	nextActivityButtonLabelKey="button.endLearning"/>
