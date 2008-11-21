<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<script type="text/javascript">
	window.onload = function() {
		onSelectChange();

		var version = document.forms[0].version.value;
	}

	function onSelectChange() {
		var version = document.forms[0].version.value;

		if (version == 'enterprise') {
			document.forms[0].standardServerURL.disabled = "disabled";
			document.forms[0].enterpriseServerURL.disabled = "";
			document.forms[0].adminPassword.disabled = "";
		} else if (version == 'standard') {
			document.forms[0].standardServerURL.disabled = "";
			document.forms[0].enterpriseServerURL.disabled = "disabled";
			document.forms[0].adminPassword.disabled = "disabled";
		}
	}
</script>

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
					<td><html:select property="version"
						onchange="onSelectChange()">
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
			<td><fmt:message key="config.standardServerURL" /></td>
			<td><html:text property="standardServerURL" /></td>
		</tr>
		<tr>
			<td><fmt:message key="config.enterpriseServerURL" /></td>
			<td><html:text property="enterpriseServerURL" /></td>
		</tr>
		<tr>
			<td><fmt:message key="config.adminPassword" /></td>
			<td><html:text property="adminPassword" /></td>
		</tr>

	</table>

	<div class="align-right"><html:submit styleClass="button">
		<fmt:message key="label.save" />
	</html:submit> <html:cancel styleClass="button">
		<fmt:message key="label.cancel" />
	</html:cancel></div>
</html:form></div>

