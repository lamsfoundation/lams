<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>
<style>
	.question-title {
		white-space: nowrap;
    	overflow: scroll;
    	max-width: 30ch;
	} 
</style>
<script>
	function exportExcel(){
		location.href = "<lams:LAMSURL/>tool/laasse10/monitoring/exportSummary.do?toolContentID=${toolContentID}&downloadTokenValue=dummy&fileName=assessment_export.xlsx&reqID=" + (new Date()).getTime();
	};
</script>

<!-- Header -->
<div class="row no-gutter">
	<div class="col-xs-12 col-md-12 col-lg-8">
		<h3>
			<fmt:message key="label.ae.questions.marks"/>
		</h3>
	</div>
</div>
<!-- End header -->

<!-- Notifications -->
<div class="row">
	<div class="col-md-6 col-lg-4 ">
	</div>
	
	<div class="col-xs-12 col-md-6 col-lg-4 col-lg-offset-2">
		<a href="#nogo" type="button" class="btn btn-sm btn-default buttons_column"
				onclick="javascript:loadTab('aes', ${toolContentID}); return false;">
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
						${fn:replace(tblQuestionDto.correctAnswer, newLineChar, ', ')}
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

