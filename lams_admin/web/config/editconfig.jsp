<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<html:form action="/config" method="post">
	<html:hidden property="method" value="save"/>
	<h2 align="left">
		<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a> :  
		<fmt:message key="sysadmin.config.settings.edit" />
	</h2>
	<lams:help page="<%= Configuration.CONFIGURATION_HELP_PAGE %>"/>
	<br/>
	<table class="alternative-color" width=100%>
		<tr>
			<th><fmt:message key="admin.config.key"/></th>
			<th><fmt:message key="admin.config.value"/></th>
		</tr>
		<c:forEach var="itemEntry" items="${config}">
			<tr>
				<td>
					<c:out value="${itemEntry.key}"/>
				</td>
				<td>
					<html:hidden property="cKey" name="cKey" value="${itemEntry.key}"/>
					<html:text property="cValue" name="cValue" value="${itemEntry.value.value}" size="50"/>
					<BR>
				</td>
			</tr>
		</c:forEach>
	</table>
	<p align="center">
		<html:submit><fmt:message key="admin.save" /></html:submit>
		<html:reset><fmt:message key="admin.reset" /></html:reset>
		<html:cancel><fmt:message key="admin.cancel" /></html:cancel>
	</p>
</html:form>