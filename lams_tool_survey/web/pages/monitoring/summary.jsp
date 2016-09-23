<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="survey" value="${sessionMap.survey}"/>
<c:set var="tool"><lams:WebAppURL/></c:set>

<c:set var="lams">
 		<lams:LAMSURL />
</c:set>

<script type="text/javascript">
	function exportSurvey(sessionId){
		var url = "<c:url value="/monitoring/exportSurvey.do"/>";
	    var reqIDVar = new Date();
		var param = "?toolSessionID=" + sessionId +"&reqID="+reqIDVar.getTime();
		url = url + param;
		location.href=url;
	}
</script>

<div class="panel">
	<h4>
	    <c:out value="${survey.title}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${survey.instructions}" escapeXml="false"/>
	</div>
	
		<c:set var="sessionButtons">
			<c:if test="${sessionMap.survey.reflectOnActivity}">
				<c:set var="listReflections"><c:url value="/pages/monitoring/listreflections.jsp?toolSessionID=${surveySession.sessionId}"/></c:set>
				<html:link href="javascript:launchPopup('${listReflections}')" styleClass="btn btn-default ${sessionButtonSize}">
					<fmt:message key="page.title.monitoring.view.reflection" />
				</html:link>
			</c:if>	
			<html:link href="javascript:exportSurvey(${surveySession.sessionId});" property="exportExcel" styleClass="btn btn-default ${sessionButtonSize} loffset5">
				<fmt:message key="label.monitoring.button.export.excel" />
			</html:link>
		</c:set>

	
	<c:if test="${empty summaryList}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:if>
	

</div>

<c:set var="sessionButtonSize">btn-sm</c:set>
<c:if test="${sessionMap.isGroupedActivity}">
<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
<c:set var="sessionButtonSize">btn-xs</c:set>
</c:if>

	<c:forEach var="group" items="${summaryList}" varStatus="status">
		<c:set var="surveySession"  value="${group.key}"/>
		<c:set var="questions"  value="${group.value}"/>

		<c:choose>
		<c:when test="${sessionMap.isGroupedActivity}">	
		    <div class="panel panel-default" >
	        <div class="panel-heading" id="heading${surveySession.sessionId}">
	        	<span class="panel-title collapsable-icon-left">
	        	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${surveySession.sessionId}" 
						aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${surveySession.sessionId}" >
				<fmt:message key="monitoring.label.group" />&nbsp;${surveySession.sessionName}</a>
				</span>
				<span class="pull-right btn-group">${sessionButtons}</span>
	        </div>
	        
	        <div id="collapse${surveySession.sessionId}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${surveySession.sessionId}">
		</c:when>
		<c:otherwise>
			<div>${sessionButtons}</div>
		</c:otherwise>
		</c:choose>
		
		<c:if test="${empty questions}">
			<b> <fmt:message key="message.monitoring.summary.no.survey.for.group" /> </b>
		</c:if>
		
		<c:forEach var="question" items="${questions}" varStatus="queStatus">
			<table class="table table-condensed table-no-border">
			<tr>
				<th class="first" colspan="2">
					<a href="javascript:;" onclick="launchPopup('<c:url value="/monitoring/listAnswers.do?"/>toolSessionID=${surveySession.sessionId}&questionUid=${question.uid}')">
						<c:out value="${question.shortTitle}"/> 
					</a>

					<%-- Only show pie/bar chart when question is single/multiple choics type --%>
					<c:if test="${question.type != 3}">
						<c:set var="chartURL" value="${tool}showChart.do?toolSessionID=${surveySession.sessionId}&questionUid=${question.uid}" />
						<a class="fa fa-lg fa-pie-chart text-primary btn btn-xs btn-primary pull-right" title="<fmt:message key='message.view.pie.chart'/>"
							onclick="javascript:drawChart('pie', 'chartDiv${surveySession.sessionId}_${queStatus.index}', '${chartURL}')"></a> 
					</c:if>

				</th>
			</tr>
			<c:set var="optSize" value="${fn:length(question.options)}" />
			<c:forEach var="option" items="${question.options}"  varStatus="optStatus">
				<tr>
					<td><c:out value="${option.description}" escapeXml="true"/></td>
					<td>
						<c:set var="imgTitle">
							<fmt:message key="message.learner.choose.answer.percentage">
								<fmt:param>${option.response}</fmt:param>
							</fmt:message>
						</c:set>
						<c:set var="imgIdx">
							${optStatus.index % 5 + 1}
						</c:set>			
						<img src="${tool}/includes/images/bar${imgIdx}.gif" height="10" width="${option.response * 2}" 
						title="${imgTitle}">
						${option.responseCount} (${option.responseFormatStr}%)
					</td>
				</tr>
			</c:forEach>
			<tr>
				<td id="chartDiv${surveySession.sessionId}_${queStatus.index}" style="height: 220px; display: none" colspan="2">
				</td>
			</tr>
			<c:if test="${question.appendText}">
				<tr>
					<td><fmt:message key="label.open.response"/></td>
					<td>
						<c:set var="imgTitle">
							<fmt:message key="message.learner.choose.answer.percentage">
								<fmt:param>${question.openResponseFormatStr}</fmt:param>
							</fmt:message>
						</c:set>
						<c:set var="imgIdx">
							${(optSize % 5)  + 1}
						</c:set>						
						<img src="${tool}/includes/images/bar${imgIdx}.gif" height="10" width="${question.openResponse * 2}" 
						title="${imgTitle}">
						${question.openResponseCount} (${question.openResponseFormatStr}%)
					</td>
				</tr>
			</c:if>
			<c:if test="${question.type == 3}">
				<tr>
					<td width="30%"><fmt:message key="label.open.response"/></td>
					<td>
						${question.openResponseCount} 
					</td>
				</tr>
			</c:if>
			<c:if test="${queStatus.last}">
				</table>
			</c:if>
		</c:forEach>

	<c:if test="${sessionMap.isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
			
	</c:forEach>
	
<c:if test="${sessionMap.isGroupedActivity}">
</div> 
</c:if>
	
<%@include file="advanceoptions.jsp"%>

<%@include file="daterestriction.jsp"%>
	