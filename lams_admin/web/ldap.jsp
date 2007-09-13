<%@ include file="/taglibs.jsp"%>

<h2>
	<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
	: <fmt:message key="sysadmin.ldap.configuration"/></h2>
<p>&nbsp;</p>

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

<c:if test="${not empty done}">
	<p><c:out value="${done}"/></p>
</c:if>

<p>
	<input 
		class="button" 
		type="button" 
		value='<fmt:message key="label.synchronise" />' 
		onclick=javascript:document.location='ldap.do?action=sync'
	/>
</p>
