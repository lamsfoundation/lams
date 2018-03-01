<%@ include file="/common/taglibs.jsp"%>
<c:set var="method">
	<c:choose>
		<c:when test="${param.isIraAssessment}">
			iraAssessment
		</c:when>
		<c:otherwise>
			aes
		</c:otherwise>
	</c:choose>
</c:set>

<script>
	function exportExcel(){
		location.href = "<lams:LAMSURL/>tool/laasse10/monitoring/exportSummary.do?toolContentID=${toolContentID}&downloadTokenValue=dummy&fileName=assessment_export.xlsx&reqID=" + (new Date()).getTime();
	};
</script>

<!-- Header -->
<div class="row no-gutter">
	<div class="col-xs-12 col-md-12 col-lg-8">
		<h3>
			<c:choose>
				<c:when test="${param.isIraAssessment}">
					<fmt:message key="label.ira.questions.marks"/>
				</c:when>
				<c:otherwise>
					<fmt:message key="label.ae.questions.marks"/>
				</c:otherwise>
			</c:choose>
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
				onclick="javascript:loadTab('${method}', ${toolContentID}); return false;">
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
<div class="col-xs-12 col-md-12 col-lg-10">
<div class="panel">
<div class="panel-body table-responsive">
          
	<table  id="questions-data" class="table table-responsive table-striped table-bordered table-hover table-condensed">
		<thead>
			<tr role="row">
				<th></th>
				<c:forEach var="questionDto" items="${questionDtos}" varStatus="i">
					<th class="text-center">
						<fmt:message key="label.authoring.basic.list.header.question"/> ${i.index + 1}
					</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
		
			<tr role="row">
				<th><b>Question type</b></th>
				<c:forEach var="questionDto" items="${questionDtos}" varStatus="i">
					<td class="text-center">
						${questionDto.questionTypeLabel}
					</td>
				</c:forEach>
			</tr>
		
			<tr>
				<td><b>Correct answer</b></td>
				<c:forEach var="questionDto" items="${questionDtos}" varStatus="i">
					<td class="text-center">
						${questionDto.correctAnswer}
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
					
					<c:forEach var="questionDto" items="${questionDtos}" varStatus="j">
						<c:set var="questionResultDto" value="${questionDto.sessionQuestionResults[i.index]}"/>
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

