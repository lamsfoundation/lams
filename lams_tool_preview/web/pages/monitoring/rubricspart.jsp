<%@ include file="/common/taglibs.jsp"%>

<style>
	.panel-collapse > .rubrics-user-panel,
	.panel-collapse > #emailPreviewArea {
		margin: 10px;
	}
</style>

<script>

	function getResultsElement(sessionId, selector) {
		let element = null;
		if (sessionId) {
			// if Peer Review is grouped, try to find the element within own group
			element = $('#collapse' + sessionId + ' ' + selector);
		}
		if (!element || element.length == 0) {
			element = $(selector);
		}
		return element;
	}
	
	function closeResultsForLearner(sessionId) {
		let emailPreviewArea = getResultsElement(sessionId, '.emailPreviewArea');
		emailPreviewArea .html("").hide();
		return false;
	}
	
	// Preview the email to be sent to the learner
	function previewResultsForLearner(sessionId, userId) {
		let buttons = getResultsElement(sessionId, ".btn-disable-on-submit"),
			messageArea = getResultsElement(sessionId, ".messageArea2"),
			messageAreaBusy = getResultsElement(sessionId, ".messageArea2_Busy"),
			emailPreviewArea = getResultsElement(sessionId, '.emailPreviewArea'),
			url = "<c:url value="/monitoring/previewResultsToUser.do"/>";
			
		buttons.prop("disabled", true);
		messageArea.html("");
		messageAreaBusy.show();
		
		emailPreviewArea.load(
			url,
			{
				sessionMapID: "${sessionMapID}",
				toolContentID: ${sessionMap.toolContentID},
				toolSessionId: sessionId, 
				userID: userId,
				reqID: (new Date()).getTime()
			},
			function() {
				messageAreaBusy.hide();
				emailPreviewArea.show();
				// scroll to preview area
				$('html, body').animate({scrollTop: emailPreviewArea.offset().top}, 1000);
				buttons.prop("disabled", false);
			}
		);
		return false;
	}
	
	// Send the previewed email to the learner
	function sendResultsForLearner(sessionId, userId, dateTimeStamp) {
		let buttons = getResultsElement(sessionId, ".btn-disable-on-submit"),
			messageArea = getResultsElement(sessionId, ".messageArea2"),
			messageAreaBusy = getResultsElement(sessionId, ".messageArea2_Busy"),
			url = "<c:url value="/monitoring/sendPreviewedResultsToUser.do"/>";
		
		buttons.prop("disabled", true);
		messageArea.html("");
		messageAreaBusy.show();
		
		messageArea.load(
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
				messageAreaBusy.hide();
				closeResultsForLearner(sessionId);
				buttons.prop("disabled", false);
			}
		);
		return false;
	}
</script>

<!--For send results feature-->
<i class="fa fa-spinner messageArea2_Busy" style="display:none"></i>
<div class="voffset5 messageArea2"></div>

	     
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
			onClick="javascript:previewResultsForLearner(${toolSessionId}, ${learnerData.key.userId}, this)">
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

<div class="voffset10 emailPreviewArea" style="display:none" ></div>