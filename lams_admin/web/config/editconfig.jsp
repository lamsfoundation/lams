<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.config.ConfigurationItem"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.config.settings.edit"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
</lams:head>
    
<body class="stripes">
	<c:set var="help"><fmt:message key="LAMS+Configuration"/></c:set>
	<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
	
	<%-- Build the breadcrumb --%>
	<c:set var="breadcrumbTop"><lams:LAMSURL/>admin/sysadminstart.do | <fmt:message key="sysadmin.maintain" /></c:set>
	<c:set var="breadcrumbActive">. | <fmt:message key="sysadmin.config.settings.edit"/></c:set>
	<c:set var="breadcrumbItems" value="${breadcrumbTop}, ${breadcrumbActive}"/>

	
	<lams:Page type="admin" title="${title}" titleHelpURL="${help}"  breadcrumbItems="${breadcrumbItems}" formID="configForm" >
	
		<c:if test="${not empty error}">
			<lams:Alert type="danger" id="error-messages" close="false">
				<c:out value="${error}" />
			</lams:Alert>
		</c:if>
		<form:form action="config/save.do" modelAttribute="configForm" id="configForm" method="post">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
				
			<c:forEach items="${config}" var="group">
				<h2 class="voffset20"><fmt:message key="${group.key}"/></h2>

				<table class="table table-striped table-bordered" role="presentation">
					<c:forEach items="${group.value}" var="row">
						<tr>
							<td>
								<label for="${row.key}"><fmt:message key="${row.descriptionKey}"/></label>
								<c:if test="${row.required}">&nbsp;&nbsp;<span class="text-danger">*</span></c:if>
							</td>
							<td>
								<input type="hidden" name="key" value="${row.key}"/>
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
									<select id="${row.key}" name="value" class="form-control form-control-sm">
										<option value="true" ${row.value ? 'selected="selected"' : '' }>true</option>
										<option value="false" ${row.value ? '' : 'selected="selected"' }>false&nbsp;&nbsp;</option>
									</select>
								</c:when>
								<c:when test="${row.format==BOOLEAN}">
									<select id="${row.key}" name="value" class="form-control form-control-sm">
										<option value="true" ${row.value ? 'selected="selected"' : '' }>true</option>
										<option value="false" ${row.value ? '' : 'selected="selected"' }>false&nbsp;&nbsp;</option>
									</select>
								</c:when>
								<c:otherwise>
									<input id="${row.key}" type="text" id="${row.key}" name="value" value="${row.value}" size="50" maxlength="255" class="form-control" <c:if test="${row.required}">required</c:if>/>
								</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</table>
			</c:forEach>
				
			<div class="pull-right">
				<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-outline-secondary btn-sm">
					<fmt:message key="admin.cancel"/>
				</a>
				<input type="submit" id="saveButton" class="btn btn-primary btn-sm loffset5" value="<fmt:message key="admin.save" />" />
			</div>
		</form:form>
		
	</lams:Page>
</body>
</lams:html>
