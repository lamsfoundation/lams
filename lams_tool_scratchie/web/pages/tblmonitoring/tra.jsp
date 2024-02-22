<%@ include file="/common/taglibs.jsp"%>
<c:set var="newLineChar" value="<%= \"\r\n\" %>" />

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

<div class="container-main ms-4">
	<div class="float-end">
		<button class="btn btn-primary btn-icon-return" type="button"
				 onclick="loadTab('tratStudentChoices', $('#load-trat-student-choices-tab-btn'))">
			<fmt:message key="label.show.students.choices"/>
		</button>   
	</div>                 

	<h3>
		<fmt:message key="label.tra.questions.marks"/>
	</h3>
	
	<div class="pt-1 mb-3">
		<div>
			<i class="fa fa-users text-secondary"></i> 
			<fmt:message key="label.attendance"/>: <span>${attemptedLearnersNumber}</span>/<span class="total-learners-number"></span> 
		</div>
	</div>          
	
	<!-- Tables -->
	<c:forEach var="item" items="${items}" varStatus="i">		
		<div class="lcard mb-3">
			<div class="card-header">
				${i.index+1}. <c:out value="${item.qbQuestion.name}" escapeXml="false"/>
			</div>
			
			<div class="card-body">
				<c:out value="${item.qbQuestion.description}" escapeXml="false"/>
						
				<div class="table-responsive">
					<table class="table table-hover">
						<tbody>
							<c:forEach var="qbOption" items="${item.qbQuestion.qbOptions}" varStatus="j">
								<c:set var="cssClass"><c:if test='${qbOption.correct}'>text-bg-success</c:if></c:set>
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
	</c:forEach>
	<!-- End tables -->
	
	<div class="lcard">
		<div class="card-header">
			<fmt:message key="label.tra.results.by.team"/>&nbsp;<small>(<fmt:message key="label.average"/>)</small>
		</div>
				
		<div class="card-body">
			<canvas id="barChart" height="240" width="515"></canvas>
		</div>
	</div> 
</div>
