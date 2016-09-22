<%@ include file="/taglibs.jsp"%>

<h4>
	<a href="sysadminstart.do">
		<fmt:message key="sysadmin.maintain" />
	</a>
</h4>
<h1>
	<fmt:message key="label.manage.tool.consumers" />
</h1>

<p class="align-right">
	<input class="button" name="addNewLtiConsumer" type="button" value="<fmt:message key='label.add.tool.consumer' />" 
		onClick="javascript:document.location='ltiConsumerManagement.do?method=edit'" />
</p>
<table class=alternative-color width="100%">

	<tr>
		<th><fmt:message key="sysadmin.serverid" /></th>
		<th><fmt:message key="sysadmin.serverkey" /></th>
		<th><fmt:message key="sysadmin.servername" /></th>
		<th><fmt:message key="sysadmin.serverdesc" /></th>
		<th><fmt:message key="sysadmin.prefix" /></th>
		<th><fmt:message key="sysadmin.disabled" /></th>
		<th><fmt:message key="admin.actions"/></th>
	</tr>
	
	<c:forEach var="ltiConsumer" items="${ltiConsumers}">
		<tr>
			<td><c:out value="${ltiConsumer.serverid}" /></td>
			<td><c:out value="${ltiConsumer.serverkey}" /></td>
			<td><c:out value="${ltiConsumer.servername}" /></td>
			<td><c:out value="${ltiConsumer.serverdesc}" /></td>
			<td><c:out value="${ltiConsumer.prefix}" /></td>
			<td>
				<c:choose>
					<c:when test="${ltiConsumer.disabled}" >
						<fmt:message key="label.yes" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.no" />
					</c:otherwise>
				</c:choose>
			</td>
			<td>
				<a href="ltiConsumerManagement.do?method=edit&sid=<c:out value='${ltiConsumer.sid}' />">
					<fmt:message key="admin.edit" />
				</a>
				&nbsp;
				<c:choose>
					<c:when test="${ltiConsumer.disabled}">
						<a href="ltiConsumerManagement.do?method=disable&disable=false&sid=<c:out value='${ltiConsumer.sid}' />">
							<fmt:message key="admin.enable" />
						</a>
					</c:when>
					<c:otherwise>
						<a href="ltiConsumerManagement.do?method=disable&disable=true&sid=<c:out value='${ltiConsumer.sid}' />">
							<fmt:message key="admin.disable" />
						</a>
					</c:otherwise>
				</c:choose>
				&nbsp;
				<a href="ltiConsumerManagement.do?method=delete&sid=<c:out value='${ltiConsumer.sid}' />">
					<fmt:message key="admin.delete" />
				</a>
			</td>
		</tr>
	</c:forEach>
</table>

<p>${fn:length(ltiConsumers)} <fmt:message key="label.tool.consumers.count" /></p>
