<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.config.ConfigurationItem"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.config.settings.edit"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
</lams:head>
    
<body class="component pb-4 pt-2 px-2 px-sm-4">
	<c:set var="help"><fmt:message key="LAMS+Configuration"/></c:set>
	<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
	
	<%-- Build the breadcrumb --%>
	<c:set var="breadcrumbTop"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
	<c:set var="breadcrumbActive">. | <fmt:message key="sysadmin.config.settings.edit"/></c:set>
	<c:set var="breadcrumbItems" value="${breadcrumbTop}, ${breadcrumbActive}"/>

	
	<lams:Page5 type="admin" title="${title}" titleHelpURL="${help}"  breadcrumbItems="${breadcrumbItems}" formID="configForm" >
	
		<c:if test="${not empty error}">
			<lams:Alert5 type="danger" id="error-messages" close="false">
				<c:out value="${error}" />
			</lams:Alert5>
		</c:if>
		
		<form:form action="config/save.do" modelAttribute="configForm" id="configForm" method="post">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
				
			<c:forEach items="${config}" var="group">
				<div class="row">
					<div class="col text-center h4">
						<fmt:message key="${group.key}"/>
					</div>
				</div>
				<div class="row mb-3">
					<div class="col-8 offset-2">					
						<table class="table table-striped table-bordered bg-white" role="presentation">
							<c:forEach items="${group.value}" var="row">
								<tr>
									<td>
										<label for="${row.key}" class="form-label"><fmt:message key="${row.descriptionKey}"/>
											<c:if test="${row.required}">&nbsp;&nbsp;<span class="text-danger">*</span></c:if>
										</label>
									</td>
									<td class="w-50">
										<input type="hidden" name="key" value="${row.key}"/>
										<c:set var="BOOLEAN"><c:out value="<%= ConfigurationItem.BOOLEAN_FORMAT %>" /></c:set>
										<c:choose>
										<c:when test="${row.key == 'SMTPAuthSecurity'}">
											<select name="value" class="form-control" id="${row.key}">
												<c:forEach items="${smtpAuthTypes}" var="authType">
													<option value="${authType.key}" ${authType.key == row.value ? 'selected="selected"' : '' }>
														${authType.value}
													</option>
												</c:forEach>
											</select>
										</c:when>
										<c:when test="${row.format==BOOLEAN}">
											<select id="${row.key}" name="value" class="form-control">
												<option value="true" ${row.value ? 'selected="selected"' : '' }>true</option>
												<option value="false" ${row.value ? '' : 'selected="selected"' }>false&nbsp;&nbsp;</option>
											</select>
										</c:when>
										<c:when test="${row.format==BOOLEAN}">
											<select id="${row.key}" name="value" class="form-control">
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
					</div>
				</div>
			</c:forEach>
				
			<div class="row">
				<div class="col-8 offset-2 text-end">
					<a href="<lams:LAMSURL/>admin/appadminstart.do" class="btn btn-secondary">
						<fmt:message key="admin.cancel"/>
					</a>
					<button type="submit" id="saveButton" class="btn btn-primary">
						<fmt:message key="admin.save" />
					</button>
				</div>
			</div>
		</form:form>
		
	</lams:Page5>
</body>
</lams:html>
