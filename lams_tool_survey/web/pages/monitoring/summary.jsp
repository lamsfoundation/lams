<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="tool"><lams:WebAppURL/></c:set>

<script type="text/javascript">
	function exportSurvey(sessionId){
		var url = "<c:url value="/monitoring/exportSurvey.do"/>";
	    var reqIDVar = new Date();
		var param = "?toolSessionID=" + sessionId +"&reqID="+reqIDVar.getTime();
		url = url + param;
		location.href=url;
	}
</script>	
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
				<tr>
					<td colspan="2">
						<B><fmt:message key="monitoring.label.group" /> ${surveySession.sessionName}</B> 
					</td>
				</tr>
				<%-- End group title display --%>
			</c:if>
			<tr>
				<th class="first" colspan="2">
					<a href="javascript:;" onclick="launchPopup('<c:url value="/monitoring/listAnswers.do?"/>toolSessionID=${surveySession.sessionId}&questionUid=${question.uid}')">
						${question.shortTitle}
					</a>
					<div style="float:right">
					<%-- Only show pie/bar chart when question is single/multiple choics type --%>
					<c:if test="${question.type !=3}">
						<a href="javascript:;" onclick="launchPopup('<c:url value="/monitoring/viewChartReport.do?chartType=pie&"/>toolSessionID=${surveySession.sessionId}&questionUid=${question.uid}')">
							<img src="${tool}/includes/images/piechart.gif" title="<fmt:message key='message.view.pie.chart'/>" height="22" width="25" border="0">
						</a>
						<a href="javascript:;" onclick="launchPopup('<c:url value="/monitoring/viewChartReport.do?chartType=bar&"/>toolSessionID=${surveySession.sessionId}&questionUid=${question.uid}')">
							<img src="${tool}/includes/images/columnchart.gif" title="<fmt:message key='message.view.bar.chart'/>" height="22" width="25" border="0">
						</a>
					</c:if>
					</div>
				</th>
			</tr>
			<%-- 
			<tr>
				<td><fmt:message key="message.possible.answers"/></td>
				<td><fmt:message key="message.total.user.response"/></td>
			</tr>
			--%>
			<c:set var="optSize" value="${fn:length(question.options)}" />
			<c:forEach var="option" items="${question.options}"  varStatus="optStatus">
				<tr>
					<td>${option.description}</td>
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
				<%-- Reflection list  --%>
				<c:if test="${sessionMap.survey.reflectOnActivity && queStatus.last}">
					<c:set var="userList" value="${sessionMap.reflectList[surveySession.sessionId]}"/>
					<c:forEach var="user" items="${userList}" varStatus="refStatus">
						<c:if test="${refStatus.first}">
							<table cellpadding="0"  class="alternative-color">
							<tr>
								<td colspan="5">
									<h2><fmt:message key="title.reflection"/>	</h2>
								</td>
							</tr>
							<tr>
								<th colspan="2">
									<fmt:message key="monitoring.user.fullname"/>
								</th>
								<th colspan="2">
									<fmt:message key="monitoring.label.user.loginname"/>
								</th>
								<th>
									<fmt:message key="monitoring.user.reflection"/>
								</th>
							</tr>
						</c:if>
						<tr>
							<td colspan="2">
								${user.fullName}
							</td>
							<td colspan="2">
								${user.loginName}
							</td>
							<td >
								<c:set var="viewReflection">
									<c:url value="/monitoring/viewReflection.do?toolSessionID=${surveySession.sessionId}&userUid=${user.userUid}"/>
								</c:set>
								<html:link href="javascript:launchPopup('${viewReflection}')">
									<fmt:message key="label.view" />
								</html:link>
							</td>
						</tr>
						<c:if test="${refStatus.last}">
							</table>
						</c:if>
					</c:forEach>
				</c:if>

		</c:forEach>
			<table>		
					<tr>
						<td >
							<html:link href="javascript:exportSurvey(${surveySession.sessionId});" property="exportExcel" styleClass="button">
									<fmt:message key="label.monitoring..button.export.excel" />
							</html:link>
						</td>
					</tr>		
			</table>
	</c:forEach>
