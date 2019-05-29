<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>
<c:set var="leaderDto" value="${sessionMap.leaderDto}"/>

<script type="text/javascript">
	$(document).ready(function(){
		
		$('#activity-evaluation').on('focus', function(){
			$(this).data('previousValue', this.value);
		}).on('change', function() {
			if (!this.value && !confirm("<fmt:message key='warn.tool.output.change.none'/>")) {
				$(this).val($(this).data('previousValue'));
				return;
			}
			$(this).data('previousValue', this.value);
			$.ajax({
				url: '<c:url value="/monitoring/setActivityEvaluation.do?sessionMapID=${sessionMapID}"/>',
				data: {
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
 		<c:choose>
		<c:when test="${sessionMap.assessment.useSelectLeaderToolOuput}">	
			drawHistogram('chartDivLeader${sessionMap.toolContentID}', 
					'<c:url value="/monitoring/getMarkChartData.do?sessionMapID=${sessionMapID}"/>', 
					'<fmt:message key="label.marks"/>', '<fmt:message key="label.number.groups.in.mark.range"/>');
		</c:when>
		<c:otherwise>
			<c:forEach var="sessionDto" items="${sessionDtos}">
			drawHistogram('chartDivSession${sessionDto.sessionId}', 
					'<c:url value="/monitoring/getMarkChartData.do?sessionMapID=${sessionMapID}&toolSessionID=${sessionDto.sessionId}"/>', 
					'<fmt:message key="label.marks"/>', '<fmt:message key="label.number.learners.in.mark.range"/>');
			</c:forEach>	
		</c:otherwise>
		</c:choose>
 

	});		
</script>

<c:choose>

<c:when test="${empty sessionDtos}">
	<lams:Alert type="warn" id="no-edit" close="false">
		<fmt:message key="message.monitoring.summary.no.session" />
	</lams:Alert>
</c:when>
	
<c:otherwise>
	
	<p><fmt:message key="label.graph.help"/></p>
	
	<c:choose>
	<c:when test="${sessionMap.assessment.useSelectLeaderToolOuput}">	
		<div class="panel panel-default" >
       	<div class="panel-body">
 		<table class="table table-condensed table-striped table-no-border">
			<tr>
				<td class="field-name" width="25%">
					<fmt:message key="label.number.groups.finished" />:
				</td>
 				<td>
					<c:out value="${leaderDto.numberGroupsLeaderFinished}" />
				</td>
				<td class="field-name" width="25%">
					<fmt:message key="label.monitoring.question.summary.average.mark" />:
				</td>
 				<td>
					<c:out value="${leaderDto.avgMark}" />
				</td>
 			</tr>
 			<tr>
				<td class="field-name" width="25%">
					<fmt:message key="label.lowest.mark"/>
				</td>
				<td>
					<c:out value="${leaderDto.minMark}" />
				</td>
				<td class="field-name" width="25%">
					<fmt:message key="label.highest.mark"/>
				</td>
				<td>
					<c:out value="${leaderDto.maxMark}" />
				</td>
			
			</tr>
 		</table>
 		<div class="row">
			 <div class="col-xs-12"></div>
				 <div id="chartDivLeader${sessionMap.toolContentID}" class="markChartDiv"></div>
		</div>
		</div>
		</div>
	</c:when>
		
	<c:otherwise>
	<c:forEach var="sessionDto" items="${sessionDtos}">
	    <div class="panel panel-default" >
			<c:if test="${!sessionMap.assessment.useSelectLeaderToolOuput && sessionMap.isGroupedActivity}">	
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
						<fmt:message key="label.lowest.mark"/>
					</td>
					<td>
						<c:out value="${sessionDto.minMark}" />
					</td>
					<td class="field-name" width="25%">
						<fmt:message key="label.highest.mark"/>
					</td>
					
					<td>
						<c:out value="${sessionDto.maxMark}" />
					</td>
				</tr>
			</table>
			<div class="row">
				 <div class="col-xs-12"></div>
 				 <div id="chartDivSession${sessionDto.sessionId}" class="markChartDiv"></div>
				</div>
			</div>
		</div>
	</c:forEach>
	</c:otherwise>
	</c:choose>		
	
	<div id="accordion-qb-stats" class="panel-group voffset20" role="tablist" aria-multiselectable="true"> 
	    <div class="panel panel-default">
	        <div class="panel-heading collapsable-icon-left" id="heading-qb-stats">
	        	<span class="panel-title">
			    	<a class="collapsed" role="button" data-toggle="collapse" href="#qb-stats" aria-expanded="true" aria-controls="qb-stats">
		          		<fmt:message key="label.qb.stats" />
		          	</a>
	      		</span>
	        </div>
	
			<div aria-expanded="false" id="qb-stats" class="panel-body panel-collapse collapse" role="tabpanel" aria-labelledby="heading-qb-stats">
				<table class="table table-condensed table-striped table-no-border">
					<tr>		
						<th scope="col">
							#
						</th>
						<th scope="col">
							<fmt:message key="label.monitoring.question.summary.question"/>
						</th>
						<th scope="col">
							<fmt:message key="label.qb.participant.count"/>
						</th>
						<th scope="col">
							<fmt:message key="label.qb.difficulty.index"/>
						</th>
						<th scope="col">
							<fmt:message key="label.qb.discrimination.index"/>
						</th>
						<th scope="col">
							<fmt:message key="label.qb.point.biserial"/>
						</th>
					</tr>
						
					<c:forEach var="question" items="${qbStats}" varStatus="i">
						<c:set var="activityDTO" value="${question.value}" />
						<tr>
							<td>
								${i.index + 1}
							</td>
							<td>
								<c:out value="${question.key}" escapeXml="false"/>			
							</td>
							<td>
								<c:out value="${activityDTO.participantCount}" />
							</td>
							<c:choose>
								<c:when test="${activityDTO.participantCount < 2}">
									<td>-</td>
									<td>-</td>
									<td>-</td>
								</c:when>
								<c:otherwise>
									<td><fmt:formatNumber type="number" maxFractionDigits="2" value="${activityDTO.difficultyIndex}" /></td>
									<td><fmt:formatNumber type="number" maxFractionDigits="2" value="${activityDTO.discriminationIndex}" /></td>
									<td><fmt:formatNumber type="number" maxFractionDigits="2" value="${activityDTO.pointBiserial}" /></td>
								</c:otherwise>
							</c:choose>
						</tr>
					</c:forEach>	
				</table>
			</div>
		</div>
	</div>

</c:otherwise>
</c:choose>

<%@ include file="parts/toolOutput.jsp"%>
