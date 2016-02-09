<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>

<c:if test="${empty sessionDtos}">
	<div align="center">
		<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
	</div>
</c:if>

<c:out value="${sessionMap.isGroupedActivity}"/>
<c:forEach var="sessionDto" items="${sessionDtos}">
	<%-- display group name on first row--%>
	<h1><fmt:message key="monitoring.label.group" /> ${sessionDto.sessionName}	</h1>	
		
	<table cellspacing="3" class="alternative-color small-space-top">
		<tr>
			<th width="45%">
				<fmt:message key="monitoring.label.title" />
			</th>
			<th width="35%">
				<fmt:message key="monitoring.label.suggest" />
			</th>
			<th width="20%" align="center">
				<fmt:message key="monitoring.label.number.learners" />
			</th>
		</tr>
		
		<c:forEach var="item" items="${sessionDto.taskListItems}" varStatus="status">
	
			<c:if test="${item.uid == -1}">
				<tr>
					<td colspan="3">
						<div align="left">
							<b> <fmt:message key="message.monitoring.summary.no.resource.for.group" /> </b>
						</div>
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
					<td align="center">
						${sessionDto.visitNumbers[status.index]}
					</td>
				</tr>
			</c:if>
		</c:forEach>
	
	</table>	
</c:forEach>
