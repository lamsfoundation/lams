<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:SubmissionDeadline title="${voteGeneralLearnerFlowDTO.activityTitle}"
	toolSessionID="${voteLearningForm.toolSessionID}"
	submissionDeadline="${submissionDeadline}"
	finishSessionUrl="/learning/learnerFinished.do?toolSessionID=${voteLearningForm.toolSessionID}&userID=${voteLearningForm.userID}&userLeader=${voteLearningForm.userLeader}"
	isLastActivity="${isLastActivity}"
	finishButtonLastActivityLabelKey="button.submitActivity"
	finishButtonLabelKey="button.endLearning"/>
