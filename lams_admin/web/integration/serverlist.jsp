<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="appadmin.maintain.external.servers"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
	<script>
		function deleteIntegratedServer() {
			return confirm('<fmt:message key="sysadmin.server.delete" />');
		}
	</script>
</lams:head>
    
<body class="component pb-4 pt-2">
	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbTop"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
	<c:set var="breadcrumbActive">. | <fmt:message key="appadmin.maintain.external.servers"/></c:set>
	<c:set var="breadcrumbItems" value="${breadcrumbTop}, ${breadcrumbActive}"/>

	<lams:Page5 type="admin" title="${title}" breadcrumbItems="${breadcrumbItems}">
		<div class="row">
			<div class="col-8 offset-2">
				<table class="table table-striped table-bordered">
					<thead>
						<tr>
							<th><fmt:message key="sysadmin.servername" /></th>
							<th><fmt:message key="sysadmin.serverid" /></th>
							<th class="d-none d-lg-table-cell"><fmt:message key="sysadmin.serverdesc" /></th>
							<th><fmt:message key="sysadmin.prefix" /></th>
							<th><fmt:message key="admin.actions"/></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${servers}" var="server">
							<tr <c:if test="${server.disabled}">class="table-danger ${signupOrganisation.disabled}"</c:if> >
								<td>
									<a href="<lams:LAMSURL/>admin/extserver/edit.do?sid=${server.sid}" title="<fmt:message key="admin.edit" />" class="fw-bold text-decoration-none">
										<c:out value="${server.servername}" escapeXml="true"/>
									</a>
									<c:choose>
									<c:when test="${server.disabled}" >
											</br><span class="badge bg-warning text-black"><fmt:message key="sysadmin.disabled" /></span>
									</c:when>
									<c:otherwise>
											</br>
											<a class="btn btn-sm btn-outline-secondary py-0 px-1" title="<fmt:message key="sysadmin.serverkey" />" href="javascript:alert('<fmt:message key="sysadmin.serverkey" />: <c:out value="${server.serverkey}" escapeXml="true"/>');">
												<i title="<fmt:message key="sysadmin.serverkey" />" class="fa fa-key font-weight-bolder "></i>
											</a>
									</c:otherwise>
									</c:choose>
								</td>
								<td><c:out value="${server.serverid}"  escapeXml="true"/></td>
								<td class="d-none d-lg-table-cell" style="word-wrap: break-word;min-width: 410px;max-width: 410px;"><span class="d-inline-block text-truncate" style="max-width: 410px;"><c:out value="${server.serverdesc}" escapeXml="true"/></span></td>
								<td><c:out value="${server.prefix}" /></td>
								<td class="text-center">
									<a style="display: inline-block;" class="btn btn-primary" id="edit_<c:out value='${server.serverid}' escapeXml="true"/>" href="<lams:LAMSURL/>admin/extserver/edit.do?sid=${server.sid}" title="<fmt:message key="admin.edit" />">
										<i class="fa fa-pencil"></i>
									</a>
									&nbsp;
									<c:choose>
										<c:when test="${server.disabled}">
			                                <csrf:form style="display: inline-block;" id="enable_${server.serverid}" method="post" action="/lams/admin/extserver/enable.do">
			                                	<input type="hidden" name="sid" value="${server.sid}"/>
			                                		<button type="submit" class="btn btn-success" title="<fmt:message key="admin.enable" />">
			                                		<i class="fa fa-power-off"></i>
			                                		</button>
			                                </csrf:form>
										</c:when>
										<c:otherwise>
			                                <csrf:form style="display: inline-block;" id="disable_${server.serverid}" method="post" action="/lams/admin/extserver/disable.do">
			                                	<input type="hidden" name="sid" value="${server.sid}"/>
			                                	<button type="submit" class="btn btn-secondary" title="<fmt:message key="admin.disable" />">
			                                		<i class="fa fa-pause"></i>
			                                	</button></csrf:form>
										</c:otherwise>
									</c:choose>
									&nbsp;
			                        <csrf:form id="delete_${server.serverid}" style="display: inline-block;" method="post" action="/lams/admin/extserver/delete.do" onSubmit="javascript:return deleteIntegratedServer()">
			                        	<input type="hidden" name="sid" value="${server.sid}"/>
			                        	<button type="submit" class="btn btn-danger" title="<fmt:message key="admin.delete" />"/>
			                        		<i class="fa fa-trash"></i>
			                        	</button>
			                        </csrf:form>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		
		<div class="row">
			<div class="col-8 offset-2">
				${fn:length(servers)}&nbsp;<fmt:message key="sysadmin.integrated.servers" />
			</div>
		</div>
		
		<div class="row">
			<div class="col-8 offset-2 text-end">
				<a href="<lams:LAMSURL/>admin/appadminstart.do" class="btn btn-secondary">
					<fmt:message key="admin.cancel"/>
				</a>

				<input class="btn btn-primary" name="addnewserver" type="button" value="<fmt:message key='sysadmin.server.add' />" onClick="javascript:document.location='edit.do'" />
			</div>
		</div>
	</lams:Page5>
</body>
</lams:html>
