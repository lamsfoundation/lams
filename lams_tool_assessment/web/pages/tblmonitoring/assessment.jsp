<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>


<%@ include file="/pages/monitoring/parts/discloseAnswers.jsp"%>

<script>
	//for allquestions.jsp to know whether to display an additional button
	const showQuestionDetailsButton = false;
	
	$(document).ready(function(){
		var assessmentPane = $('#assessment-pane-${toolContentID}');
		//insert total learners number taken from the parent tblmonitor.jsp
		$(".total-learners-number", assessmentPane).text(TOTAL_LESSON_LEARNERS_NUMBER);
		
		loadResultsPane($('.results', assessmentPane), false);
	});
	
	function showStudentChoices() {
		// these methods come from tblmonitor.jsp and aes.jsp in lams_monitoring
		if (${not empty isIraAssessment and isIraAssessment}) {
			loadTab('iraAssessmentStudentChoices', ${toolContentID});
			return;
		}
		loadAePane(${toolContentID}, 'studentChoices');
	}
</script>

<%-- For AEs tab the panes are defined in TBL monitor, for IRA we need to define it here --%>
<div ${isIraAssessment ? 'id="assessment-pane-' += toolContentID += '"' : '' }>
	<c:if test="${isIraAssessment}">
		<div class="row">
			<div class="col-xs-12">
				<h3>
					<fmt:message key="label.ira.questions.marks"/>
				</h3>
			</div>
		</div>
	</c:if>
	
	<!-- Notifications -->  
	<div class="row">
		<div class="col-xs-6 col-sm-4">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<i class="fa fa-users" style="color:gray" ></i> 
						<fmt:message key="label.attendance"/>: <span>${attemptedLearnersNumber}</span>/<span class="total-learners-number"></span> 
					</h4> 
				</div>
			</div>
		</div>
		<div class="col-xs-6 col-sm-8">
			<div class="btn btn-sm btn-default pull-right"
				 onclick="javascript:showStudentChoices()">
				<i class="fa fa-file"></i>
				<fmt:message key="label.show.students.choices"/>
			</div>   
		</div>                 
	</div>
		
	<c:if test="${allowDiscloseAnswers}">
		<%-- Release correct/groups answers for all questions in this assessment --%>
		<div class="row">
			<div class="col-xs-12 col-md-12 col-lg-12">
				<div class="btn-group-sm pull-right disclose-all-button-group">
					<div class="btn btn-default disclose-all-correct-button">
						<fmt:message key="label.disclose.all.correct.answers"/>
					</div>
					<div class="btn btn-default disclose-all-groups-button">
						<fmt:message key="label.disclose.all.groups.answers"/>
					</div>
				</div>
			</div>
		</div>
	</c:if>		

	<div class="results" data-tool-content-id="${toolContentID}"></div>
</div>