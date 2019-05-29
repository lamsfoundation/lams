<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="leaderDto" value="${sessionMap.leaderDto}"/>

<script type="text/javascript">
	$(document).ready(function(){
	   	// must display charts after screen is visible or cannot calculate widths.
		drawHistogram('chartDiv', 
					'<c:url value="/monitoring/getMarkChartData.do?sessionMapID=${sessionMapID}"/>', 
					'<fmt:message key="label.marks"/>', '<fmt:message key="label.number.groups.in.mark.range"/>');
	});		
</script>

<c:choose>

<c:when test="${empty summaryList}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="message.monitoring.summary.no.session" />
	</lams:Alert>
</c:when>
	
<c:otherwise>
	
	<p><fmt:message key="label.graph.help"/></p>
	
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
				<fmt:message key="label.average.mark" />:
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
			 <div id="chartDiv" class="markChartDiv"></div>
	</div>
	</div>
	</div>
	
	<div class="panel panel-default" >
      	<div class="panel-body">
      	<h4><fmt:message key="label.qb.stats" /></h4>
      	
		<table class="table table-condensed table-striped table-no-border">
			<tr>		
				<th scope="col">
					#
				</th>
				<th scope="col">
					<fmt:message key="label.question"/>
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
	
</c:otherwise>
</c:choose>	
