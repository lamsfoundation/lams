<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.config.ConfigurationItem" %>
<%@ page import="org.lamsfoundation.lams.web.session.SessionManager" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.service.ILdapService" %>

<script type="text/JavaScript">
	<% if (SessionManager.getSession().getAttribute(ILdapService.SYNC_RESULTS) != null) { %>
		document.location = 'results.do';
	<% } %>
	function startSync(){
		document.location='sync.do';
	}
</script>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.ldap.configuration"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
</lams:head>
    
<body class="stripes">
	
	<c:set var="help"><fmt:message key="LDAP+Configuration"/></c:set>
	<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
	<lams:Page5 type="admin" title="${title}" titleHelpURL="${help}">
		
		<p>
			<a href="<lams:LAMSURL/>admin/appadminstart.do" class="btn btn-default"><fmt:message key="appadmin.maintain" /></a>
		</p>

		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="panel-title"><fmt:message key="sysadmin.ldap.configuration"/></div>
			</div>
				
			<div class="panel-body panel-default">
				<c:if test="${not empty config}">
					<form:form action="../config/save.do" modelAttribute="configForm" id="configForm" method="post">
						<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
						
						<c:forEach items="${config}" var="group">
							<div class="panel panel-default">
								<div class="panel-heading">
									<div class="panel-title"><fmt:message key="${group.key}"/></div>
								</div>
												
								<table class="table table-striped table-condensed" >
									<c:forEach items="${group.value}" var="row">
										<tr>
											<td>
												<fmt:message key="${row.descriptionKey}"/>
												<c:if test="${row.required}">&nbsp;&nbsp;*</c:if>
											</td>
											<td>
												<form:hidden path="key" value="${row.key}"/>
												<c:set var="BOOLEAN"><c:out value="<%= ConfigurationItem.BOOLEAN_FORMAT %>" /></c:set>
												<c:choose>
												<c:when test="${row.key == 'SMTPAuthSecurity'}">
													<select name="value" class="form-control form-control-sm" id="${row.key}">
														<c:forEach items="${smtpAuthTypes}" var="authType">
															<option value="${authType.key}" ${authType.key == row.value ? 'selected="selected"' : '' }>
																${authType.value}
															</option>
														</c:forEach>
													</select>
												</c:when>
												<c:when test="${row.format==BOOLEAN}">
													<select name="value" class="form-control form-control-sm">
														<option value="true" ${row.value ? 'selected="selected"' : '' }>true</option>
														<option value="false" ${row.value ? '' : 'selected="selected"' }>false&nbsp;&nbsp;</option>
													</select>
												</c:when>
												<c:when test="${row.format==BOOLEAN}">
													<select name="value" class="form-control form-control-sm">
														<option value="true" ${row.value ? 'selected="selected"' : '' }>true</option>
														<option value="false" ${row.value ? '' : 'selected="selected"' }>false&nbsp;&nbsp;</option>
													</select>
												</c:when>
												<c:otherwise>
													<form:input id="${row.key}" path="value" value="${row.value}" size="50" maxlength="255" cssClass="form-control"/>
												</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:forEach>
								</table>
							</div>
						</c:forEach>
					
						<div class="pull-right">
							<input type="reset" class="btn btn-default" value="<fmt:message key="admin.reset" />" />
							<a href="<lams:LAMSURL/>admin/appadminstart.do" class="btn btn-default loffset5"><fmt:message key="admin.cancel"/></a>
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
							<script type="text/javascript">
								function refresh() {
									document.location = 'waiting.do';
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
						
			<input type="button" class="btn btn-primary pull-right" value="<fmt:message key="label.synchronise" />" onclick='startSync();'/>
						
			</div>
		</div>				
	</lams:Page5>

</body>
</lams:html>




