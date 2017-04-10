<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>

<script type="text/javascript">
	$(document).ready(function(){
		
		debugger;
		
		$('#activity-evaluation').on('change', function() {

			if (this.value == "dummy") {
				return;
			}

			$.ajax({
				url: '<c:url value="/monitoring/setActivityEvaluation.do"/>',
				data: {
					toolContentID: "${sessionMap.toolContentID}",
					activityEvaluation: this.value
				},
				dataType: 'json',
				success: function (json) {
					if (json.success == "true") {
						alert("<fmt:message key='label.tool.output.has.been.changed'/>");
					}
				},
				error: function (xhr, ajaxOptions, thrownError) {
					alert("<fmt:message key='label.operation.failed'/>");
				}
			});
			
		});
		
	   	// must display charts after screen is visible or cannot calculate widths.
		<c:forEach var="sessionDto" items="${sessionDtos}">
		drawHistogram('chartDiv${sessionDto.sessionId}', '<c:url value="/monitoring/getMarkChartData.do?sessionMapID=${sessionMapID}&toolSessionID=${sessionDto.sessionId}"/>','Marks', 'Number of Learners in Mark Range');
		</c:forEach>	


	});		
</script>

<c:choose>

	<%-- <lams:WaitingSpinner id="statisticArea_Busy"/> --%>
	
	<c:when test="${empty sessionDtos}">
		<lams:Alert type="warn" id="no-edit" close="false">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:when>
		
	<c:otherwise>
	
		<p width="90%">In the graph(s) below, click on a bar in the graph and then use your mouse wheel to zoom in and out of the graph. Once you zoom in, 
		the grey selection in the bottom graph can be dragged 
			left or right to show a different set of marks.</p>
			
		<c:forEach var="sessionDto" items="${sessionDtos}">

		    <div class="panel panel-default" >

			<c:if test="${sessionMap.isGroupedActivity}">	
		        <div class="panel-heading" id="heading${sessionDto.sessionId}">
					<span class="panel-title">
						<fmt:message key="monitoring.label.group" />: <c:out value="${sessionDto.sessionName}" />
					</span>
		        </div>
			</c:if>

	
       		<div class="panel-body">
			<table class="table table-condensed table-striped table-no-border">
				<tr>
					<td class="field-name" width="25%">
						<fmt:message key="label.number.learners.per.session" />:
					</td>
					<td>
						<c:out value="${sessionDto.numberLearners}" />
					</td>
					<td class="field-name" width="25%">
						<fmt:message key="label.monitoring.question.summary.average.mark" />:
					</td>
					
					<td>
						<c:out value="${sessionDto.avgMark}" />
					</td>
				</tr>
				<tr>
					<td class="field-name" width="25%">
						Lowest Mark:
					</td>
					<td>
						<c:out value="${sessionDto.minMark}" />
					</td>
					<td class="field-name" width="25%">
						Highest Mark:
					</td>
					
					<td>
						<c:out value="${sessionDto.maxMark}" />
					</td>
				</tr>
			</table>
			
			<div class="row">
				 <div class="col-xs-12"></div>
 				 <div id="chartDiv${sessionDto.sessionId}" class="chartDiv"></div>
				</div>
			</div>
					
			</div>

		</c:forEach>
		
	
	</c:otherwise>
</c:choose>

<%@ include file="parts/toolOutput.jsp"%>
