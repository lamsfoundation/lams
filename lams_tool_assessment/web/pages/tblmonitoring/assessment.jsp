<%@ include file="/common/taglibs.jsp"%>

<script>
	$(document).ready(function(){
		// change attempted and all learners numbers
		$('.nav-tabs').bind('click', function (event) {
			
			var link = $(event.target);
			var attemptedLearnersNumber = link.data("attempted-learners-number");
			$("#attempted-learners-number").html(attemptedLearnersNumber);

			var toolContentId = link.data("tool-content-id");
			$("#selected-content-id").val(toolContentId);
		});

		//insert total learners number taken from the parent tblmonitor.jsp
		$("#total-learners-number").html(TOTAL_LESSON_LEARNERS_NUMBER);
	});
</script>

<!-- Header -->
<div class="row no-gutter">
	<div class="col-xs-12 col-md-12 col-lg-8">
		<h3>
			<c:choose>
				<c:when test="${isIraAssessment}">
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
<div class="row no-gutter">
	<div class="col-xs-6 col-md-4 col-lg-4 ">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<i class="fa fa-users" style="color:gray" ></i> 
					<fmt:message key="label.attendance"/>: <span id="attempted-learners-number">${assessmentDtos[0].attemptedLearnersNumber}</span>/<span id="total-learners-number"></span> 
				</h4> 
			</div>
		</div>
	</div>

	<div class="col-xs-6 col-md-4 col-md-offset-4 col-lg-4 col-lg-offset-2">
		<a href="#nogo" type="button" class="btn btn-sm btn-default buttons_column"
				onclick="javascript:loadTab('aesStudentChoices', document.getElementById('selected-content-id').value, ${isIraAssessment}); return false;">
			<i class="fa fa-file"></i>
			<fmt:message key="label.show.students.choices"/>
		</a>
	</div>                                  
</div>

<div class="row no-gutter">
	<input type="hidden" value="${assessmentDtos[0].assessment.contentId}" id="selected-content-id">
	
	<c:if test="${!isIraAssessment}">
		<ul class="nav nav-tabs">
			<c:forEach var="assessmentDto" items="${assessmentDtos}" varStatus="i">
				<li <c:if test="${(i.index == 0 && toolContentID == null) || (toolContentID != null && assessmentDto.assessment.contentId == toolContentID)}">class="active"</c:if>>
					<a data-toggle="tab" href="#assessment-${assessmentDto.assessment.uid}" 
							data-attempted-learners-number="${assessmentDto.attemptedLearnersNumber}"
							data-tool-content-id="${assessmentDto.assessment.contentId}">
						<c:out value="${assessmentDto.activityTitle}" escapeXml="false"/>
					</a>
				</li>
			</c:forEach>
		</ul>
	</c:if>
		
</div>
<br>
<!-- End notifications -->

<!-- Tables -->
<div class="tab-content">
	<c:forEach var="assessmentDto" items="${assessmentDtos}" varStatus="k">
		<div id="assessment-${assessmentDto.assessment.uid}" class="tab-pane 
			<c:if test="${(k.index == 0 && toolContentID == null) || (toolContentID != null && assessmentDto.assessment.contentId == toolContentID)}">active</c:if>">
			<c:forEach var="question" items="${assessmentDto.questions}" varStatus="i">
				<div class="row no-gutter">
				<div class="col-xs-12 col-md-12 col-lg-10">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<span class="float-left space-right">Q${i.index+1})</span> <c:out value="${question.title}" escapeXml="false"/>
						</h4> 
					</div>
					
					<div class="panel-body">
						<div class="table-responsive">	
							<c:out value="${question.question}" escapeXml="false"/>
							
							<table class="table table-striped">
								<tbody>
									
									<c:choose>
										<c:when test="${question.type == 1}">
											<c:forEach var="questionOption" items="${question.options}" varStatus="j">
													
												<tr>
													<td width="5px">
														${ALPHABET[j.index]}.
													</td>									
													<td>
														<c:out value="${questionOption.optionString}" escapeXml="false"/>
													</td>
												</tr>
																		
											</c:forEach>						
										</c:when>
										
										<c:when test="${question.type == 2}">
											<tr style="background: none;">
												<td style="border-top: none;">
												</td>
																
												<td style="border-top: none; font-weight: bold;">
													Possible answers
												</td>
											</tr>
										
											<c:forEach var="questionOption" items="${question.options}" varStatus="j">
												<tr>
													<td>
														<span class="pull-left">${ALPHABET[j.index]}.</span>
																
														<c:out value="${questionOption.question}" escapeXml="false"/>
													</td>
																
													<td>
														<c:out value="${questionOption.optionString}" escapeXml="false"/>
													</td>
												</tr>
											</c:forEach>
										</c:when>
										
										<c:when test="${(question.type == 3) || (question.type == 4) || (question.type == 6)}">
										</c:when>
															
										<c:when test="${question.type == 5}">
											<tr>
												<td width="5px">
													a.
												</td>
												<td>
													<fmt:message key="label.learning.true.false.true"/>
												</td>
											</tr>
											<tr>
												<td width="5px">
													b.
												</td>								
												<td>
													<fmt:message key="label.learning.true.false.false"/>
												</td>
											</tr>
										</c:when>
															
										<c:when test="${(question.type == 7) || (question.type == 8)}">
											<c:forEach var="questionOption" items="${question.options}" varStatus="j">
															
												<tr>
													<td width="5px">
														${ALPHABET[j.index]}.
													</td>
													<td>
														<c:out value="${questionOption.optionString}" escapeXml="false"/>
													</td>
												</tr>
												
											</c:forEach>
										</c:when>						
										
									</c:choose>
								</tbody>
							</table>
						</div>
					</div>
				</div>  
				</div>
				</div>
			</c:forEach>
		</div>
	</c:forEach>
</div>
<!-- End tables -->
