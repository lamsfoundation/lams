<%@ include file="/common/taglibs.jsp"%>

<!-- ChartJS. Colour used is brand-primary for purple skin. -->
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/chartjs.js"></script>
<script>
	$(document).ready(function(){
		var barData = {
			labels: [<c:forEach var="groupRow" items="${groupRows}">"${groupRow[0]}",</c:forEach>],
			datasets: [{
				label: "My First dataset",
				fillColor: "#85237d",
				strokeColor: "#85237d",
				highlightFill: "#85237d",
				highlightStroke: "#85237d",
				data: [<c:forEach var="groupRow" items="${groupRows}">${groupRow[1]},</c:forEach>]
			}]
		};

		var barOptions = {
			scaleBeginAtZero: true,
			scaleShowGridLines: true,
			scaleGridLineColor: "rgba(0,0,0,.05)",
			scaleGridLineWidth: 1,
			barShowStroke: true,
			barStrokeWidth: 2,
			barValueSpacing: 5,
			barDatasetSpacing: 1,
			responsive: true,
		};

		var ctx = document.getElementById("barChart").getContext("2d");
		var myNewChart = new Chart(ctx).Bar(barData, barOptions);

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
			<h4 class="panel-title">
				Q${i.index+1}) <c:out value="${item.qbQuestion.description}" escapeXml="false"/>
			</h4> 
		</div>
		
		<div class="panel-body">
			<div class="table-responsive">
				<table class="table table-striped">
					<tbody>
						<c:forEach var="optionDto" items="${item.optionDtos}" varStatus="j">
							<tr>
								<td width="5px">
									${ALPHABET[j.index]}.
								</td>
								<td>
									<c:out value="${optionDto.qbOption.name}" escapeXml="false"/>
								</td>
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
				<canvas id="barChart" height="240" width="515" style="width: 515px; height: 240px;"></canvas>
			</div>
		</div>  
	</div>
</div>
<!-- Chart ends-->
