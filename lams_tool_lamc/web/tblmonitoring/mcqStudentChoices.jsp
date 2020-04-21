<%@ include file="/common/taglibs.jsp"%>
<script>
	function exportExcel(){
		//dynamically create a form and submit it
		var exportExcelUrl = "<lams:LAMSURL/>tool/lamc11/monitoring/downloadMarks.do?downloadTokenValue=jjj&toolContentID=${toolContentID}&reqID=" + (new Date()).getTime();
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
				onclick="javascript:loadTab('iraMcq'); return false;">
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
<div class="panel-body">
<div class="table-responsive" style="margin:0">
	<table id="questions-data" class="table table-striped table-bordered table-hover table-condensed">
		<thead>
			<tr role="row">
				<th class="text-center">
					<fmt:message key="label.question"/>
				</th>
				<c:forEach begin="0" end="${maxOptionsInQuestion - 1}" var="i">
					<th class="text-center">
						${ALPHABET_CAPITAL_LETTERS[i]}
					</th>
				</c:forEach>
			</tr>
		</thead>
		
		<tbody>
			<c:forEach var="questionDto" items="${questionDtos}" varStatus="i">
				<tr>
					<td class="normal">
						<a data-toggle="modal" href="#question${i.index}Modal">
							${i.index+1}
						</a>
					</td>
					
					<c:forEach var="optionDto" items="${questionDto.optionDtos}">
						<td class="normal <c:if test='${optionDto.correct == "Correct"}'>success</c:if>">
							<fmt:formatNumber type="number" maxFractionDigits="2" value="${optionDto.percentage}"/>%
						</td>
					</c:forEach>
					
					<c:forEach begin="1" end="${maxOptionsInQuestion - fn:length(questionDto.optionDtos)}" var="j">
						<td class="normal"></td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
</div>
</div>          
</div>
</div>
<!-- End table -->

<!-- Question detail modal -->
<c:forEach var="questionDto" items="${questionDtos}" varStatus="i">
	<div class="modal fade" id="question${i.index}Modal">
	<div class="modal-dialog">
	<div class="modal-content">
	<div class="modal-body">
	
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<span class="float-left space-right">Q${i.index+1})</span> 
					${questionDto.name}
					<br>
					${questionDto.description}
				</h4>
			</div>
			<div class="panel-body">
				<div class="table-responsive">
					<table class="table table-striped table-hover">
						<tbody>
						
							<c:forEach var="optionDto" items="${questionDto.optionDtos}" varStatus="j">
								<c:set var="cssClass"><c:if test='${optionDto.correct == "Correct"}'>bg-success</c:if></c:set>
								<tr>
									<td width="5px" class="${cssClass}">
										${ALPHABET[j.index]}.
									</td>
									<td class="${cssClass}">
										<c:out value="${optionDto.candidateAnswer}" escapeXml="false"/>
									</td>
									<td class="${cssClass}">
										<fmt:formatNumber type="number" maxFractionDigits="2" value="${optionDto.percentage}"/>%
									</td>
								</tr>
							</c:forEach>
							
						</tbody>
					</table>
				</div>
			</div> 
		</div>
	            
		<div class="modal-footer">	
			<a href="#" data-dismiss="modal" class="btn btn-default">
				<fmt:message key="label.close"/>
			</a>
		</div>
	
	</div>
	</div>
	</div>
	</div>
</c:forEach>
<!-- End question detail modal -->
