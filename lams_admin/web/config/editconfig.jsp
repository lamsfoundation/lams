<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.config.ConfigurationItem"%>
<%@ include file="/taglibs.jsp"%>

<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

<c:if test="${not empty error}">
	<lams:Alert type="danger" id="error-messages" close="false">
			<c:out value="${error}" />
	</lams:Alert>
</c:if>

<html:form action="/config" method="post">
	<html:hidden property="method" value="save" />

	<tiles:insert attribute="items" />

	<div class="pull-right">
		<html:cancel styleClass="btn btn-default">
			<fmt:message key="admin.cancel" />
		</html:cancel>
		<html:submit styleId="saveButton" styleClass="btn btn-primary loffset5">
			<fmt:message key="admin.save" />
		</html:submit>
	</div>
</html:form>
