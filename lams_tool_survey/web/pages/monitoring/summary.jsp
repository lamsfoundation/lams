<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<c:if test="${empty summaryList}">
	<div align="center">
		<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
	</div>
</c:if>

<table cellpadding="0">
	<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">
		<c:set var="groupSize" value="${fn:length(group)}" />
		<c:set var="surveySession"  value="${summaryList.key}"/>
		<c:set var="questions"  value="${summaryList.value}"/>
		
		<c:if test="${empty questions}">
			<tr>
				<td colspan="2">
					<div align="left">
						<b> <fmt:message key="message.monitoring.summary.no.survey.for.group" /> </b>
					</div>
				</td>
			</tr>
		</c:if>
		<c:forEach var="item" items="${questions}" varStatus="status">
			<%-- display group name on first row--%>
			<c:if test="${status.first}">
				<tr>
					<td colspan="2">
						<B><fmt:message key="monitoring.label.group" /> ${surveySession.sessionName}</B> 
						<SPAN style="font-size: 12px;"> 
							<c:if test="${firstGroup.index==0}">
								<fmt:message key="monitoring.summary.note" />
							</c:if> 
						</SPAN>
					</td>
				</tr>
				<%-- End group title display --%>
			</c:if>
			<tr>
				<th>${question.shortTitle}</th>
				<th>
					<a href="javascript:;" onclick="launchPopup('<c:url value="/monitoring/viewPieChart.do?"/>toolSessionID${surveySession.sessionId}&questionUid=${question.uid}')">
						<img src="/includes/images/piechart.gif" title="<fmt:message key='message.view.pie.chart'/>">
					</a>
					<a href="javascript:;" onclick="launchPopup('<c:url value="/monitoring/viewBarChart.do?"/>toolSessionID${surveySession.sessionId}&questionUid=${question.uid}')">
						<img src="/includes/images/barchart.gif" title="<fmt:message key='message.view.bar.chart'/>">
					</a>
				</th>
			</tr>
			<tr>
				<td><fmt:message key="message.possible.answers"/></td>
				<td><fmt:message key="message.total.user.response"/></td>
			</tr>
			<c:set var="optSize" value="${fn:length(question.options)}" />
			<c:forEach var="option" items="question.options"  varStatus="status">
				<tr>
					<td>${option.description}</td>
					<td>
						<c:set var="imgTitle">
							<fmt:message key="message.learner.choose.answer">
								<fmt:param>${option.response}</fmt:param>
							</fmt:message>
						</c:set>
						<c:set var="imgIdx">
							${status.index % 5}
						</c:set>			
						<img src="/includes/images/bar${imgIdx}.gif" height="10" width="${option.response * 2}" 
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
							<fmt:message key="message.learner.choose.answer">
								<fmt:param>${question.openResponseFormatStr}</fmt:param>
							</fmt:message>
						</c:set>
						<c:set var="imgIdx">
							${(optSize + 1) % 5}
						</c:set>						
						<img src="/includes/images/bar${imgIdx}.gif" height="10" width="${question.openResponse * 2}" 
						title="${imgTitle}">
						${question.openResponseCount} (${question.openResponseFormatStr}%)
					</td>
				</tr>
			</c:if>
				<%-- Reflection list  --%>
				<c:if test="${sessionMap.survey.reflectOnActivity && status.last}">
					<c:set var="userList" value="${sessionMap.reflectList[surveySession.sessionId]}"/>
					<c:forEach var="user" items="${userList}" varStatus="refStatus">
						<c:if test="${refStatus.first}">
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
									<c:url value="/monitoring/viewReflection.do?toolSessionID=${item.sessionId}&userUid=${user.userUid}"/>
								</c:set>
								<html:link href="javascript:launchPopup('${viewReflection}')">
									<fmt:message key="label.view" />
								</html:link>
							</td>
						</tr>
					</c:forEach>
				</c:if>
			
		</c:forEach>
		
	</c:forEach>
</table>
