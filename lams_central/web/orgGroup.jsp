<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<lams:css/>
	<link rel="stylesheet" href="css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/orgGroup.css" type="text/css" media="screen" />

	<c:set var="lessonMode" value="${not empty param.activityID}" />

	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="includes/javascript/dialog.js"></script>
	<script type="text/javascript" src="includes/javascript/orgGroup.js"></script>
	<script type="text/javascript">;
		var grouping = ${grouping},
			organisationId = grouping.organisationId,
			unassignedUsers = ${unassignedUsers},
			canEdit = ${canEdit},
			groupingActivityId = '${param.activityID}',
			lessonMode = ${lessonMode},
			// This attribute can be empty.
			// When true, it means that it shows already existing groups so no calls to server are needed.
			skipAssigningWhenCreatingGroup = ${skipInitialAssigning eq true},
			// This attribute can be empty.
			// When true, it means that groups can not be added or removed, but user can be still moved.
			usedForBranching = ${usedForBranching eq true},
		
			LAMS_URL = '<lams:LAMSURL/>',
			decoderDiv = $('<div />'),
			LABELS = {
				<fmt:message key="label.course.groups.prefix" var="GROUP_PREFIX_LABEL_VAR"/>
				GROUP_PREFIX_LABEL : '<c:out value="${GROUP_PREFIX_LABEL_VAR}" />',
				<fmt:message key="label.course.groups.remove.confirm" var="GROUP_REMOVE_LABEL_VAR"/>
				GROUP_REMOVE_LABEL : decoderDiv.html('<c:out value="${GROUP_REMOVE_LABEL_VAR}" />').text(),
				<fmt:message key="label.course.groups.remove.empty.confirm" var="EMPTY_GROUP_SAVE_LABEL_VAR"/>
				EMPTY_GROUP_SAVE_LABEL : decoderDiv.html('<c:out value="${EMPTY_GROUP_SAVE_LABEL_VAR}" />').text(),
				<fmt:message key="label.course.groups.locked" var="GROUP_LOCK_LABEL_VAR"/>
				GROUP_LOCK_LABEL : decoderDiv.html('<c:out value="${GROUP_LOCK_LABEL_VAR}" />').text(),
				<fmt:message key="label.course.groups.locked.transfer" var="TRANSFER_LOCKED_LABEL_VAR"/>
				TRANSFER_LOCKED_LABEL : decoderDiv.html('<c:out value="${TRANSFER_LOCKED_LABEL_VAR}" />').text(),
				<fmt:message key="label.save.as.course.grouping" var="SAVE_AS_COURSE_GROUPING_LABEL_VAR"/>
				SAVE_AS_COURSE_GROUPING_LABEL : decoderDiv.html('<c:out value="${SAVE_AS_COURSE_GROUPING_LABEL_VAR}" />').text(),
				<fmt:message key="label.course.groups.name.blank" var="NAME_BLANK_LABEL_VAR"/>
				NAME_BLANK_LABEL : decoderDiv.html('<c:out value="${NAME_BLANK_LABEL_VAR}" />').text(),
				<fmt:message key="label.course.groups.name.not.unique" var="NAME_NOT_UNIQUE_LABEL_VAR"/>
				NAME_NOT_UNIQUE_LABEL : decoderDiv.html('<c:out value="${NAME_NOT_UNIQUE_LABEL_VAR}" />').text(),
				<fmt:message key="label.enter.course.grouping.name" var="ENTER_COURSE_GROUPING_NAME_LABEL_VAR"/>
				ENTER_COURSE_GROUPING_NAME_LABEL : decoderDiv.html('<c:out value="${ENTER_COURSE_GROUPING_NAME_LABEL_VAR}" />').text(),
				<fmt:message key="authoring.msg.save.success" var="SAVED_SUCCESSFULLY_LABEL_VAR"/>
				SAVED_SUCCESSFULLY_LABEL : decoderDiv.html('<c:out value="${SAVED_SUCCESSFULLY_LABEL_VAR}" />').text()
			};
	</script>
</lams:head>
<body>

<c:if test="${lessonMode}">
	<div id="save-course-grouping-container">
		<button class="pull-right btn btn-default" onClick="javascript:saveAsCourseGrouping();">
			<i class="fa fa-save"></i>
			<fmt:message key="label.save.as.course.grouping" />
		</button>
	</div>
</c:if>

<c:if test="${not lessonMode and (empty usedForBranching or usedForBranching eq false)}">
	<div id="titleDiv">
		<fmt:message key="label.course.groups.name" />
		<input id="groupingName" type="text"
			<c:if test="${not canEdit or lessonMode}">
				readonly="readonly"
			</c:if>
		/>
		<!-- It is showed when user tries to save a grouping with empty name -->
		<span id="groupingNameBlankError" class="errorMessage">
			<fmt:message key="label.course.groups.name.blank" />
		</span>
		
		<c:if test="${canEdit}">
			<button class="pull-right btn btn-default" onClick="javascript:saveGroups()">
				<i class="fa fa-save"></i>
				<span><fmt:message key="button.save" /></span>
			</button>
		</c:if>
		<button class="pull-right btn btn-default" 
		   onClick="javascript:window.parent.showOrgGroupingDialog(organisationId)"
		>
			<i class="fa fa-users"></i>
			<fmt:message key="label.course.groups.back" />
		</button>
	</div>
</c:if>

<div id="titleInstructions">
	<c:choose>
		<c:when test="${canEdit}">
			<fmt:message key="label.course.groups.edit.title" />
		</c:when>
		<c:otherwise>
			<fmt:message key="label.course.groups.viewonly.title" />
		</c:otherwise>
	</c:choose>
</div>

<table id="groupsTable">
	<tr>
		<td id="unassignedUserCell">
			<div class="userContainerTitle">
				<fmt:message key="label.course.groups.unassigned" />
				<span class="sortUsersButton"
				      title="<fmt:message key='label.course.groups.sort.tooltip' />">▲</span>
			</div>
			<div class="userContainer"></div>
		</td>
		<td id="groupsCell">
			<c:if test="${canEdit and not usedForBranching}">
				<div id="newGroupPlaceholder" class="groupContainer">
					<div><fmt:message key="label.course.groups.add" /></div>
				</div>
			</c:if>
		</td>
	</tr>
</table>

<!-- A template which gets cloned when a group is added -->
<div id="groupTemplate" class="groupContainer">
	<div class="userContainerTitle">
		<c:if test="${canEdit and not usedForBranching}">
			<i class="removeGroupButton fa fa-remove fa-2x" title="<fmt:message key='label.course.groups.remove.tooltip' />" ></i>
		</c:if>
		<input type="text" 
			<c:if test="${not canEdit}">
				readonly="readonly"
			</c:if>
		/>
		<span class="sortUsersButton"
		      title="<fmt:message key='label.course.groups.sort.tooltip' />">▲</span>
	</div>
	<div class="userContainer"></div>
</div>

<!-- Inner dialog placeholders -->
<div id="save-course-grouping-dialog-contents" class="dialogContainer">
	<span id="span-tooltip">
		<fmt:message key="label.enter.course.grouping.name"/>
	</span>
		
	<div>
		<input id="dialog-course-grouping-name" type="text"/>
	</div>
			
	<div class="btn-group pull-right">
		<button id="dialog-save-button" class="btn btn-default roffset5">
			<fmt:message key="button.save" />
		</button>
		<button id="dialog-close-button" class="btn btn-default">
			<fmt:message key="label.cancel" />
		</button>
	</div>
</div>
	
</body>
</lams:html>