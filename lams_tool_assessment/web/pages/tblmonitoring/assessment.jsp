<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>
<c:set var="showQuestionMonitoringActionButtons" value="${attemptedLearnersNumber > 0}" />

<%@ include file="/pages/monitoring/parts/discloseAnswers5.jsp"%>

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
		class="container-fluid ${isIraAssessment ? 'assessment-questions-pane-ira' : ''}">

	<c:if test="${isIraAssessment}">
		<div class="row">
			<div class="col-10 offset-1">
				<button class="btn btn-secondary float-end" type="button"
					 onclick="javascript:showStudentChoices()">
					<i class="fa fa-gauge"></i>
					<fmt:message key="label.show.students.choices"/>
				</button>   
				<h3>
					<fmt:message key="label.ira.questions.marks"/>
				</h3>
			</div>
		</div>
	</c:if>
	
	<!-- Notifications -->  
	<div class="row mb-3">
		<div class="col-10 offset-1 pt-1">
			<c:if test="${not isIraAssessment}">
				<button class="btn btn-secondary float-end" type="button"
					 onclick="javascript:showStudentChoices()">
					<i class="fa fa-gauge"></i>
					<fmt:message key="label.show.students.choices"/>
				</button>   
			</c:if>
			<h4>
				<i class="fa fa-users" style="color:gray" ></i> 
				<fmt:message key="label.attendance"/>: <span>${attemptedLearnersNumber}</span>/<span class="total-learners-number"></span> 
			</h4> 
		</div>
	</div>
		
	<c:if test="${allowDiscloseAnswers and showQuestionMonitoringActionButtons}">
		<%-- Release correct/groups answers for all questions in this assessment --%>
		<div class="row mb-3 disclose-all-button-group">
			<div class="col-4 offset-7 text-end">
				<button class="btn btn-secondary disclose-all-correct-button" type="button">
					<fmt:message key="label.disclose.all.correct.answers"/>
				</button>
				<button class="btn btn-secondary disclose-all-groups-button" type="button">
					<fmt:message key="label.disclose.all.groups.answers"/>
				</button>
			</div>
		</div>
	</c:if>		

	<div class="results" data-tool-content-id="${toolContentID}"></div>
</div>
