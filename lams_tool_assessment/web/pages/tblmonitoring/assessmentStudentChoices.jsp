<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>

<c:set var="timeLimitPanelUrl"><lams:LAMSURL/>monitoring/timeLimit.jsp</c:set>
<c:url var="timeLimitPanelUrl" value="${timeLimitPanelUrl}">
	<c:param name="toolContentId" value="${assessment.contentId}"/>
	<c:param name="absoluteTimeLimit" value="${assessment.absoluteTimeLimitSeconds}"/>
	<c:param name="relativeTimeLimit" value="${assessment.relativeTimeLimit}"/>
	<c:param name="isTbl" value="true" />
	<c:param name="controllerContext" value="tool/laasse10/monitoring" />
</c:url>

<style>
	.question-title {
    	overflow: auto;
    	min-width: 150px;
	} 
	
	#completion-charts-container > div {
		padding: 5rem 0;
	}
</style>

<script src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
<script src="<lams:LAMSURL/>includes/javascript/jquery.plugin.js"></script>
<script src="<lams:LAMSURL/>includes/javascript/jquery.countdown.js"></script>
<lams:JSImport src="includes/javascript/chart.js" relative="true" />
	
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
	COMPLETION_CHART_UPDATE_INTERVAL = 10 * 1000;
	
	$(document).ready(function(){
		drawCompletionCharts(${toolContentID}, ${groupsInAnsweredQuestionsChart}, true);

		$('#time-limit-panel-placeholder').load('${timeLimitPanelUrl}');
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


<!-- Notifications -->
<div class="pull-right">
	<a href="#nogo" type="button" class="btn btn-sm btn-default buttons_column"
			onclick="javascript:loadAePane(${toolContentID}, 'default'); return false;">
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

<!-- Table --> 
<div class="row no-gutter">
<div class="col-xs-12 col-md-12 col-lg-12">
<div class="panel">
<div class="panel-body table-responsive">
          
	<table  id="questions-data" class="table table-responsive table-striped table-bordered table-hover table-condensed">
		<thead>
			<tr role="row">
				<th></th>
				<c:forEach var="tblQuestionDto" items="${questionDtos}" varStatus="i">
					<th class="text-center">
						<div class="question-title">
							<c:if test="${assessment.numbered}">
									${i.count}.
							</c:if>
							${tblQuestionDto.title}
						</div>
					</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
		
			<tr role="row">
				<th><b>Question type</b></th>
				<c:forEach var="tblQuestionDto" items="${questionDtos}" varStatus="i">
					<td class="text-center">
						${tblQuestionDto.questionTypeLabel}
					</td>
				</c:forEach>
			</tr>
		
			<tr>
				<td><b>Correct answer</b></td>
				<c:forEach var="tblQuestionDto" items="${questionDtos}" varStatus="i">
					<td class="text-center">
						<c:out value="${tblQuestionDto.correctAnswer}" escapeXml="false" />
 					</td>
				</c:forEach>
			</tr>
			
			<tr>
				<td colspan="${fn:length(questionDtos) + 1}" style="font-weight: bold;"><fmt:message key="label.teams"/></td> 
			</tr>
			
			<c:forEach var="session" items="${sessions}" varStatus="i">
				<tr>
					<td class="text-center">
						${session.sessionName}
					</td>
					
					<c:forEach var="tblQuestionDto" items="${questionDtos}" varStatus="j">
						<c:set var="questionResultDto" value="${tblQuestionDto.sessionQuestionResults[i.index]}"/>
						<td class="text-center <c:if test="${questionResultDto.correct}">success</c:if>" >
							${questionResultDto.answer}
						</td>
					</c:forEach>
					
				</tr>
			</c:forEach>                                               
		</tbody>
	</table>
</div>
</div>
</div>          
</div>

<div id="time-limit-panel-placeholder"></div>