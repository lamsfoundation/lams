<!DOCTYPE html>

<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<lams:html>
<lams:head>
	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/learningLibraryGroup.css" type="text/css" media="screen" />

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>admin/includes/javascript/learningLibraryGroup.js"></script>
	<script type="text/javascript">
		var groups = ${groups},	
			learningLibraries = ${learningLibraries},
		
			LAMS_URL = '<lams:LAMSURL/>',
			
			decoderDiv = $('<div />'),
			LABELS = {
					<fmt:message key="tool.groups.group.default.name" var="GROUP_DEFAULT_NAME_VAR"/>
					GROUP_DEFAULT_NAME : '${GROUP_DEFAULT_NAME_VAR}',
					<fmt:message key="tool.groups.remove.confirm" var="GROUP_REMOVE_CONFIRM_VAR"/>
					GROUP_REMOVE_CONFIRM : decoderDiv.html('${GROUP_REMOVE_CONFIRM_VAR}').text(),
					<fmt:message key="tool.groups.group.name.error" var="GROUP_NAME_VALIDATION_ERROR_VAR"/>
					GROUP_NAME_VALIDATION_ERROR : decoderDiv.html('${GROUP_NAME_VALIDATION_ERROR_VAR}').text(),
					<fmt:message key="tool.groups.save.error" var="SAVE_ERROR_VAR"/>
					SAVE_ERROR : decoderDiv.html('${SAVE_ERROR_VAR}').text()
			};
	</script>
</lams:head>

<body>
	
		<div id="titleDiv">
			<fmt:message key="tool.groups.dialog.instructions" />
			<button class="pull-right btn btn-default btn-disable-on-downupload" onClick="javascript:saveGroups()">
					<i class="fa fa-save"></i>
				<span><fmt:message key="admin.save" /></span>
			</button>
			<div class="clearfix"></div>
		</div>
		
		<table id="groupsTable">
			<tr>
				<td id="learningLibraryCell">
					<div class="learningLibraryContainer"></div>
				</td>
				<td id="groupsCell">
					<div id="newGroupPlaceholder" class="groupContainer">
						<div><fmt:message key="tool.groups.add.group.button" /></div>
					</div>
				</td>
			</tr>
		</table>
		
		<!-- A template which gets cloned when a group is added -->
		<div id="groupTemplate" class="groupContainer">
			<div class="learningLibraryContainerTitle">
				<i class="removeGroupButton fa fa-times" src="<lams:LAMSURL/>images/css/delete.png"
					 title="<fmt:message key='tool.groups.remove.group.button.tooltip' />"></i>
				<input type="text" />
			</div>
			<div class="learningLibraryContainer"></div>
		</div>
</body>
</lams:html>