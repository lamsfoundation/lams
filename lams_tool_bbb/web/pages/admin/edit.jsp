<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<c:set var="title"><fmt:message key="admin.title" /></c:set>
<lams:Page type="admin" title="${title}">

	<logic:messagesPresent>
		<lams:Alert type="danger" id="form-error" close="false">
		<html:messages id="error">
			<c:out value="${error}" escapeXml="false" />
			<br />
		</html:messages>
		</lams:Alert>
	</logic:messagesPresent> 
	
	<html:form action="/admin/save">

		<lams:Alert type="warn" id="form-warn" close="false">
			<fmt:message key="config.securitySalt.notice" />
			<p>
			<fmt:message key="config.securitySalt.notice2" />
			</p>
		</lams:Alert>
	
		<table class="table table-no-border">
			<tr>
				<td width="25%"><fmt:message key="config.serverURL" /></td>
				<td><html:text property="serverURL" size="50" styleClass="form-control"/></td>
			</tr>
			<tr>
				<td><fmt:message key="config.securitySalt"/></td>
				<td><html:text property="securitySalt" size="50"  styleClass="form-control"/></td>
			</tr>	
		</table>
	
		<div class="pull-right">
			<html:cancel styleClass="btn btn-default"><fmt:message key="label.cancel" /></html:cancel>
			<html:submit styleClass="btn btn-primary loffset5"><fmt:message key="label.save" /></html:submit> 
		</div>

	</html:form>
</lams:Page>
