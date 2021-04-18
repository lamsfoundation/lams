<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>

<style>
	#completion-charts-container > div {
		padding: 5rem 0;
	}
</style>

<script src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
<script src="<lams:LAMSURL/>includes/javascript/jquery.plugin.js"></script>
<script src="<lams:LAMSURL/>includes/javascript/jquery.countdown.js"></script>
<script src="<lams:LAMSURL/>includes/javascript/chart.bundle.min.js"></script>
<script src="<lams:WebAppURL />includes/javascript/chart.js"></script>

<script>	
	var WEB_APP_URL = '<lams:WebAppURL />',

		LABELS = $.extend(LABELS, {
			<fmt:message key="label.monitoring.summary.completion" var="ACTIVITY_COMPLETION_CHART_TITLE_VAR"/>
			ACTIVITY_COMPLETION_CHART_TITLE : '<c:out value="${ACTIVITY_COMPLETION_CHART_TITLE_VAR}" />',
			<fmt:message key="label.monitoring.summary.completion.possible" var="ACTIVITY_COMPLETION_CHART_POSSIBLE_LEARNERS_VAR"/>
			ACTIVITY_COMPLETION_CHART_POSSIBLE_LEARNERS : '<c:out value="${ACTIVITY_COMPLETION_CHART_POSSIBLE_LEARNERS_VAR}" />',
			<fmt:message key="label.monitoring.summary.completion.started" var="ACTIVITY_COMPLETION_CHART_STARTED_LEARNERS_VAR"/>
			ACTIVITY_COMPLETION_CHART_STARTED_LEARNERS : '<c:out value="${ACTIVITY_COMPLETION_CHART_STARTED_LEARNERS_VAR}" />',	
			<fmt:message key="label.monitoring.summary.completion.completed" var="ACTIVITY_COMPLETION_CHART_COMPLETED_LEARNERS_VAR"/>
			ACTIVITY_COMPLETION_CHART_COMPLETED_LEARNERS : '<c:out value="${ACTIVITY_COMPLETION_CHART_COMPLETED_LEARNERS_VAR}" />',	
			<fmt:message key="label.monitoring.summary.answered.questions" var="ANSWERED_QUESTIONS_CHART_TITLE_VAR"/>
			ANSWERED_QUESTIONS_CHART_TITLE : '<c:out value="${ANSWERED_QUESTIONS_CHART_TITLE_VAR}" />',
			<fmt:message key="label.monitoring.summary.answered.questions.groups" var="ANSWERED_QUESTIONS_CHART_TITLE_GROUPS_VAR"/>
			ANSWERED_QUESTIONS_CHART_TITLE_GROUPS : '<c:out value="${ANSWERED_QUESTIONS_CHART_TITLE_GROUPS_VAR}" />',
			<fmt:message key="label.monitoring.summary.answered.questions.x.axis" var="ANSWERED_QUESTIONS_CHART_X_AXIS_VAR"/>
			ANSWERED_QUESTIONS_CHART_X_AXIS : '<c:out value="${ANSWERED_QUESTIONS_CHART_X_AXIS_VAR}" />',
			<fmt:message key="label.monitoring.summary.answered.questions.y.axis.students" var="ANSWERED_QUESTIONS_CHART_Y_AXIS_STUDENTS_VAR"/>
			ANSWERED_QUESTIONS_CHART_Y_AXIS_STUDENTS : '<c:out value="${ANSWERED_QUESTIONS_CHART_Y_AXIS_STUDENTS_VAR}" />',
			<fmt:message key="label.monitoring.summary.answered.questions.y.axis.groups" var="ANSWERED_QUESTIONS_CHART_Y_AXIS_GROUPS_VAR"/>
			ANSWERED_QUESTIONS_CHART_Y_AXIS_GROUPS : '<c:out value="${ANSWERED_QUESTIONS_CHART_Y_AXIS_GROUPS_VAR}" />'
		}),
		
		activityCompletionChart = null,
		answeredQuestionsChart = null,
		// do not refresh charts automatically
		// they will be redrawn on page auto reload
		COMPLETION_CHART_UPDATE_INTERVAL = 0;
	
	$(document).ready(function(){
		drawCompletionCharts(${toolContentID}, ${groupsInAnsweredQuestionsChart}, false);
	});
			
	function exportExcel(){
		//dynamically create a form and submit it
		var exportExcelUrl = "<lams:LAMSURL/>tool/laasse10/monitoring/exportSummary.do?toolContentID=${toolContentID}&downloadTokenValue=dummy&fileName=assessment_export.xlsx&reqID=" + (new Date()).getTime();
		var form = $('<form method="post" action="' + exportExcelUrl + '"></form>');
	    var hiddenInput = $('<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"></input>');
	    form.append(hiddenInput);
	    $(document.body).append(form);
	    form.submit();
	};
</script>

<!-- Header -->
<div class="row no-gutter">
	<div class="col-xs-12 col-md-12 col-lg-8">
		<h3>
			<fmt:message key="label.ira.questions.marks"/>
		</h3>
	</div>
</div>
<!-- End header -->

<!-- Notifications -->  
<div class="row no-gutter">
	<div class="col-md-6 col-lg-4 ">
	</div>
	
	<div class="col-xs-12 col-md-6 col-lg-4 col-lg-offset-2">
		<a href="#nogo" type="button" class="btn btn-sm btn-default buttons_column"
				onclick="javascript:loadTab('iraAssessment'); return false;">
			<i class="fa fa-undo"></i>
			<fmt:message key="label.hide.students.choices"/>
		</a>
		<a href="#nogo" onclick="javascript:printTable(); return false;" type="button" class="btn btn-sm btn-default buttons_column">
			<i class="fa fa-print"></i>
			<fmt:message key="label.print"/>
		</a>
		<a href="#nogo" onclick="javascript:exportExcel(); return false;" type="button" class="btn btn-sm btn-default buttons_column">
			<i class="fa fa-file"></i>
			<fmt:message key="label.excel.export"/>
		</a>
	</div>
</div>
<br>
<!-- End notifications -->

<div id="completion-charts-container">
	<div class="col-sm-12 col-md-6">
		<canvas id="activity-completion-chart"></canvas>
	</div>
	
	<div class="col-sm-12 col-md-6">
		<canvas id="answered-questions-chart"></canvas>
	</div>
</div>

<%-- Include student's choices part --%>
<%@ include file="/pages/monitoring/parts/mcqStudentChoices.jsp" %>

<%@ include file="/pages/monitoring/parts/timeLimit.jsp"%>