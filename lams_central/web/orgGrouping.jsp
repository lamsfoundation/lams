<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE HTML>
<lams:html>
<lams:head>
	<lams:css style="main" />
	<link rel="stylesheet" href="css/jquery-ui-redmond-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/orgGrouping.css" type="text/css" media="screen" />
	<style type="text/css">
		div.branchMappingDialog > table {
			width: 100%;
			height: 98%;
			table-layout: fixed;
			border-collapse: collapse;
		}
		
		div.branchMappingDialog td {
			text-align: center;
			vertical-align: top;
		}
		
		div.branchMappingDialog td.branchMappingLabelCell {
			height: 15px;
			font-weight: bold;
		}
		
		div.branchMappingDialog td.branchMappingListCell {
			border: thin black solid;
			padding: 3px;
		}
		
		div.branchMappingDialog td.branchMappingListCell .selected {
			background-color: #5c9ccc;
			color: white;
		}
		
		div.branchMappingDialog .branchMappingButton {
			width: 40%;
			height: 25px;
			display: block;
			margin: auto;
			margin-bottom: 20px;
		}
		
		div.branchMappingDialog td.branchMappingListCell div {
			text-align: left;
			cursor: pointer;
		}	
	</style>
	
	<c:set var="lessonMode" value="${not empty param.activityID}" />
	<c:set var="groupingMethod" value="${usedForBranching eq true ? 'openGroupMappingDialog' : 'viewGroups'}" />
	
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/orgGrouping.js"></script>
	<script type="text/javascript">
		var groupingActivityId = '${param.activityID}',
			lessonMode = ${lessonMode},
			// parameter can be null and the ID available only as a JSP attribute
			organisationId = ${empty organisationID ? param.organisationID : organisationID},
			lessonId = '${param.lessonID}',
		
			LAMS_URL = '<lams:LAMSURL/>',
			
			decoderDiv = $('<div />'),
			LABELS = {
				<fmt:message key="label.course.groups.grouping.remove.confirm" var="REMOVE_GROUPING_CONFIRM_LABEL_VAR"/>
				REMOVE_GROUPING_CONFIRM_LABEL : decoderDiv.html('<c:out value="${REMOVE_GROUPING_CONFIRM_LABEL_VAR}" />').text(),
				<fmt:message key="label.course.groups.grouping.use.confirm" var="USE_GROUPING_CONFIRM_LABEL_VAR"/>
				USE_GROUPING_CONFIRM_LABEL : decoderDiv.html('<c:out value="${USE_GROUPING_CONFIRM_LABEL_VAR}" />').text(),
				<fmt:message key="authoring.fla.course.groups.to.branches.match.dialog.title" var="COURSE_GROUPS_TO_BRANCHES_MATCH_DIALOG_TITLE_VAR"/>
				COURSE_GROUPS_TO_BRANCHES_MATCH_DIALOG_TITLE : '<c:out value="${COURSE_GROUPS_TO_BRANCHES_MATCH_DIALOG_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.ok.button" var="OK_BUTTON_VAR"/>
				OK_BUTTON : '<c:out value="${OK_BUTTON_VAR}" />'
			};
	</script>
</lams:head>
<body>

	<div id="titleDiv">
		<c:choose>
			<c:when test="${lessonMode}">
				<fmt:message key="index.course.groups.title" />
			</c:when>
			<c:otherwise>
				<fmt:message key="label.course.groups.grouping.title" />
			</c:otherwise>
		</c:choose>
	</div>

	<c:forEach var="grouping" items="${groupings}">
		<%-- In lesson mode do not show groupings with zero groups --%>
		<c:if test="${not lessonMode or grouping.groupCount > 0}">
			<div id="grouping-${grouping.groupingId}" class="groupingContainer">
				<a href="#" class="groupingName" onClick="javascript:${groupingMethod}(${grouping.groupingId})"> <c:out value="${grouping.name}" />
				</a> <span class="groupCount" title='<fmt:message key="label.course.groups.grouping.count.label" />'>
					(${grouping.groupCount}) </span>
				<c:if test="${canEdit and not lessonMode}">
					<img class="removeGroupingButton" src="<lams:LAMSURL/>images/css/delete.png"
						title="<fmt:message key='label.course.groups.remove.tooltip' />" onClick="javascript:removeGrouping(${grouping.groupingId})" />
				</c:if>
			</div>
		</c:if>
	</c:forEach>

	<c:if test="${canEdit}">
		<div id="addGroupingButton" class="ui-button" onClick="javascript:showGroups()">
			<fmt:message key='label.course.groups.grouping.create' />
		</div>
	</c:if>
	
	<c:if test="${usedForBranching eq true}">
		<div id="groupMappingDialog" class="branchMappingDialog" style="display:none">
			<table>
				<tr>
					<td></td>
					<td></td>
					<td rowspan="2"></td>
					<td colspan="2" class="branchMappingLabelCell">
						<fmt:message key="authoring.fla.page.dialog.mappings" />
					</td>
				</tr>
				<tr>
					<td class="branchMappingLabelCell branchMappingFreeItemHeaderCell">
						<fmt:message key="authoring.fla.branch.mapping.course.groups.header" />
					</td>
					<td class="branchMappingLabelCell branchMappingFreeBranchHeaderCell">
						<fmt:message key="authoring.fla.branch.mapping.branching.groups.header" />
					</td>
					<td class="branchMappingLabelCell branchMappingBoundItemHeaderCell">
						<fmt:message key="authoring.fla.branch.mapping.course.groups.header" />
					</td>
					<td class="branchMappingLabelCell branchMappingBoundBranchHeaderCell">
						<fmt:message key="authoring.fla.branch.mapping.branching.groups.header" />
					</td>
				</tr>
				<tr>
					<td class="branchMappingFreeItemCell branchMappingListCell"></td>
					<td class="branchMappingFreeBranchCell branchMappingListCell"></td>
					<td>
						<div class="branchMappingAddButton branchMappingButton"></div>
						<div class="branchMappingRemoveButton branchMappingButton"></div>
					</td>
					<td class="branchMappingBoundItemCell branchMappingListCell"></td>
					<td class="branchMappingBoundBranchCell branchMappingListCell"></td>
				</tr>
			</table>
		</div>
	</c:if>
</body>
</lams:html>