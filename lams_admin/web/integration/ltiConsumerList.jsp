<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="label.manage.tool.consumers"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	<script>
		function deleteConsumer() {
			return confirm('<fmt:message key="label.manage.tool.consumers.delete" />');
		}
	</script>

</lams:head>
    
<body class="stripes">
	<lams:Page type="admin" title="${title}">

		<nav aria-label="breadcrumb" role="navigation">
		  <ol class="breadcrumb">
		    <li class="breadcrumb-item">
		    	<a href="<lams:LAMSURL/>admin/sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
		    </li>
		    <li class="breadcrumb-item active" aria-current="page"><fmt:message key="label.manage.tool.consumers"/></li>
		  </ol>
		</nav>
				
		<table class="table table-striped table-bordered">
			<thead class="thead-light">
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
						<a href="../ltiConsumerManagement/edit.do?sid=<c:out value='${ltiConsumer.sid}' escapeXml='true'/>" title="<fmt:message key="admin.edit" />" class="font-weight-bold">
							<c:out value="${ltiConsumer.servername}" escapeXml="true"/>
						</a>
						<c:choose>
							<c:when test="${ltiConsumer.disabled}" >
								</br><span class="badge badge-warning"><fmt:message key="sysadmin.disabled" /></span>
							</c:when>
							<c:otherwise>
								</br>
								<a class="btn btn-sm btn-outline-secondary py-0 px-1" style="font-size: 0.8em;" title="<fmt:message key="sysadmin.serverkey" />" href="javascript:alert('<fmt:message key="sysadmin.serverkey" />: <c:out value="${ltiConsumer.serverkey}" escapeXml="true"/>');">
									<i title="<fmt:message key="sysadmin.serverkey" />" class="fa fa-key font-weight-bolder "></i>
								</a>
							</c:otherwise>
						</c:choose>						
					</td>
					<td><c:out value="${ltiConsumer.serverid}" escapeXml="true"/></td>
					<td class="d-none d-lg-table-cell"style="word-wrap: break-word;min-width: 410px;max-width: 410px;"><span class="d-inline-block text-truncate" style="max-width: 410px;"><c:out value="${ltiConsumer.serverdesc}" escapeXml="true"/></span></td>
					<td><c:out value="${ltiConsumer.prefix}" /></td>
					<td class="text-center">
						<a style="display: inline-block;" title="<fmt:message key="admin.edit" />" class="btn btn-outline-primary btn-sm" id="edit_<c:out value='${ltiConsumer.sid}' escapeXml="true"/>" href="../ltiConsumerManagement/edit.do?sid=<c:out value='${ltiConsumer.sid}' escapeXml='true'/>">
							<i class="fa fa-pencil"></i>
						</a>
						&nbsp;
						<c:choose>
							<c:when test="${ltiConsumer.disabled}">
								<csrf:form style="display: inline-block;" id="enable_${ltiConsumer.sid}" method="post" action="/lams/admin/ltiConsumerManagement/disable.do">
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
									<button type="submit" class="btn btn-outline-secondary btn-sm" title="<fmt:message key="admin.disable" />"/>
										<i class="fa fa-pause"></i>
									</button>
								</csrf:form>
							</c:otherwise>
						</c:choose>
						&nbsp;
                        <csrf:form style="display: inline-block;" id="delete_${ltiConsumer.sid}" method="post" action="/lams/admin/ltiConsumerManagement/delete.do" onSubmit="javascript:return deleteConsumer()">
                        	<input type="hidden" name="sid" value="${ltiConsumer.sid}"/>
                        	<button type="submit" class="btn btn-outline-danger btn-sm" title="<fmt:message key="admin.delete" />"/>
                        		<i class="fa fa-trash"></i>
                        	</button>
                       	</csrf:form>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
				
		<p>${fn:length(ltiConsumers)}&nbsp;<fmt:message key="label.tool.consumers.count" /></p>

		<div class="pull-right">
				<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-outline-secondary btn-sm">
					<fmt:message key="admin.cancel"/>
				</a>
			<input class="btn btn-primary btn-sm loffset5" name="addNewLtiConsumer" type="button" value="<fmt:message key='label.add.tool.consumer' />" 
					onClick="javascript:document.location='../ltiConsumerManagement/edit.do'" />
		</div>
	</lams:Page>
</body>
</lams:html>


