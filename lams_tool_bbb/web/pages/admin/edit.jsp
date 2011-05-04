<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<div id="content">

<h1><fmt:message key="admin.title" /></h1>

<logic:messagesPresent>
	<p class="warning"><html:messages id="error">
		<c:out value="${error}" escapeXml="false" />
		<br />
	</html:messages></p>
</logic:messagesPresent> <html:form action="/admin/save">

<div class="warning">
	<fmt:message key="config.securitySalt.notice" />
	<p>
	<fmt:message key="config.securitySalt.notice2" />
	</p>
</div>

	<table>
		<tr>
			<td><fmt:message key="config.serverURL" /></td>
			<td><html:text property="serverURL" size="50"/></td>
		</tr>
		<tr>
			<td><fmt:message key="config.securitySalt"/></td>
			<td><html:text property="securitySalt" size="50" /></td>
		</tr>	
	</table>

	<div class="align-right"><html:submit styleClass="button">
		<fmt:message key="label.save" />
	</html:submit> <html:cancel styleClass="button">
		<fmt:message key="label.cancel" />
	</html:cancel></div>
</html:form></div>

