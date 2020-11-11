<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>

<style>
	#completion-charts-container > div {
		padding: 5rem 0;
	}
</style>

<script>
	var activityCompletionChart = null,
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