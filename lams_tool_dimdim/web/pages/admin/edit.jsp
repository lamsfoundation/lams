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

	<table>
		<c:choose>
			<c:when test="${allowVersionChange}">
				<tr>
					<td><fmt:message key="config.version" /></td>
					<td><html:select property="version">
						<html:option value="">
							<fmt:message key="label.version.pleaseSelect" />
						</html:option>
						<html:option value="standard">
							<fmt:message key="label.version.standard" />
						</html:option>
						<html:option value="enterprise">
							<fmt:message key="label.version.enterprise" />
						</html:option>
					</html:select></td>
				</tr>
			</c:when>
			<c:otherwise>
				<html:hidden property="version" />
			</c:otherwise>
		</c:choose>

		<tr>
			<td><fmt:message key="config.serverURL" /></td>
			<td><html:text property="serverURL" /></td>
		</tr>
	</table>

	<div class="align-right"><html:submit styleClass="button">
		<fmt:message key="label.save" />
	</html:submit> <html:cancel styleClass="button">
		<fmt:message key="label.cancel" />
	</html:cancel></div>
</html:form></div>

