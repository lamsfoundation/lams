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
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
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
	
	<lams:Page type="admin" title="${title}">
				<p><a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

				<h1><fmt:message key="sysadmin.tool.management" /></h1>
				
				<div class="panel">
					<p>
						<fmt:message key="msg.edit.tool.content.1" />
						&nbsp;
						<fmt:message key="msg.edit.tool.content.2" />
					</p>
					<p>
						<fmt:message key="msg.edit.tool.content.3" />
					</p>
					
					<a href="javascript:openToolGroups()" class="btn btn-default pull-right" 
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
				
				<table class="table table-striped table-condensed voffset10">
					<tr>
						<th><fmt:message key="label.tool" /></th>
						<th><fmt:message key="sysadmin.library.activity.description" /></th>
						<th><fmt:message key="label.tool.version" /></th>
						<th><fmt:message key="label.database.version" /></th>
						<th style="padding-right: 15px"><fmt:message key="admin.user.actions" /></th>
					</tr>
					<c:forEach items="${toolLibrary}" var="dto">
						<tr>
							<td>
								<c:out value="${dto.activityTitle}" />
							</td>
							<td>
								<c:out value="${dto.description}" />
							</td>
							<td>
								<c:out value="${toolVersions[dto.toolID]}" />
							</td>
							<td>
								<c:out value="${dbVersions[dto.toolSignature]}" />
							</td>
							<td>
								<c:choose>
									<c:when test="${learningLibraryValidity[dto.learningLibraryID]}">
										[<a id="disable${dto.activityTitle}" href="<c:url value='disable.do?libraryID=${dto.learningLibraryID}' />" ><fmt:message key="admin.disable" /></a>] 
									</c:when>
									<c:otherwise>
										[<a id="enable${dto.activityTitle}" href="<c:url value='enable.do?libraryID=${dto.learningLibraryID}' />" ><fmt:message key="admin.enable" /></a>]
										
									</c:otherwise>
								</c:choose>
								<c:if test="${not empty dto.toolContentID}">
									<c:set var="editDefaultContentUrl">
										<lams:LAMSURL /><c:out value="${dto.authoringURL}" />?toolContentID=${dto.toolContentID}&contentFolderID=-1"
									</c:set>
									[<a id="defaultContent${dto.activityTitle}" href="${editDefaultContentUrl}" target="_blank"><fmt:message key="sysadmin.edit.default.tool.content" /></a>]
									<c:if test="${(displayToolManagement == 'true') and (dto.adminURL != null)}">
										&nbsp;&nbsp;
										[<a id="toolManagement${dto.activityTitle}" href="<lams:LAMSURL /><c:out value="${dto.adminURL}" />" ><fmt:message key="msg.tool.management" /></a>]
									</c:if>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>
				</p>
	</lams:Page>

</body>
</lams:html>
