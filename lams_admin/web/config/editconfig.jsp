<!DOCTYPE html>

<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.config.ConfigurationItem"%>
<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title" value="${sysadmin.config.settings.edit}"/>
	<c:set var="title"><fmt:message key="${title}"/></c:set>
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>/admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" type="text/css" media="screen">
	<script language="JavaScript" type="text/JavaScript" src="<lams:LAMSURL/>/includes/javascript/changeStyle.js"></script>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
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

				<c:if test="${not empty error}">
					<lams:Alert type="danger" id="error-messages" close="false">
							<c:out value="${error}" />
					</lams:Alert>
				</c:if>
				
				<form:form action="save.do" method="post">
				
					<%@ include file="/config/items.jsp"%>
				
					<div class="pull-right">
						<html:cancel cssClass="btn btn-default">
							<fmt:message key="admin.cancel" />
						</html:cancel>
						<input type="submit" id="saveButton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
					</div>
				</form:form>
			</lams:Page>
		</c:when>
		<c:otherwise>
			<lams:Page type="admin" title="${title}" >
				<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

				<c:if test="${not empty error}">
					<lams:Alert type="danger" id="error-messages" close="false">
							<c:out value="${error}" />
					</lams:Alert>
				</c:if>
				
				<form:form action="save.do" method="post">
				
					<%@ include file="/config/items.jsp"%>
				
					<div class="pull-right">
						<html:cancel cssClass="btn btn-default">
							<fmt:message key="admin.cancel" />
						</html:cancel>
						<input type="submit" id="saveButton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
					</div>
				</form:form>
			</lams:Page>
		</c:otherwise>
	</c:choose>


</body>
</lams:html>



