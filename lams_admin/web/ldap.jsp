<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.web.session.SessionManager" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.service.ILdapService" %>

<script language="javascript" type="text/JavaScript">
	<% if (SessionManager.getSession().getAttribute(ILdapService.SYNC_RESULTS) != null) { %>
		document.location = 'ldap.do?action=results';
	<% } %>
	function startSync(){
		document.location='ldap.do?action=sync';
	}
</script>

<h4><a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a></h4>
<lams:help style="no-tabs" page="LDAP+Configuration"/>
<h1><fmt:message key="sysadmin.ldap.configuration"/></h1>

<c:if test="${not empty config}">
<html:form action="/config" method="post">
	<html:hidden property="method" value="save"/>
	<tiles:insert attribute="items" />
	<p align="center">
		<html:submit styleClass="button"><fmt:message key="admin.save" /></html:submit>
		<html:reset styleClass="button"><fmt:message key="admin.reset" /></html:reset>
		<html:cancel styleClass="button"><fmt:message key="admin.cancel" /></html:cancel>
	</p>
</html:form>
</c:if>

<h3><fmt:message key="heading.ldap.synchronise"/></h3>
<p>
<fmt:message key="msg.ldap.synchronise.intro"/>
</p>
<p>
<fmt:message key="msg.ldap.synchronise.warning"/>
</p>

<c:if test="${not empty numLdapUsersMsg}">
	<p><c:out value="${numLdapUsersMsg}"/></p>
</c:if>

<p>
	<input 
		class="button" 
		type="button" 
		value="<fmt:message key="label.synchronise" />" 
		onclick='startSync();'
	/>
</p>

<p>&nbsp;</p>

<c:if test="${not empty wait}">
	<p><c:out value="${wait}"/></p>
	<script language="javascript" type="text/javascript">
		function refresh() {
			document.location = 'ldap.do?action=waiting';
		}
		window.setInterval("refresh()",5000);
	</script>
</c:if>

<c:if test="${not empty done}">
	<h3><c:out value="${done}"/></h3>
	<ul>
		<c:if test="${not empty messages}">
		<li>
			<fmt:message key="msg.ldap.synchronise.errors"/>
			<ul>
				<c:forEach items="${messages}" var="message">
					<li><c:out value="${message}"/></li>
				</c:forEach>
			</ul>
		</li>
		</c:if>
		<c:if test="${not empty numSearchResults}">
		<li><c:out value="${numSearchResults}"/></li>
		</c:if>
		<c:if test="${not empty numLdapUsersCreated}">
		<li><c:out value="${numLdapUsersCreated}"/></li>
		</c:if>
		<c:if test="${not empty numLdapUsersUpdated}">
		<li><c:out value="${numLdapUsersUpdated}"/></li>
		</c:if>
		<c:if test="${not empty numLdapUsersDisabled}">
		<li><c:out value="${numLdapUsersDisabled}"/></li>
		</c:if>
	</ul>
</c:if>

