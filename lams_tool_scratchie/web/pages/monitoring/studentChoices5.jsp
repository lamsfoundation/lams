<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>

<c:set var="timeLimitPanelUrl"><lams:LAMSURL/>monitoring/timeLimit5.jsp</c:set>
<c:url var="timeLimitPanelUrl" value="${timeLimitPanelUrl}">
	<c:param name="toolContentId" value="${scratchie.contentId}"/>
	<c:param name="absoluteTimeLimit" value="${scratchie.absoluteTimeLimitSeconds}"/>
	<c:param name="relativeTimeLimit" value="${scratchie.relativeTimeLimit}"/>
	<c:param name="isTbl" value="true" />
	<c:param name="controllerContext" value="tool/lascrt11/monitoring" />
</c:url>

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
	
	span.user-response .fa, .question-detail-modal .fa {
	  cursor: default;
	}
	
	span.successful-response {
	  background-color: #5cb85c; }
	
	span.wrong-response {
	  background-color: #d9534f; }
</style>


<script>
	// tell TBL monitor that this element is scrollable horizontally
	tlbMonitorHorizontalScrollElement = '#questions-data-container';
	
	function exportExcel(){
		//dynamically create a form and submit it
		var exportExcelUrl = "<lams:LAMSURL/>tool/lascrt11/tblmonitoring/exportExcel.do?toolContentID=${toolContentID}&reqID=" + (new Date()).getTime();
		var form = $('<form method="post" action="' + exportExcelUrl + '"></form>');
	    var hiddenInput = $('<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"></input>');
	    form.append(hiddenInput);
	    $(document.body).append(form);
	    form.submit();
	};

	$(document).ready(function(){
		openEventSource('<lams:WebAppURL />tblmonitoring/traStudentChoicesFlux.do?toolContentId=${toolContentID}', function(event) {
			$('#student-choices-table').load('<lams:WebAppURL />tblmonitoring/traStudentChoicesTable.do?toolContentID=${toolContentID}');
		});
		
		$('#time-limit-panel-placeholder').load('${timeLimitPanelUrl}');
	});
</script>

<div class="container-fluid">
	 <div class="row">
		<div class="col-8 offset-2 text-center">
			<h3>
				<fmt:message key="label.tra.questions.marks"/>
			</h3>
		</div>
	</div>
	
	<!-- Notifications -->  
	<div class="row mb-4">
		<div class="col-10 offset-1 text-end">
			<a href="#nogo" type="button" class="btn btn-secondary buttons_column"
					onclick="javascript:loadTab('trat', $('#load-trat-tab-btn'))">
				<i class="fa fa-undo"></i>
				<fmt:message key="label.hide.students.choices"/>
			</a>
			<a href="#nogo" onclick="javascript:printTable(); return false;" type="button" class="btn btn-secondary buttons_column">
				<i class="fa fa-print"></i>
				<fmt:message key="label.print"/>
			</a>
			<a href="#nogo" onclick="javascript:exportExcel(); return false;" type="button" class="btn btn-secondary buttons_column">
				<i class="fa fa-file"></i>
				<fmt:message key="label.export.excel"/>
			</a>
			<c:if test="${vsaPresent}">
				<a class="btn btn-secondary buttons_column" target="_blank"
				   href='<lams:LAMSURL />qb/vsa/displayVsaAllocate.do?toolContentID=${scratchie.contentId}'>
					<fmt:message key="label.vsa.allocate.button" />
				</a>
			</c:if>
		</div>
	</div>
	<!-- End notifications -->
	
	<div class="row">
		<div class="col-10 offset-1">
			<div class="card">
				<div id="questions-data-container" class="table-responsive card-body">
					<table id="questions-data" class="table table-bordered table-hover table-condensed">
						<thead>
							<tr role="row" class="border-top-0">
								<th></th>
								<c:forEach var="item" items="${items}" varStatus="i">
									<th class="text-center">
										<a data-bs-toggle="modal" data-bs-target="#question${i.index}Modal" href="#">
											<fmt:message key="label.authoring.basic.question.text"/> ${i.index + 1}
										</a>
									</th>
								</c:forEach>
								
								<th class="text-center">
									<fmt:message key="label.total"/>&nbsp;
									<lams:Popover>
										<fmt:message key="label.total.1st.attempt.by.team"/>
									</lams:Popover>
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
								<c:forEach var="item" items="${items}">
									<td class="text-center">
										<c:if test="${item.qbQuestion.type == 1 or item.qbQuestion.type == 8}">
											${item.correctAnswerLetter}
										</c:if>
									</td>
								</c:forEach>
								
								<td class="text-center"></td>
								<td class="text-center"></td>
							</tr>
						</tbody>
						<tbody id="student-choices-table">
						</tbody>
					</table>
				</div>
			</div>
		</div>          
	</div>
		
	<!-- Question detail modal -->
	<c:forEach var="item" items="${items}" varStatus="i">
		<div class="modal fade" id="question${i.index}Modal">
		<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header align-items-start">
				<div class="modal-title">
					<span>Q${i.index+1})</span>
					<c:if test="${not empty item.qbQuestion.name}">
						<p><c:out value="${item.qbQuestion.name}"  escapeXml="false" /></p>
					</c:if>
					${item.qbQuestion.description}
				</div>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			<div class="modal-body">
				<div class="table-responsive voffset10">
					<table class="table table-striped table-hover">
						<tbody>
							<c:forEach var="qbOption" items="${item.qbQuestion.qbOptions}" varStatus="j">
								<c:set var="cssClass"><c:if test='${qbOption.correct}'>bg-success</c:if></c:set>
									<tr>
										<c:choose>
											<c:when test="${item.qbQuestion.type == 1 or item.qbQuestion.type == 8}">
												<td width="5px" class="${cssClass}">
													${ALPHABET[j.index]}.
												</td>
												<td class="${cssClass}">
													<c:out value="${qbOption.name}" escapeXml="false"/>
												</td>
											</c:when>
											<c:otherwise>
												<td width="5px" class="${cssClass}">
													<c:choose>
														<c:when test="${qbOption.correct}">
															<i class="fa fa-check"></i>
														</c:when>
														<c:otherwise>
															<i class="fa fa-close"></i>
														</c:otherwise>
													</c:choose>
												</td>
												<td class="${cssClass}">
													${fn:replace(qbOption.name, newLineChar, ', ')}
												</td>
											</c:otherwise>
										</c:choose>
									</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div> 
			<div class="modal-footer">	
				<a href="#" data-bs-dismiss="modal" class="btn btn-secondary">
					<fmt:message key="button.close"/>
				</a>
			</div>
		</div>
		</div>
		</div>
	</c:forEach>
	<!-- End question detail modal -->
	
	<div class="row">
		<div class="col-10 offset-1" id="time-limit-panel-placeholder">
		</div>
	</div>
</div>