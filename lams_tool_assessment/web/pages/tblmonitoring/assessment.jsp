<%@ include file="/common/taglibs.jsp"%>
<c:set var="newLineChar" value="<%= \"\r\n\" %>" />
<c:set var="showQuestionMonitoringActionButtons" value="${attemptedLearnersNumber > 0}" />

<%@ include file="/pages/monitoring/parts/discloseAnswers.jsp"%>
<script>
	//for allquestions.jsp to know whether to display an additional button
	var showQuestionDetailsButton = false;
	
	$(document).ready(function(){
		var assessmentPane = $('#assessment-pane-${toolContentID}');
		//insert total learners number taken from the parent tblmonitor.jsp
		$(".total-learners-number", assessmentPane).text(TOTAL_LESSON_LEARNERS_NUMBER);
		
		loadResultsPane($('.results', assessmentPane), false);
	});
	
	function showStudentChoices() {
		// these methods come from tblmonitor.jsp and aes.jsp in lams_monitoring
		if (${not empty isIraAssessment and isIraAssessment}) {
			loadTab('iratStudentChoices', $('#load-irat-student-choices-tab-btn'));
			return;
		}
		loadAePane(${toolContentID}, 'studentChoices');
	}
</script>

<%-- For AEs tab the panes are defined in TBL monitor, for IRA we need to define it here --%>
<div ${isIraAssessment ? 'id="assessment-pane-' += toolContentID += '"' : '' }
		class="container-main ms-4 ${isIraAssessment ? 'assessment-questions-pane-ira' : ''}">

	<c:if test="${isIraAssessment}">
		<button class="btn btn-primary btn-icon-return float-end" type="button" onclick="showStudentChoices()">
			<fmt:message key="label.show.students.choices"/>
		</button>   
		<h3>
			<fmt:message key="label.ira.questions.marks"/>
		</h3>
	</c:if>
	
	<!-- Notifications -->  
	<div class="mb-3 pt-1">
		<c:if test="${not isIraAssessment}">
			<button class="btn btn-primary btn-icon-return float-end" type="button" onclick="showStudentChoices()">
				<fmt:message key="label.show.students.choices"/>
			</button>   
		</c:if>
		<div>
			<i class="fa fa-users" style="color:gray" ></i> 
			<fmt:message key="label.attendance"/>: <span>${attemptedLearnersNumber}</span>/<span class="total-learners-number"></span> 
		</div> 
	</div>
		
	<c:if test="${allowDiscloseAnswers and showQuestionMonitoringActionButtons}">
		<%-- Release correct/groups answers for all questions in this assessment --%>
		<div class="disclose-all-button-group clearfix mb-3">
			<div class=" float-end btn-group">
				<button class="btn btn-light disclose-all-correct-button" type="button">
					<i class="fa-solid fa-eye me-1"></i>
					<fmt:message key="label.disclose.all.correct.answers"/>
				</button>
				<button class="btn btn-light disclose-all-groups-button" type="button">
					<i class="fa-solid fa-eye me-1"></i>
					<fmt:message key="label.disclose.all.groups.answers"/>
				</button>
			</div>
		</div>
	</c:if>		

	<div class="results" data-tool-content-id="${toolContentID}"></div>
</div>
