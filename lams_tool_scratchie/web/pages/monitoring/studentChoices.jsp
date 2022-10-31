<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>

<c:set var="timeLimitPanelUrl"><lams:LAMSURL/>monitoring/timeLimit.jsp</c:set>
<c:url var="timeLimitPanelUrl" value="${timeLimitPanelUrl}">
	<c:param name="toolContentId" value="${scratchie.contentId}"/>
	<c:param name="absoluteTimeLimit" value="${scratchie.absoluteTimeLimitSeconds}"/>
	<c:param name="relativeTimeLimit" value="${scratchie.relativeTimeLimit}"/>
	<c:param name="isTbl" value="${isTbl}" />
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
	  
	.burning-questions-badge {
		margin-left: 5px;
		font-size: 11px;
		background-color: #aaa;
	}
</style>

<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.plugin.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.countdown.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/common.js"></script>
<script>
	$(document).ready(function(){
		$('#time-limit-panel-placeholder').load('${timeLimitPanelUrl}');
	});
</script>

<c:if test="${not showStudentChoicesTableOnly}">
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
		<div class="col-md-5 col-lg-4 ">
		</div>
		
		<div class="col-xs-12 col-md-7 col-lg-5 col-lg-offset-3">
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
			<c:if test="${vsaPresent}">
				<a class="btn btn-sm btn-default buttons_column" target="_blank"
				   href='<lams:LAMSURL />qb/vsa/displayVsaAllocate.do?toolContentID=${scratchie.contentId}'>
					<fmt:message key="label.vsa.allocate.button" />
				</a>
			</c:if>
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
<div id="questions-data-container" class="table-responsive">
	<table id="questions-data" class="table table-striped table-bordered table-hover table-condensed">
		<thead>
			<tr role="row">
			
				<th></th>
				<c:forEach var="item" items="${items}" varStatus="i">
					<th class="text-center">
						<a data-toggle="modal" href="#question${i.index}Modal">
							<fmt:message key="label.authoring.basic.question.text"/> ${i.index + 1}
						</a>
						<c:if test="${not empty burningQuestionItemDtos}">
							<c:forEach var="burningQuestionDto" items="${burningQuestionItemDtos}">
								<c:if test="${burningQuestionDto.scratchieItem.uid eq item.uid and not empty burningQuestionDto.burningQuestionDtos}">
									<a href="#accordionBurning" class="badge burning-questions-badge" title='<fmt:message key="label.burning.questions" />'>
										<c:out value="${fn:length(burningQuestionDto.burningQuestionDtos)}" />
									</a>
								</c:if>
							</c:forEach>
						</c:if>
					</th>
				</c:forEach>
				
				<c:if test="${not empty sessionDtos}">
					<th class="text-center">
						<fmt:message key="label.total"/>&nbsp;
						<i class="fa fa-question-circle text-primary" data-toggle="tooltip" data-placement="top" data-container="body" 
						   title="<fmt:message key="label.total.1st.attempt.by.team"/>"></i>
					</th>
					<th class="text-center">
						<fmt:message key="label.total"/> %
					</th>
				</c:if>
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
				
				<c:if test="${not empty sessionDtos}">
					<td class="text-center"></td>
					<td class="text-center"></td>
				</c:if>
			</tr>
			
			<c:if test="${not empty sessionDtos and (not showStudentChoicesTableOnly or sessionMap.isGroupedActivity)}">
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
							<c:choose>
								<c:when test="${empty sessionDto.leaderUid}">
									${sessionDto.sessionName}
								</c:when>
								<c:otherwise>
									<c:url var="userSummaryUrl" value='/learning/start.do'>
										<c:param name="userID" value="${sessionDto.leaderUid}" />
										<c:param name="toolSessionID" value="${sessionDto.sessionId}" />
										<c:param name="mode" value="teacher" />
									</c:url>
									<a href="#" onClick="javascript:launchPopup('${userSummaryUrl}', 'MonitoringReview')">
										${sessionDto.sessionName}
										<c:if test="${sessionDto.scratchingFinished}">
											<i class="fa fa-check text-success loffset5" title="<fmt:message key='label.finish'/>"></i>
										</c:if>
									</a>
									
								</c:otherwise>
							</c:choose>
							
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
									<c:forEach var="optionDto" items="${itemDto.optionDtos}">
										<c:if test="${optionDto.answer != ''}">
											<span class="user-response <c:if test="${optionDto.correct}">successful-response</c:if> <c:if test="${!optionDto.correct}">wrong-response</c:if>">
												<c:choose>
													<c:when test="${itemDto.type == 1 or itemDto.type == 8}">
														<c:out value="${optionDto.answer}" escapeXml="false"/>
													</c:when>
													<c:when test="${optionDto.correct}"><i class="fa fa-check"></i></c:when>
													<c:otherwise><i class="fa fa-close"></i></c:otherwise>
												</c:choose>
												
											</span>
										</c:if>
									</c:forEach>
								</td>
							</c:forEach>
							
							<c:set var="highlightClass">
								<c:choose>
									<c:when test="${sessionDto.totalPercentage > 95}">bg-success</c:when>
									<c:when test="${sessionDto.totalPercentage < 40}">bg-danger</c:when>
									<c:when test="${sessionDto.totalPercentage < 75}">bg-warning</c:when>
								</c:choose>
							</c:set>
							
							<td class="text-center ${highlightClass}">
								${sessionDto.mark}
							</td>
							
							<td class="text-center ${highlightClass}">
								<fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="2" value="${sessionDto.totalPercentage}" /> %
							</td>
						</c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>      
			
			<c:if test="${not empty sessionDtos}">
				<c:set var="totalSum" value="0" />
				<c:set var="totalPercentSum" value="0" />
				<tr>
					<th><fmt:message key="label.total"/>&nbsp;
					<i class="fa fa-question-circle text-primary" data-toggle="tooltip" data-placement="top" data-container="body" 
					   title="<fmt:message key="label.total.1st.attempt.by.question"/>"></i></th>
					   
					<c:forEach var="item" items="${items}">
						<c:set var="highlightClass">
							<c:choose>
								<c:when test="${item.correctOnFirstAttemptPercent > 95}">bg-success</c:when>
								<c:when test="${item.correctOnFirstAttemptPercent < 40}">bg-danger</c:when>
								<c:when test="${item.correctOnFirstAttemptPercent < 75}">bg-warning</c:when>
							</c:choose>
						</c:set>
						
						<td class="text-center ${highlightClass}">
							${item.correctOnFirstAttemptCount}
						</td>
						<c:set var="totalSum" value="${totalSum + item.correctOnFirstAttemptCount}" />
						<c:set var="totalPercentSum" value="${totalPercentSum + item.correctOnFirstAttemptPercent}" />
					</c:forEach>
					
					<c:set var="totalPercentAverage" value="${totalPercentSum / fn:length(items)}" />
					<c:set var="totalAverageHighlightClass">
						<c:choose>
							<c:when test="${totalPercentAverage > 95}">bg-success</c:when>
							<c:when test="${totalPercentAverage < 40}">bg-danger</c:when>
							<c:when test="${totalPercentAverage < 75}">bg-warning</c:when>
						</c:choose>
					</c:set>
					<td class="text-center ${totalAverageHighlightClass}">
						<fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="2" value="${totalSum / fn:length(items)}" />&nbsp;
						<i class="fa fa-question-circle text-primary" data-toggle="tooltip" data-placement="top" data-container="body" 
						    title="<fmt:message key="label.total.1st.attempt.average"/>"></i>
					</td>
					<td class="text-center">-</td>
				</tr>
				<tr>
					<th><fmt:message key="label.total"/> %</th>
					<c:forEach var="item" items="${items}">
						<c:set var="highlightClass">
							<c:choose>
								<c:when test="${item.correctOnFirstAttemptPercent > 95}">bg-success</c:when>
								<c:when test="${item.correctOnFirstAttemptPercent < 40}">bg-danger</c:when>
								<c:when test="${item.correctOnFirstAttemptPercent < 75}">bg-warning</c:when>
							</c:choose>
						</c:set>
						
						<td class="text-center ${highlightClass}">
							<fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="2" value="${item.correctOnFirstAttemptPercent}" /> %
						</td>
					</c:forEach>
					<td class="text-center">-</td>
					<td class="text-center ${totalAverageHighlightClass}">					
						<fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="2" value="${totalPercentAverage}" />&nbsp;%&nbsp;
						<i class="fa fa-question-circle text-primary" data-toggle="tooltip" data-placement="top" data-container="body" 
						    title="<fmt:message key="label.total.1st.attempt.average"/>"></i>
					</td>
				</tr>                         
			</c:if>                     
		</tbody>
	</table>
	</div>
</div>
</div>
</div>          
</div>

<!-- Question detail modal -->
<c:forEach var="item" items="${items}" varStatus="i">
	<div class="modal fade question-detail-modal" id="question${i.index}Modal">
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

<c:if test="${isTbl}">
	<div id="time-limit-panel-placeholder"></div>
</c:if>