<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.sessionMapID]}" />

<lams:SubmissionDeadline title="${generalLearnerFlowDTO.activityTitle}"
	toolSessionID="${qaLearningForm.toolSessionID}"
	submissionDeadline="${sessionMap.submissionDeadline}"
	finishSessionUrl="/learning/endLearning.do?toolSessionID=${qaLearningForm.toolSessionID}&userID=${qaLearningForm.userID}&sessionMapID=${qaLearningForm.sessionMapID}&totalQuestionCount=${qaLearningForm.totalQuestionCount}"
	isLastActivity="${sessionMap.isLastActivity}" 
	finishButtonLastActivityLabelKey="button.submit"
	finishButtonLabelKey="button.endLearning"/>
