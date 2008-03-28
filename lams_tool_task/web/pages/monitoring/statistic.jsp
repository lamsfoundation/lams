<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summary" value="${sessionMap.summary}"/>

<table cellspacing="3">
	<c:if test="${empty summary}">
		<div align="center">
			<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
		</div>
	</c:if>
	
	<%-- display group name on first row--%>
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
	
	<c:forEach var="item" items="${summary.taskListItems}" varStatus="status">

		<c:if test="${item.uid == -1}">
			<tr>
				<td colspan="4">
					<div align="left">
						<b> <fmt:message key="message.monitoring.summary.no.resource.for.group" /> </b>
					</div>
				</td>
			</tr>
		</c:if>
		<c:if test="${item.uid != -1}">
			<tr>
				<td>
					${item.title}
				</td>
				<td>
					${item.createBy.loginName}
				</td>
				<td align="center">
					${summary.visitNumbers[status.index]}
				</td>
			</tr>
		</c:if>
	</c:forEach>

</table>
