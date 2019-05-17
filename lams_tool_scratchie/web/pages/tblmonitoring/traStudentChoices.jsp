<%@ include file="/common/taglibs.jsp"%>

<script>
	function exportExcel(){
		location.href = "<lams:LAMSURL/>tool/lascrt11/tblmonitoring/exportExcel.do?toolContentID=${toolContentID}&reqID=" + (new Date()).getTime();
	};
</script>

<!-- Header -->
<div class="row no-gutter">
	<div class="col-xs-12 col-md-12 col-lg-8">
		<h3>
			<fmt:message key="label.tra.questions.marks"/>
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
				onclick="javascript:loadTab('tra'); return false;">
			<i class="fa fa-undo"></i>
			<fmt:message key="label.hide.students.choices"/>
		</a>
		<a href="#nogo" onclick="javascript:printTable(); return false;" type="button" class="btn btn-sm btn-default buttons_column">
			<i class="fa fa-print"></i>
			<fmt:message key="label.print"/>
		</a>
		<a href="#nogo" onclick="javascript:exportExcel(); return false;" type="button" class="btn btn-sm btn-default buttons_column">
			<i class="fa fa-file"></i>
			<fmt:message key="label.export.excel"/>
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
          
	<table id="questions-data" class="table table-responsive table-striped table-bordered table-hover table-condensed">
		<thead>
			<tr role="row">
			
				<th></th>
				<c:forEach var="item" items="${items}" varStatus="i">
					<th class="text-center">
						<a data-toggle="modal" href="#question${i.index}Modal">
							<fmt:message key="label.authoring.basic.question.text"/> ${i.index + 1}
						</a>
					</th>
				</c:forEach>
				<th class="text-center">
					<fmt:message key="label.total"/>
				</th>
				<th class="text-center">
					<fmt:message key="label.total"/> %
				</th>
				
			</tr>
		</thead>
		<tbody>
		
			<tr>
				<td>
					<b>
						<fmt:message key="label.correct.answer"/>
					</b>
				</td>
				<c:forEach begin="1" end="${fn:length(correctOptions) - 1}" var="i">
					<td class="text-center">
						${correctOptions[i].cellValue}
					</td>
				</c:forEach>
				<td class="text-center"></td>
				<td class="text-center"></td>
			</tr>
			
			<tr>
				<td colspan="7" style="font-weight: bold;">
					<fmt:message key="label.teams.notuppercase"/>
				</td> 
			</tr>
			
			<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="i">
				<tr>
					<td class="text-center">
						${sessionDto.sessionName}
					</td>
					
					<c:choose>
						<c:when test="${empty sessionDto.itemDtos}">
							<c:forEach begin="1" end="${fn:length(items) + 2}">
								<td></td>
							</c:forEach>
						</c:when>
						
						<c:otherwise>
							<c:forEach var="itemDto" items="${sessionDto.itemDtos}">
								<td class="text-center">
									<c:forEach var="optionDto" items="${itemDto.optionDtos}">
										<c:if test="${optionDto.qbOption.name != ''}">
											<span class="user-response <c:if test="${optionDto.qbOption.correct}">successful-response</c:if> <c:if test="${!optionDto.qbOption.correct}">wrong-response</c:if>">
												<c:out value="${optionDto.qbOption.name}"></c:out>
											</span>
										</c:if>
									</c:forEach>
								</td>
							</c:forEach>
							
							<td class="text-center">
								${sessionDto.mark}
							</td>
							
							<td class="text-center">
								${sessionDto.totalPercentage}
							</td>
						</c:otherwise>
					</c:choose>
					
				</tr>
			</c:forEach>                                               
		</tbody>
	</table>
</div>
</div>
</div>          
</div>

<!-- Question detail modal -->
<c:forEach var="item" items="${items}" varStatus="i">
	<div class="modal fade" id="question${i.index}Modal">
	<div class="modal-dialog">
	<div class="modal-content">
	<div class="modal-body">
	
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title"><span class="float-left space-right">Q${i.index+1})</span> ${item.qbQuestion.name}</h4>
			</div>
			<div class="panel-body">
			
				<div>
					${item.qbQuestion.description}
				</div>
			
				<div class="table-responsive voffset10">
					<table class="table table-striped table-hover">
						<tbody>
						
							<c:forEach var="optionDto" items="${item.optionDtos}" varStatus="j">
								<c:set var="cssClass"><c:if test='${optionDto.qbOption.correct}'>bg-success</c:if></c:set>
								<tr>
									<td width="5px" class="${cssClass}">
										${ALPHABET[j.index]}.
									</td>
									<td class="${cssClass}">
										<c:out value="${optionDto.qbOption.name}" escapeXml="false"/>
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
				<fmt:message key="button.close"/>
			</a>
		</div>
	
	</div>
	</div>
	</div>
	</div>
</c:forEach>
<!-- End question detail modal -->
