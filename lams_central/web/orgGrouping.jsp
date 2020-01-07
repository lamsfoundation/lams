<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<!DOCTYPE html>
<lams:html>
<lams:head>
	<lams:css/>
	<link rel="stylesheet" href="${lams}css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="${lams}css/orgGrouping.css" type="text/css" media="screen" />
	
	<c:set var="lessonMode" value="${not empty param.activityID}" />
	<c:set var="groupingMethod" value="${usedForBranching eq true ? 'openGroupMappingDialog' : 'viewGroups'}" />
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/orgGrouping.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/dialog.js"></script>
	<script type="text/javascript">
		var groupingActivityId = '${param.activityID}',
			lessonMode = ${lessonMode},
			// parameter can be null and the ID available only as a JSP attribute
			organisationId = ${empty organisationID ? param.organisationID : organisationID},
			lessonId = '${param.lessonID}',
			csrfTokenName = '<csrf:tokenname/>',
			csrfTokenValue = '<csrf:tokenvalue/>',
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
				<i class="removeGroupingButton fa fa-remove fa-lg" title="<fmt:message key='label.course.groups.remove.tooltip' />"
				   onClick="javascript:removeGrouping(${grouping.groupingId})" ></i>
			</c:if>
		</div>
	</c:if>
</c:forEach>

<c:if test="${canEdit}">
		<div id="addGroupingButton" class="btn btn-default" onClick="javascript:viewGroups()">
		<i class="fa fa-plus"></i>
		<span><fmt:message key='label.course.groups.grouping.create' /></span>
	</div>
</c:if>
	
<c:if test="${usedForBranching eq true}">
	<div id="groupMappingDialogContents" style="display:none">
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
					<button class="btn btn-default branchMappingAddButton branchMappingButton"><i class="fa fa-chevron-right"></i></button>
					<button class="btn btn-default branchMappingRemoveButton branchMappingButton"><i class="fa fa-chevron-left"></i></button>
				</td>
				<td class="branchMappingBoundItemCell branchMappingListCell"></td>
				<td class="branchMappingBoundBranchCell branchMappingListCell"></td>
			</tr>
			<tr>
				<td colspan="5">
					<div class="container-fluid">
						<div class="pull-right btn-group" role="group">
							<button id="branchMappingOKButton" class="btn btn-default">
								<i class="fa fa-check"></i> 
								<span><fmt:message key="authoring.fla.ok.button" /></span>
							</button>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
</c:if>
</body>
</lams:html>