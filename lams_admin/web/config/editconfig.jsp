<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.config.ConfigurationItem" %>
<%@ include file="/taglibs.jsp"%>

<h2 class="align-left">
		<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a> :  
		<fmt:message key="sysadmin.config.settings.edit" />
	</h2>
	<lams:help style="no-tabs" page="<%= Configuration.CONFIGURATION_HELP_PAGE %>"/>
	<br/>

<div align="center">
<c:if test="${not empty error}">
	<p class='warning'><c:out value="${error}"/></p>
</c:if>
</div>

<html:form action="/config" method="post">
	<html:hidden property="method" value="save"/>
	
	<logic:iterate name="config" id="group">
		<h2 align="center"><fmt:message key="${group.key}"/></h2>
		<table class="alternative-color" width=100% cellspacing="0">
		<logic:iterate name="group" property="value" id="row">
			<tr>
				<td align="right" width="50%">
					<fmt:message key="${row.descriptionKey}"/>
					<c:if test="${row.required}">&nbsp;&nbsp;*</c:if>
				</td>
				<td>
					<html:hidden property="key" name="key" value="${row.key}"/>
					<c:set var="BOOLEAN"><%= ConfigurationItem.BOOLEAN_FORMAT %></c:set>
					<c:choose>
					<c:when test="${row.format==BOOLEAN}">
						<html:select name="row" property="value">
						<html:option value="true">true</html:option>
						<html:option value="false">false&nbsp;&nbsp;</html:option>
						</html:select>
					</c:when>
					<c:otherwise>
						<html:text property="value" name="row" value="${row.value}" size="50" maxlength="255"/>
					</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</logic:iterate>
		</table>
	</logic:iterate>
	
	<p align="center">
		<html:submit styleClass="button"><fmt:message key="admin.save" /></html:submit>
		<html:reset styleClass="button"><fmt:message key="admin.reset" /></html:reset>
		<html:cancel styleClass="button"><fmt:message key="admin.cancel" /></html:cancel>
	</p>
</html:form>