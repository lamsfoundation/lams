<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<%-- Build breadcrumb --%>
<c:set var="breadcrumbTop"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
<c:set var="breadcrumbActive">. | <fmt:message key="label.manage.tool.consumers"/></c:set>
<c:set var="breadcrumbItems" value="${breadcrumbTop}, ${breadcrumbActive}"/>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="label.manage.tool.consumers"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
	<script>
		function deleteConsumer() {
			return confirm('<fmt:message key="label.manage.tool.consumers.delete" />');
		}
	</script>
</lams:head>

	<lams:PageAdmin title="${title}" breadcrumbItems="${breadcrumbItems}">
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
						<c:forEach var="ltiConsumer" items="${ltiConsumers}">
							<tr <c:if test="${ltiConsumer.disabled}">class="table-danger ${signupOrganisation.disabled}"</c:if> >
								<td>
									<a href="../ltiConsumerManagement/edit.do?sid=<c:out value='${ltiConsumer.sid}' escapeXml='true'/>" title="<fmt:message key="admin.edit" />" class="fw-bold text-decoration-none">
										<c:out value="${ltiConsumer.servername}" escapeXml="true"/>
									</a>
									</br>
									<c:choose>
										<c:when test="${ltiConsumer.disabled}" >
											<span class="badge bg-warning text-black"><fmt:message key="sysadmin.disabled" /></span>
										</c:when>
										<c:otherwise>
											<a class="btn btn-sm btn-outline-secondary py-0 px-1" title="<fmt:message key="sysadmin.serverkey" />" href="javascript:alert('<fmt:message key="sysadmin.serverkey" />: <c:out value="${ltiConsumer.serverkey}" escapeXml="true"/>');">
												<i title="<fmt:message key="sysadmin.serverkey" />" class="fa fa-key font-weight-bolder "></i>
											</a>
										</c:otherwise>
									</c:choose>
								</td>
								<td><c:out value="${ltiConsumer.serverid}" escapeXml="true"/></td>
								<td class="d-none d-lg-table-cell"style="word-wrap: break-word;min-width: 410px;max-width: 410px;"><span class="d-inline-block text-truncate" style="max-width: 410px;"><c:out value="${ltiConsumer.serverdesc}" escapeXml="true"/></span></td>
								<td><c:out value="${ltiConsumer.prefix}" /></td>
								<td class="text-center">
									<a style="display: inline-block;" title="<fmt:message key="admin.edit" />" class="btn btn-primary" id="edit_<c:out value='${ltiConsumer.sid}' escapeXml="true"/>" href="../ltiConsumerManagement/edit.do?sid=<c:out value='${ltiConsumer.sid}' escapeXml='true'/>">
										<i class="fa fa-pencil"></i>
									</a>
									&nbsp;
									<c:choose>
										<c:when test="${ltiConsumer.disabled}">
									<csrf:form style="display: inline-block;" id="enable_${ltiConsumer.sid}" method="post" action="/lams/admin/ltiConsumerManagement/enable.do">
										<input type="hidden" name="sid" value="${ltiConsumer.sid}"/>
										<input type="hidden" name="disable" value="false"/>
										<button type="submit" class="btn btn-outline-primary btn-sm" title="<fmt:message key="admin.enable" />"/>
	                                		<i class="fa fa-power-off"></i>
	                                	</button>
									</csrf:form>
										</c:when>
										<c:otherwise>
									<csrf:form style="display: inline-block;" id="disable_${ltiConsumer.sid}" method="post" action="/lams/admin/ltiConsumerManagement/disable.do">
										<input type="hidden" name="sid" value="${ltiConsumer.sid}"/>
										<input type="hidden" name="disable" value="true"/>
										<button type="submit" class="btn btn-secondary" title="<fmt:message key="admin.disable" />"/>
											<i class="fa fa-pause"></i>
										</button>
									</csrf:form>
										</c:otherwise>
									</c:choose>
									&nbsp;
			                        <csrf:form style="display: inline-block;" id="delete_${ltiConsumer.sid}" method="post" action="/lams/admin/ltiConsumerManagement/delete.do" onSubmit="javascript:return deleteConsumer()">
			                        	<input type="hidden" name="sid" value="${ltiConsumer.sid}"/>
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
				${fn:length(ltiConsumers)}&nbsp;<fmt:message key="label.tool.consumers.count" />
			</div>
		</div>
		
		<div class="row">
			<div class="col-8 offset-2 text-end">
				<a href="<lams:LAMSURL/>admin/appadminstart.do" class="btn btn-secondary">
					<fmt:message key="admin.cancel"/>
				</a>
				<input class="btn btn-primary" name="addNewLtiConsumer" type="button" value="<fmt:message key='label.add.tool.consumer' />" 
						onClick="javascript:document.location='../ltiConsumerManagement/edit.do'" />
			</div>
		</div>
	</lams:PageAdmin>
</lams:html>
