<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE HTML>
<lams:html>
<lams:head>
	<lams:css style="main" />
	<link rel="stylesheet" href="css/jquery-ui-redmond-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/orgGroup.css" type="text/css" media="screen" />

	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/orgGroup.js"></script>
	<script type="text/javascript">
		var orgId = '${organisationID}';
		var groups = ${groups};
		var unassignedUsers = ${unassignedUsers};
		var canEdit = ${canEdit};
		
		var LAMS_URL = '<lams:LAMSURL/>';
		var LABELS = {
			GROUP_PREFIX_LABEL : '<fmt:message key="label.course.groups.prefix" />',
			GROUP_REMOVE_LABEL : '<fmt:message key="label.course.groups.remove.confirm" />',
			EMPTY_GROUP_SAVE_LABEL : '<fmt:message key="label.course.groups.remove.empty.confirm" />'
		};
	</script>
</lams:head>
<body>
<c:if test="${canEdit}">
	<!-- It has to have inline style as it gets appended to dialog titlebar
	     where local CSS is not visible -->
	<a id="saveButton" href="#" onClick="javascript:saveOrgGroups()" style="
		position: absolute;
		right: 50px;
		top: 0;
		width: 50px;
		padding: 5px 0 5px 25px;
		vertical-align: center;
		font-weight:bolder;
		border: thin solid #2E6E9E;
		color: #222222;
		background-color: #D0E5F5;"
	>
		<fmt:message key="button.save" />
	</a>
</c:if>

<div id="titleDiv">
<c:choose>
	<c:when test="${canEdit}">
		<fmt:message key="label.course.groups.edit.title" />
	</c:when>
	<c:otherwise>
		<fmt:message key="label.course.groups.viewonly.title" />
	</c:otherwise>
</c:choose>
</div>

<table>
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
			<c:if test="${canEdit}">
				<div id="newGroupPlaceholder" class="groupContainer">
					<div><fmt:message key="label.course.groups.add" /></div>
				</div>
			</c:if>
		</td>
	</tr>
</table>

<div id="groupTemplate" class="groupContainer">
	<div class="userContainerTitle">
		<input type="text" 
			<c:if test="${not canEdit}">
				readonly="readonly"
			</c:if>
		/>
		<span class="sortUsersButton"
		      title="<fmt:message key='label.course.groups.sort.tooltip' />">▲ </span>
		<c:if test="${canEdit}">
			<img class="removeGroupButton" src="<lams:LAMSURL/>images/cross.gif"
		    	 title="<fmt:message key='label.course.groups.remove.tooltip' />" />
		</c:if>
	</div>
	<div class="userContainer"></div>
</div>
</body>
</lams:html>