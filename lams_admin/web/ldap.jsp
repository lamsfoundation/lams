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

<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title"><fmt:message key="sysadmin.ldap.configuration"/></div>
	</div>

	<div class="panel-body panel-default">
		<c:if test="${not empty config}">
		<html:form action="/config" method="post">
			<html:hidden property="method" value="save"/>
			<tiles:insert attribute="items" />
			<div class="pull-right">
				<html:reset styleClass="btn btn-default"><fmt:message key="admin.reset" /></html:reset>
				<html:cancel styleClass="btn btn-default loffset5"><fmt:message key="admin.cancel" /></html:cancel>
				<html:submit styleClass="btn btn-primary loffset5"><fmt:message key="admin.save" /></html:submit>
			</div>
		</html:form>
		</c:if>
	</div>
</div>

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title"><fmt:message key="heading.ldap.synchronise"/></div>
	</div>

	<div class="panel-body panel-default">
		<p><fmt:message key="msg.ldap.synchronise.intro"/></p>
		<p><fmt:message key="msg.ldap.synchronise.warning"/></p>
		
		<c:if test="${not empty numLdapUsersMsg}">
			<p><c:out value="${numLdapUsersMsg}"/></p>
		</c:if>
		
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
			<h4><c:out value="${done}"/></h4>
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
		
		<input class="btn btn-primary pull-right" value="<fmt:message key="label.synchronise" />" onclick='startSync();'/>
		
	</div>
</div>
