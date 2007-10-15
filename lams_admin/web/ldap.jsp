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

<h2>
	<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
	: <fmt:message key="sysadmin.ldap.configuration"/></h2>

<lams:help style="no-tabs" page="LDAP+Configuration"/>
<br/>

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
		value='<fmt:message key="label.synchronise" />' 
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
		<li><c:out value="${numSearchResults}"/></li>
		<li><c:out value="${numLdapUsersCreated}"/></li>
		<li><c:out value="${numLdapUsersUpdated}"/></li>
		<li><c:out value="${numLdapUsersDisabled}"/></li>
	</ul>
</c:if>

