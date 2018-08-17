<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.web.session.SessionManager" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.service.ILdapService" %>

<lams:html>
<lams:head>
	<c:set var="title"><tiles:getAsString name="title"/></c:set>
	<c:set var="title"><fmt:message key="${title}"/></c:set>
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>/admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" type="text/css" media="screen">
	<script language="JavaScript" type="text/JavaScript" src="<lams:LAMSURL/>/includes/javascript/changeStyle.js"></script>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	
	<script language="javascript" type="text/JavaScript">
	<% if (SessionManager.getSession().getAttribute(ILdapService.SYNC_RESULTS) != null) { %>
		document.location = 'ldap/results.do';
	<% } %>
	function startSync(){
		document.location='ldap/sync.do';
	}
</script>
	
</lams:head>
    
<body class="stripes">
	<c:set var="subtitle"><tiles:getAsString name="subtitle" ignore="true"/></c:set>	
	<c:if test="${not empty subtitle}">
		<c:set var="title">${title}: <fmt:message key="${subtitle}"/></c:set>
	</c:if>
	
	<c:set var="help"><tiles:getAsString name='help'  ignore="true"/></c:set>
	<c:choose>
		<c:when test="${not empty help}">
			<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
			<lams:Page type="admin" title="${title}" titleHelpURL="${help}">
				<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="panel-title"><fmt:message key="sysadmin.ldap.configuration"/></div>
					</div>
				
					<div class="panel-body panel-default">
						<c:if test="${not empty config}">
						<form:form action="save.do" modelAttribute="configForm" id="configForm" method="post">
							<%@ include file="/config/items.jsp"%>
							<div class="pull-right">
								<input type="reset" class="btn btn-default" value="<fmt:message key="admin.reset" />" />
								<html:cancel styleClass="btn btn-default loffset5"><fmt:message key="admin.cancel" /></html:cancel>
								<input type="submit" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
							</div>
						</form:form>
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
									document.location = 'ldap/waiting.do';
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
			</lams:Page>
		</c:when>
		<c:otherwise>
			<lams:Page type="admin" title="${title}" >
				<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="panel-title"><fmt:message key="sysadmin.ldap.configuration"/></div>
					</div>
				
					<div class="panel-body panel-default">
						<c:if test="${not empty config}">
						<form:form action="save.do" modelAttribute="configForm" id="configForm" method="post">
							<%@ include file="/config/items.jsp"%>
							<div class="pull-right">
								<input type="reset" class="btn btn-default" value="<fmt:message key="admin.reset" />" />
								<html:cancel styleClass="btn btn-default loffset5"><fmt:message key="admin.cancel" /></html:cancel>
								<input type="submit" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
							</div>
						</form:form>
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
									document.location = 'ldap/waiting.do';
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
			</lams:Page>
		</c:otherwise>
	</c:choose>


</body>
</lams:html>




