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
	
</c:otherwise>
</c:choose>	

