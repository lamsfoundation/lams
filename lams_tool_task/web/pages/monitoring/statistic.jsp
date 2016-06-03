<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>

<c:if test="${empty sessionDtos}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
	</lams:Alert>
</c:if>

<c:forEach var="sessionDto" items="${sessionDtos}">

	<c:if test="${sessionMap.isGroupedActivity}">	
	   <div class="panel panel-default" >
	      <div class="panel-heading">
			<span class="panel-title">
				<fmt:message key="monitoring.label.group" />&nbsp;${sessionDto.sessionName}
			</span>
	      </div>
	</c:if>

	<table class="table table-condensed table-striped">
		<tr>
			<th width="45%">
				<fmt:message key="monitoring.label.title" />
			</th>
			<th width="35%">
				<fmt:message key="monitoring.label.suggest" />
			</th>
			<th width="20%" class="text-center">
				<fmt:message key="monitoring.label.number.learners" />
			</th>
		</tr>
		
		<c:forEach var="item" items="${sessionDto.taskListItems}" varStatus="status">
	
			<c:if test="${item.uid == -1}">
				<tr>
					<td colspan="3" class="text-center">
						<b> <fmt:message key="message.monitoring.summary.no.resource.for.group" /> </b>
					</td>
				</tr>
			</c:if>
			<c:if test="${item.uid != -1}">
				<tr>
					<td>
						<c:out value="${item.title}" escapeXml="true"/>
					</td>
					<td>
						<c:out value="${item.createBy.loginName}" escapeXml="true"/>
					</td>
					<td class="text-center">
						${sessionDto.visitNumbers[status.index]}
					</td>
				</tr>
			</c:if>
		</c:forEach>
	</table>	
	
	<c:if test="${sessionMap.isGroupedActivity}">	
		</div>
	</c:if>

</c:forEach>
