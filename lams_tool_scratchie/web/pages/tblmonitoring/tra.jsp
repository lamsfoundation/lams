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
			myNewChart = new Chart(ctx, {
				type : 'bar',
				data : {
					datasets : [ {
						data : [<c:forEach var="groupSummary" items="${groupSummaries}">${groupSummary.totalPercentage},</c:forEach>],
						backgroundColor : "#85237d",
						borderColor     :  "#85237d",
						hoverBackgroundColor : "#85237d",
						hoverBorderColor: "#85237d"
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
		$("#total-learners-number").html(TOTAL_LESSON_LEARNERS_NUMBER);
	});
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
<div class="row no-gutter">
	<div class="col-xs-6 col-md-4 col-lg-4 ">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<i class="fa fa-users" style="color:gray" ></i> 
					<fmt:message key="label.attendance"/>: ${attemptedLearnersNumber}/<span id="total-learners-number"></span> 
				</h4> 
			</div>
		</div>
	</div>
	
	<c:if test="${attemptedLearnersNumber != 0}">
		<div class="col-xs-6 col-md-4 col-md-offset-4 col-lg-4 col-lg-offset-2">
			<a href="#nogo" type="button" class="btn btn-sm btn-default buttons_column"
					onclick="javascript:loadTab('traStudentChoices'); return false;">
				<i class="fa fa-file"></i>
				<fmt:message key="label.show.students.choices"/>
			</a>
		</div>
	</c:if>                                   
</div>
<!-- End notifications -->              

<!-- Tables -->
<c:forEach var="item" items="${items}" varStatus="i">
	<div class="row no-gutter">
	<div class="col-xs-12 col-md-12 col-lg-12">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title" style="margin-bottom: 10px;font-size: initial;">
				${i.index+1}. <c:out value="${item.qbQuestion.name}" escapeXml="false"/>
			</h3> 
			<c:out value="${item.qbQuestion.description}" escapeXml="false"/>
		</div>
		
		<div class="panel-body">
			<div class="table-responsive">
				<table class="table table-striped">
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
	</div>
	</div>
</c:forEach>
<!-- End tables -->

<!-- Chart -->
<div class="row no-gutter">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<fmt:message key="label.tra.results.by.team"/> <small>(<fmt:message key="label.average"/>)</small>
			</div>
			
			<div class="panel-body">
				<canvas id="barChart" height="240" width="515"></canvas>
			</div>
		</div>  
	</div>
</div>
<!-- Chart ends-->

