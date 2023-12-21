<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:Notebook title="${voteGeneralLearnerFlowDTO.activityTitle}"
	toolSessionID="${voteLearningForm.toolSessionID}"
	instructions="${voteGeneralLearnerFlowDTO.reflectionSubject}"
	formModelAttribute="voteLearningForm"
	hiddenInputs="toolSessionID,userID,userLeader,groupLeaderName,groupLeaderUserId,useSelectLeaderToolOuput"
	notebookLabelKey="label.reflection"
	isNextActivityButtonSupported="false"
	finishButtonLabelKey="button.endLearning"/>









