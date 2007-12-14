<%@ include file="/taglibs.jsp"%>

<h4><a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a></h4>
<h1><fmt:message key="sysadmin.maintain.external.servers" /></h1>

<p class="align-right"><input class="button" type="button" value="<fmt:message key='sysadmin.server.add' />" onClick="javascript:document.location='servermaintain.do?method=edit'" /></p>
<table class=alternative-color width=100%>
	<tr>
		<th><fmt:message key="sysadmin.serverid" /></th>
		<th><fmt:message key="sysadmin.serverkey" /></th>
		<th><fmt:message key="sysadmin.servername" /></th>
		<th><fmt:message key="sysadmin.serverdesc" /></th>
		<th><fmt:message key="sysadmin.prefix" /></th>
		<th><fmt:message key="sysadmin.disabled" /></th>
		<th><fmt:message key="sysadmin.organisation" /></th>
	</tr>
	<c:forEach items="${servers}" var="serverOrgMap">
	<tr>
		<td><c:out value="${serverOrgMap.serverid}" /></td>
		<td><c:out value="${serverOrgMap.serverkey}" /></td>
		<td><c:out value="${serverOrgMap.servername}" /></td>
		<td><c:out value="${serverOrgMap.serverdesc}" /></td>
		<td><c:out value="${serverOrgMap.prefix}" /></td>
		<td>
			<c:choose>
			<c:when test="${serverOrgMap.disabled}" >
				<fmt:message key="label.yes" />
			</c:when>
			<c:otherwise>
				<fmt:message key="label.no" />
			</c:otherwise>
			</c:choose>
		</td>
		<td><c:out value="${serverOrgMap.organisation.name}" /></td>
	</tr>
	<tr>	
		<td colspan="7">
			<a href="servermaintain.do?method=edit&sid=<c:out value='${serverOrgMap.sid}' />"><fmt:message key="admin.edit" /></a>
			&nbsp;
			<c:choose>
				<c:when test="${serverOrgMap.disabled}">
					<a href="servermaintain.do?method=enable&sid=<c:out value='${serverOrgMap.sid}' />"><fmt:message key="admin.enable" /></a>
				</c:when>
				<c:otherwise>
					<a href="servermaintain.do?method=disable&sid=<c:out value='${serverOrgMap.sid}' />"><fmt:message key="admin.disable" /></a>
				</c:otherwise>
			</c:choose>
			&nbsp;
			<a href="servermaintain.do?method=delete&sid=<c:out value='${serverOrgMap.sid}' />"><fmt:message key="admin.delete" /></a>
		</td>
	</tr>
	</c:forEach>
</table>
<p>${fn:length(servers)} <fmt:message key="sysadmin.integrated.servers" /></p>