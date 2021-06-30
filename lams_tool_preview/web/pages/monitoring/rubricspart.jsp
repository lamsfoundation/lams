<%@ include file="/common/taglibs.jsp"%>

<style>
	.panel-collapse > .rubrics-user-panel,
	.panel-collapse > #emailPreviewArea {
		margin: 10px;
	}
</style>

<script>
	function closeResultsForLearner() {
		$("#emailPreviewArea").html("");
		$("#emailPreviewArea").hide();
		return false;
	}
	
	function clearMessage() {
		$("#messageArea2").html("");
		return false;
	}
	
	// Prview the email to be sent to the learner
	function previewResultsForLearner(sessionId, userId) {
		$(".btn-disable-on-submit").prop("disabled", true);
		var url = "<c:url value="/monitoring/previewResultsToUser.do"/>";
		clearMessage();
		$("#messageArea2_Busy").show();
		$("#emailPreviewArea").load(
			url,
			{
				sessionMapID: "${sessionMapID}",
				toolContentID: ${sessionMap.toolContentID},
				toolSessionId: sessionId, 
				userID: userId,
				reqID: (new Date()).getTime()
			},
			function() {
				$("#messageArea2_Busy").hide();
				$("#emailPreviewArea").show();
				$(".btn-disable-on-submit").prop("disabled", false);
			}
		);
		return false;
	}
	
	// Send the previewed email to the learner
	function sendResultsForLearner(sessionId, userId, dateTimeStamp) {
		$(".btn-disable-on-submit").prop("disabled", true);
		var url = "<c:url value="/monitoring/sendPreviewedResultsToUser.do"/>";
		clearMessage();
		$("#messageArea2_Busy").show();
		$("#messageArea2").load(
			url,
			{
				sessionMapID: "${sessionMapID}",
				toolContentID: ${sessionMap.toolContentID},
				toolSessionId: sessionId, 
				dateTimeStamp: dateTimeStamp,
				userID: userId,
				reqID: (new Date()).getTime()
			},
			function() {
				$("#messageArea2_Busy").hide();
				closeResultsForLearner();
				$(".btn-disable-on-submit").prop("disabled", false);
			}
		);
		return false;
	}
</script>

<!--For send results feature-->
<i class="fa fa-spinner" style="display:none" id="messageArea2_Busy"></i>
<div class="voffset5" id="messageArea2"></div>

	     
<c:forEach var="learnerData" items="${rubricsLearnerData}">
  	    	
<%-- List learners in the given session --%>
<div class="panel panel-default rubrics-user-panel">
      <div class="panel-heading" role="tab" id="rubrics-user-heading-${learnerData.key.uid}">
      	<span class="panel-title collapsable-icon-left">
      		<a class="collapsed" role="button" data-toggle="collapse" href="#rubrics-user-collapse-${learnerData.key.uid}" 
				aria-expanded="false" aria-controls="rubrics-user-collapse-${learnerData.key.uid}">
			<lams:Portrait userId="${learnerData.key.userId}" hover="false" />
			&nbsp;<c:out value="${learnerData.key.firstName}" escapeXml="true"/>
			&nbsp;<c:out value="${learnerData.key.lastName}" escapeXml="true"/>
		</a>
	</span>
	<button class="btn btn-default pull-right email-button btn-disable-on-submit"
			onClick="javascript:previewResultsForLearner(${toolSessionId}, ${learnerData.key.userId})">
		<fmt:message key="button.preview.results" />
	</button>
      </div>
     
     	<div id="rubrics-user-collapse-${learnerData.key.uid}" class="panel-collapse collapse" 
     	    role="tabpanel" aria-labelledby="rubrics-user-heading-${learnerData.key.uid}">
     	    	<%-- Display ratings given to this user --%>
		<lams:StyledRating criteriaRatings="${learnerData.value}" showJustification="true" alwaysShowAverage="false" currentUserDisplay="true"/>
	</div>
</div>
</c:forEach>

<div class="voffset10" id="emailPreviewArea" style="display:none" ></div>