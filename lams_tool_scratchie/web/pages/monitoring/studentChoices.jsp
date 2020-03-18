<%@ include file="/common/taglibs.jsp"%>
<style>
	/* show horizontal scroller for iPads */
	body {
	    -webkit-overflow-scrolling: touch;
	}

	/*----  fixed first column ----*/
	#questions-data {
	  position: relative;
	}
	
	#questions-data thead th {
	  position: -webkit-sticky; /* for Safari */
	  position: sticky;
	  top: 0;
	  background: #FFF;
	}
	
	#questions-data thead th:first-child {
	  left: 0;
	  z-index: 1;
	}
	
	#questions-data tbody th {
	  position: -webkit-sticky; /* for Safari */
	  position: sticky;
	  left: 0;
	  background: #FFF;
	}
	
	span.user-response {
	  padding: 1px 3px;
	  color: white;
	  border-radius: 3px; }
	
	span.successful-response {
	  background-color: #5cb85c; }
	
	span.wrong-response {
	  background-color: #d9534f; }
</style>

<c:if test="${not showStudentChoicesTableOnly}">
	<script>
		function exportExcel(){
			//dynamically create a form and submit it
			var exportExcelUrl = "<lams:LAMSURL/>tool/lascrt11/tblmonitoring/exportExcel.do?toolContentID=${toolContentID}&reqID=" + (new Date()).getTime();
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
</c:if>

<!-- Table --> 
<div class="row no-gutter">
<div class="col-xs-12 col-md-12 col-lg-12">
<div class="panel">
<div class="panel-body">
<div class="table-responsive">
	<table id="questions-data" class="table table-striped table-bordered table-hover table-condensed">
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
				<th>
					<b>
						<fmt:message key="label.correct.answer"/>
					</b>
				</th>
				<c:forEach var="correctAnswerCell"  items="${correctAnswers.cells}" varStatus="status">
					<c:if test="${!status.first}">
						<td class="text-center">
							${correctAnswerCell.cellValue}
						</td>
					</c:if>
				</c:forEach>
				<td class="text-center"></td>
				<td class="text-center"></td>
			</tr>
			
			<c:if test="${not showStudentChoicesTableOnly or sessionMap.isGroupedActivity}">
				<tr>
					<th colspan="0" style="font-weight: bold;">
						<fmt:message key="label.teams.notuppercase"/>
					</th> 
				</tr>
			</c:if>
			
			<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="i">
				<tr>
					<th class="text-center">
						<c:if test="${not showStudentChoicesTableOnly or sessionMap.isGroupedActivity}">
							${sessionDto.sessionName}
						</c:if>
					</th>
					
					<c:choose>
						<c:when test="${empty sessionDto.itemDtos}">
							<c:forEach begin="1" end="${fn:length(items) + 2}">
								<td></td>
							</c:forEach>
						</c:when>
						
						<c:otherwise>
							<c:forEach var="itemDto" items="${sessionDto.itemDtos}">
								<td class="text-center">
									<c:forEach var="answer" items="${itemDto.answers}">
										<c:if test="${answer.description != ''}">
											<span class="user-response <c:if test="${answer.correct}">successful-response</c:if> <c:if test="${!answer.correct}">wrong-response</c:if>">
												<c:out value="${answer.description}"></c:out>
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
</div>

<!-- Question detail modal -->
<c:forEach var="item" items="${items}" varStatus="i">
	<div class="modal fade" id="question${i.index}Modal">
	<div class="modal-dialog">
	<div class="modal-content">
	<div class="modal-body">
	
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title"><span class="float-left space-right">Q${i.index+1})</span> ${item.title}</h4>
			</div>
			<div class="panel-body">
			
				<div>
					${item.description}
				</div>
			
				<div class="table-responsive voffset10">
					<table class="table table-striped table-hover">
						<tbody>
						
							<c:forEach var="answer" items="${item.answers}" varStatus="j">
								<c:set var="cssClass"><c:if test='${answer.correct}'>bg-success</c:if></c:set>
								<tr>
									<td width="5px" class="${cssClass}">
										${ALPHABET[j.index]}.
									</td>
									<td class="${cssClass}">
										<c:out value="${answer.description}" escapeXml="false"/>
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
