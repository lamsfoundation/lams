<%@ include file="/common/taglibs.jsp"%>

<input type="hidden" name="overallFeedbackList" id="overallFeedbackList" />
<iframe id="advancedInputArea" name="advancedInputArea" 
		style="width:100%; height:100%; border:0; display:block;" 
		frameborder="no" scrolling="no" src="<c:url value='/authoring/initOverallFeedback.do'/>?sessionMapID=${assessmentForm.sessionMapID}">
</iframe>
