<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>
<style>
	#barChart {
		width: 515px !important; 
		height: 240px !important;
	}
</style>

<!-- ChartJS. Colour used is brand-primary for purple skin. -->
<script>
	$(document).ready(function(){
		var ctx = document.getElementById("barChart").getContext("2d"),
			chartColor = 'rgba(1, 117, 226, 0.85)',
			myNewChart = new Chart(ctx, {
				type : 'bar',
				data : {
					datasets : [ {
						data : [<c:forEach var="groupSummary" items="${groupSummaries}">${groupSummary.totalPercentage},</c:forEach>],
						backgroundColor : chartColor,
						borderColor     :  chartColor,
						hoverBackgroundColor : chartColor,
						hoverBorderColor: chartColor
					} ],
					labels :  [<c:forEach var="groupSummary" items="${groupSummaries}">"${groupSummary.sessionName}",</c:forEach>],
				},
				options : {
					legend : {
						display : false
					},
					scales : {
						xAxes : [
							{
								 ticks : {
									fontSize : 30
								}
							}
						],
						yAxes : [
							{
							    ticks : {
									beginAtZero   : true,
									maxTicksLimit : 6,
									suggestedMax  : 100,
									fontSize : 25
								}
							}
						]
					}
				}
			});

		//insert total learners number taken from the parent tblmonitor.jsp
		$(".total-learners-number").html(TOTAL_LESSON_LEARNERS_NUMBER);
	});
</script>

<div class="container-fluid">
	 <div class="row">
		<div class="col-10 offset-1 text-center">
			<h3>
				<fmt:message key="label.tra.questions.marks"/>
			</h3>
		</div>
	</div>
	
	<div class="row mb-3">
		<div class="col-5 offset-1 pt-1">
			<h4>
				<i class="fa fa-users text-secondary"></i> 
				<fmt:message key="label.attendance"/>: <span>${attemptedLearnersNumber}</span>/<span class="total-learners-number"></span> 
			</h4> 
		</div>
		<div class="col-5 text-end">
			<button class="btn btn-secondary" type="button"
				 onclick="javascript:loadTab('tratStudentChoices', $('#load-trat-student-choices-tab-btn'))">
				<i class="fa fa-list-check"></i>
				<fmt:message key="label.show.students.choices"/>
			</button>   
		</div>                 
	</div>
	<!-- End notifications -->              
	
	<!-- Tables -->
	<c:forEach var="item" items="${items}" varStatus="i">
		<div class="row mb-3">
			<div class="col-10 offset-1">			
				<div class="card">
					<div class="card-header">			
						<h3 class="card-title mb-3" style="font-size: initial">
							${i.index+1}. <c:out value="${item.qbQuestion.name}" escapeXml="false"/>
						</h3> 
						<c:out value="${item.qbQuestion.description}" escapeXml="false"/>
					</div>
			
					<div class="card-body">
						<div class="table-responsive">
							<table class="table">
								<tbody>
									<c:forEach var="qbOption" items="${item.qbQuestion.qbOptions}" varStatus="j">
										<c:set var="cssClass"><c:if test='${qbOption.correct}'>bg-success</c:if></c:set>
										<tr>
											<c:choose>
												<c:when test="${item.qbQuestion.type == 1 or item.qbQuestion.type == 8}">
													<td width="5px" class="${cssClass}">
														<c:if test="${item.qbQuestion.prefixAnswersWithLetters}">
															${ALPHABET[j.index]}.
														</c:if>
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
			</div>
		</div>
	</c:forEach>
	<!-- End tables -->
	
	<div class="row">
		<div class="col-8 offset-2 text-center">
			<div class="card">
				<div class="card-header">
					<fmt:message key="label.tra.results.by.team"/>&nbsp;<small>(<fmt:message key="label.average"/>)</small>
				</div>
				
				<div class="card-body">
					<canvas id="barChart" height="240" width="515"></canvas>
				</div>
			</div>  
		</div>
	</div>
</div>