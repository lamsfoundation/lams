<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.tool.management"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/dialog.js"></script>
	
	<script type="text/javascript">
		function openToolGroups(){
			showDialog("dialogToolGroup", {
				'width' : 900,
				'height': 700,
				'title'	: '<fmt:message key="tool.groups.dialog.title" />',
				'open' : function() {
					// load contents after opening the dialog
					$('iframe', this).attr('src', 'openLearningLibraryGroups.do');
				}
			}, false);
		}
	</script>
				
</lams:head>
    
<body class="stripes">

	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbTop"><lams:LAMSURL/>admin/sysadminstart.do | <fmt:message key="sysadmin.maintain" /></c:set>
	<c:set var="breadcrumbActive">. | <fmt:message key="sysadmin.tool.management" /></c:set>
	<c:set var="breadcrumbItems" value="${breadcrumbTop}, ${breadcrumbActive}"/>

	
	<lams:Page type="admin" title="${title}" breadcrumbItems="${breadcrumbItems}">
				
			<div class="panel">
				<p>
					<fmt:message key="msg.edit.tool.content.1" />
					&nbsp;
					<fmt:message key="msg.edit.tool.content.2" />
				</p>
				<p>
					<fmt:message key="msg.edit.tool.content.3" />
				</p>
				
				<a href="javascript:openToolGroups()" class="btn btn-outline-secondary btn-sm pull-right" 
				   title="<fmt:message key='tool.groups.open.button.tooltip' />" ><fmt:message key="tool.groups.open.button" /></a>
				<div class="clearfix"></div>
				
				<p>${fn:length(toolLibrary)}&nbsp;<fmt:message key="sysadmin.library.totals" /></p>
			</div>	
				
				<c:set var="displayToolManagement" value="false" />
				<c:forEach var="dto" items="${toolLibrary}">
					<c:if test="${dto.adminURL != null}">
						<c:set var="displayToolManagement" value="true" />
					</c:if>
				</c:forEach>
				
				<table class="table table-striped table-bordered voffset10">
					<thead class="thead-light">
					<tr>
						<th><fmt:message key="label.tool" /></th>
						<th><fmt:message key="label.tool.version" /></th>
						<th><fmt:message key="label.database.version" /></th>
						<th class="text-center"><fmt:message key="admin.user.actions" /></th>
					</tr>
					</thead>
					<tbody>
					<c:forEach items="${toolLibrary}" var="dto">
						<tr>
							<td <c:if test="${learningLibraryValidity[dto.learningLibraryID] == 'false'}">class="table-danger"</c:if> >
								<strong><c:out value="${dto.activityTitle}" escapeXml="true"/></strong>
								<c:if test="${learningLibraryValidity[dto.learningLibraryID] == 'false'}">
									<span class="badge badge-warning">Disabled</span> 
								</c:if>
								<br/>
								<c:out value="${dto.description}" escapeXml="true"/>
							</td>
							<td>
								<c:out value="${toolVersions[dto.toolID]}" />
							</td>
							<td>
								<c:out value="${dbVersions[dto.toolSignature]}" />
							</td>
							<td class="text-center d-flex">
								<c:choose>
									<c:when test="${learningLibraryValidity[dto.learningLibraryID]}">
										<csrf:form style="display: inline-block;" id="disable${dto.activityTitle}" method="post" action="disable.do">
											<input type="hidden" name="libraryID" value="${dto.learningLibraryID}"/>
											<input type="hidden" name="disable" value="false"/>
											<button type="submit" class="btn btn-sm btn-outline-secondary" title="<fmt:message key="admin.disable" />"/>
												<i class="fa fa-eye-slash"></i>
											</button>
										</csrf:form>
									</c:when>
									<c:otherwise>
										<csrf:form style="display: inline-block;" id="enable${dto.activityTitle}" method="post" action="enable.do">
											<input type="hidden" name="libraryID" value="${dto.learningLibraryID}"/>
											<input type="hidden" name="enable" value="false"/>
											<button type="submit" class="btn btn-sm btn-outline-success" title="<fmt:message key="admin.enable" />"/>
												<i class="fa fa-eye"></i>
											</button>
										</csrf:form>
										
									</c:otherwise>
								</c:choose>
								<c:if test="${not empty dto.toolContentID}">
									<c:set var="editDefaultContentUrl">
										<lams:LAMSURL /><c:out value="${dto.authoringURL}" />?toolContentID=${dto.toolContentID}&contentFolderID=-1"
									</c:set>
									&nbsp;
									<a class="btn btn-sm btn-outline-primary" id="defaultContent${dto.activityTitle}" href="${editDefaultContentUrl}" target="_blank" title="<fmt:message key="sysadmin.edit.default.tool.content" />">
										<i class="fa fa-pencil fa-wa"></i>
									</a>
									<c:if test="${(displayToolManagement == 'true') and (dto.adminURL != null)}">
										&nbsp;
										<a title="<fmt:message key="msg.tool.management" />" class="btn btn-sm btn-outline-primary" id="toolManagement${dto.activityTitle}" href="<lams:LAMSURL /><c:out value="${dto.adminURL}" />" >
											<i class="fa fa-cog fa-wa"></i>
										</a>
									</c:if>
								</c:if>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				</p>
	</lams:Page>

</body>
</lams:html>
