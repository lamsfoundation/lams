<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<!DOCTYPE HTML>
<lams:html>
<lams:head>
	<lams:css style="main" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-redmond-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/learningLibraryGroup.css" type="text/css" media="screen" />

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/learningLibraryGroup.js"></script>
	<script type="text/javascript">
		var groups = ${groups},	
			learningLibraries = ${learningLibraries},
		
			LAMS_URL = '<lams:LAMSURL/>',
			LABELS = {
					<fmt:message key="tool.groups.group.default.name" var="GROUP_DEFAULT_NAME_VAR"/>
					GROUP_DEFAULT_NAME : '<c:out value="${GROUP_DEFAULT_NAME_VAR}" />',
					<fmt:message key="tool.groups.remove.confirm" var="GROUP_REMOVE_CONFIRM_VAR"/>
					GROUP_REMOVE_CONFIRM : '<c:out value="${GROUP_REMOVE_CONFIRM_VAR}" />',
					<fmt:message key="tool.groups.group.name.error" var="GROUP_NAME_VALIDATION_ERROR_VAR"/>
					GROUP_NAME_VALIDATION_ERROR : '<c:out value="${GROUP_NAME_VALIDATION_ERROR_VAR}" />',
					<fmt:message key="tool.groups.save.error" var="SAVE_ERROR_VAR"/>
					SAVE_ERROR : '<c:out value="${SAVE_ERROR_VAR}" />'
			};
	</script>
</lams:head>
<body>

<div id="titleDiv">
	<fmt:message key="tool.groups.dialog.instructions" />
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
		<img class="removeGroupButton" src="<lams:LAMSURL/>images/css/delete.png"
			 title="<fmt:message key='tool.groups.remove.group.button.tooltip' />" />
		<input type="text" />
	</div>
	<div class="learningLibraryContainer"></div>
</div>
</body>
</lams:html>