<%@ include file="/taglibs.jsp"%>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$('#toolGroupsDialog').dialog({
			'autoOpen' : false,
			'modal'	   : true,
			'draggable': false,
			'width'	   : 900,
			'height'   : 700,
			'title'	   : '<fmt:message key="tool.groups.dialog.title" />',
			'open' 	   : function(){
				$('#toolGroupsFrame', this).attr('src', 'toolcontentlist.do?action=openLearningLibraryGroups');
			},
			'beforeClose'	   : function(){
				$('#toolGroupsFrame', this).attr('src', null);
			}
		});

		$('div.ui-dialog-titlebar').prepend($('.customDialogButton'));
	});

	function openToolGroups(){
		$('#toolGroupsDialog').dialog('open');
	}
	
	function saveToolGroups(){
		var result = $('#toolGroupsFrame')[0].contentWindow.saveGroups();
		if (result) {
			$('#toolGroupsDialog').dialog('close');
		}
	}
</script>


<h4><a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a></h4>
<h1><fmt:message key="sysadmin.tool.management" /></h1>

<p>
	<fmt:message key="msg.edit.tool.content.1" />
	&nbsp;&nbsp;
	<fmt:message key="msg.edit.tool.content.2" />
</p>
<p>
	<fmt:message key="msg.edit.tool.content.3" />
</p>

<a href="javascript:openToolGroups()" class="button" style="float:right; margin: 10px 0"
   title="<fmt:message key='tool.groups.open.button.tooltip' />"><fmt:message key="tool.groups.open.button" /></a>
<div id="toolGroupsDialog" style="display: none">
	<a class="customDialogButton" href="javascript:saveToolGroups()" style="right: 50px;">
		<fmt:message key="admin.save" />
	</a>
	<iframe id="toolGroupsFrame" style="border: none; width: 100%; height: 100%"></iframe>
</div>


<c:set var="displayToolManagement" value="false" />
<logic:iterate id="dto" name="toolLibrary">
	<c:if test="${dto.adminURL != null}">
		<c:set var="displayToolManagement" value="true" />
	</c:if>
</logic:iterate>

<p>
<table class="alternative-color" width="100%">
	<tr>
		<th><fmt:message key="label.tool" /></th>
		<th><fmt:message key="sysadmin.library.activity.description" /></th>
		<th><fmt:message key="label.tool.version" /></th>
		<th><fmt:message key="label.database.version" /></th>
		<th style="padding-right: 15px"><fmt:message key="sysadmin.function" /></th>
	</tr>
	<logic:iterate name="toolLibrary" id="dto">
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
						<a href="<c:url value='toolcontentlist.do?action=disable&libraryID=${dto.learningLibraryID}' />"><fmt:message key="admin.disable" /></a>
					</c:when>
					<c:otherwise>
						<a href="<c:url value='toolcontentlist.do?action=enable&libraryID=${dto.learningLibraryID}'/>"><fmt:message key="admin.enable" /></a>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td colspan="5">
				<c:if test="${not empty dto.toolContentID}">
					<c:set var="editDefaultContentUrl">
						<lams:LAMSURL /><c:out value="${dto.authoringURL}" />?toolContentID=<c:out value="${dto.toolContentID}" />&contentFolderID=-1"
					</c:set>
					<a href="${editDefaultContentUrl}" target="_blank">
						<fmt:message key="sysadmin.edit.default.tool.content" />
					</a>
					<c:if test="${(displayToolManagement == 'true') and (dto.adminURL != null)}">
						&nbsp;&nbsp;
						<a href="<lams:LAMSURL /><c:out value="${dto.adminURL}" />">
							<fmt:message key="msg.tool.management" />
						</a>
					</c:if>
				</c:if>
			</td>
		</tr>
	</logic:iterate>
</table>
</p>
<p>${fn:length(toolLibrary)} <fmt:message key="sysadmin.library.totals" /></p>