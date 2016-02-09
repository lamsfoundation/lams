<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="survey" value="${sessionMap.survey}"/>
<c:set var="tool"><lams:WebAppURL/></c:set>

<c:set var="lams">
 		<lams:LAMSURL />
</c:set>

<link type="text/css" href="${lams}/css/jquery-ui-smoothness-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet"> 
<link type="text/css" href="${lams}/css/chart.css" rel="stylesheet" />

<script type="text/javascript">
	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${sessionMap.submissionDeadline}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>',
		toolContentID: '${param.toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};	
</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/d3.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/chart.js"></script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript">
	function exportSurvey(sessionId){
		var url = "<c:url value="/monitoring/exportSurvey.do"/>";
	    var reqIDVar = new Date();
		var param = "?toolSessionID=" + sessionId +"&reqID="+reqIDVar.getTime();
		url = url + param;
		location.href=url;
	}
</script>

<h1>
	<c:out value="${survey.title}" escapeXml="true" />
</h1>
<div class="instructions small-space-top">
	<c:out value="${survey.instructions}" escapeXml="false"/>
</div>
<br/>

<c:if test="${empty summaryList}">
	<div align="center">
		<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
	</div>
</c:if>

	<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">
		<c:set var="surveySession"  value="${group.key}"/>
		<c:set var="questions"  value="${group.value}"/>
		
		<c:if test="${empty questions}">
			<table cellpadding="0"  class="alternative-color">
				<tr>
					<td colspan="2">
						<div align="left">
							<b> <fmt:message key="message.monitoring.summary.no.survey.for.group" /> </b>
						</div>
					</td>
				</tr>
			</table>
		</c:if>
		<c:forEach var="question" items="${questions}" varStatus="queStatus">
			<%-- display group name on first row--%>
			<c:if test="${queStatus.first}">
				<table cellpadding="0"  class="alternative-color">
					<c:if test="${sessionMap.isGroupedActivity}">
						<tr>
							<td colspan="2">
								<B><fmt:message key="monitoring.label.group" /> ${surveySession.sessionName}</B> 
							</td>
						</tr>
					</c:if>
					<%-- End group title display --%>
			</c:if>
			<tr>
				<th class="first" colspan="2">
					<a href="javascript:;" onclick="launchPopup('<c:url value="/monitoring/listAnswers.do?"/>toolSessionID=${surveySession.sessionId}&questionUid=${question.uid}')">
						<c:out value="${question.shortTitle}"/> 
					</a>
					<div style="float:right">
					<%-- Only show pie/bar chart when question is single/multiple choics type --%>
					<c:if test="${question.type != 3}">
						<c:set var="chartURL" value="${tool}showChart.do?toolSessionID=${surveySession.sessionId}&questionUid=${question.uid}" />
						<img src='<c:out value="${tool}"/>includes/images/piechart.gif'
							title="<fmt:message key='message.view.pie.chart'/>"
							style="cursor: pointer; width: 30px; border: none"
							onclick="javascript:drawChart('pie', 'chartDiv${queStatus.index}', '${chartURL}')"> 
					</c:if>
					</div>
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
				<td id="chartDiv${queStatus.index}" style="height: 220px; display: none" colspan="2">
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
					<td><fmt:message key="label.open.response"/></td>
					<td>
						${question.openResponseCount} 
					</td>
				</tr>
			</c:if>
			<c:if test="${queStatus.last}">
				</table>
			</c:if>
		</c:forEach>
			<table>		
					<tr>
						<td >
							<c:if test="${sessionMap.survey.reflectOnActivity}">
								<c:set var="listReflections"><c:url value="/monitoring/listReflections.do?toolSessionID=${surveySession.sessionId}"/></c:set>
								<html:link href="javascript:launchPopup('${listReflections}')" styleClass="button">
									<fmt:message key="page.title.monitoring.view.reflection" />
								</html:link>
							</c:if>	
							<html:link href="javascript:exportSurvey(${surveySession.sessionId});" property="exportExcel" styleClass="button">
									<fmt:message key="label.monitoring.button.export.excel" />
							</html:link>
						</td>
					</tr>		
			</table>
	</c:forEach>
	
<br />	
	
<%@include file="advanceoptions.jsp"%>

<%@include file="daterestriction.jsp"%>
	