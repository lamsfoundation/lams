<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="lessonMode" value="${not empty param.activityID}" />
<c:set var="groupingMethod" value="${usedForBranching eq true ? 'openGroupMappingDialog' : 'viewGroups'}" />
<c:set var="title">
	<c:choose>
		<c:when test="${lessonMode}">
			<fmt:message key="index.course.groups.title" />
		</c:when>
		<c:otherwise>
			<fmt:message key="label.course.groups.grouping.title" />
		</c:otherwise>
	</c:choose>
</c:set>
	
<lams:html>
<lams:head>
	<title><c:out value="${title}" /></title>

	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme5.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/orgGrouping.css" type="text/css" media="screen" />
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
	<lams:JSImport src="includes/javascript/dialog5.js" />
	<lams:JSImport src="includes/javascript/orgGrouping.js" />
	
	<script type="text/javascript">
		var groupingActivityId = '${param.activityID}',
			lessonMode = ${lessonMode},
			// parameter can be null and the ID available only as a JSP attribute
			organisationId = ${empty organisationID ? param.organisationID : organisationID},
			targetOrganisationId = '${targetOrganisationId}',
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
<body class="component">
	<lams:Page5 type="admin" title="${title}">
		<c:if test="${lessonMode}">
			<div class="row mb-2">
				<div class="col-4 offset-4">
					<div class="card">
						<div class="card-body">
							<fmt:message key="label.grouping.general.instructions.line1" />
						</div>
					</div>
					
				</div>
			</div>
		</c:if>
		
		<div class="row">
			<div class="col-4 offset-4">
				<ul class="list-group">
					<c:forEach var="grouping" items="${groupings}">
						<%-- In lesson mode do not show groupings with zero groups --%>
						<c:if test="${not lessonMode or grouping.groupCount > 0}">
							<li id="grouping-${grouping.groupingId}" class="grouping list-group-item fw-bold"
								onClick="javascript:${groupingMethod}(${grouping.groupingId})">
								<div class="row">
									<div class="col-10">
										<c:out value="${grouping.name}" />
									</div>
									<div class="col-1">
										<span class="badge text-bg-primary" title='<fmt:message key="label.course.groups.grouping.count.tooltip" />'>
											${grouping.groupCount}
										</span>
									</div>
									<div class="col-1 text-end">
										<c:if test="${canEdit and not lessonMode}">
											<i class="removeGroupingButton fa fa-remove fa-lg" title="<fmt:message key='label.course.groups.remove.tooltip' />"
											   onClick="javascript:removeGrouping(${grouping.groupingId})" ></i>
										</c:if>
									</div>
								</div>
							</li>
						</c:if>
					</c:forEach>
				</ul>
			</div>
		</div>
		<c:if test="${canEdit}">
			<div class="row mt-3">
				<div class="col-4 offset-4 text-end">
					<div class="btn btn-secondary" onClick="javascript:viewGroups()">
					<i class="fa fa-plus"></i>
					<span><fmt:message key='label.course.groups.grouping.create' /></span>
				</div>
			</div>
		</c:if>
</lams:Page5>

<c:if test="${usedForBranching eq true}">
<div id="groupMappingDialogContents" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog modal-dialog-centered modal-lg">
    <div class="modal-content">
    	<div class="modal-body">
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
						<button class="btn btn-secondary branchMappingAddButton branchMappingButton"><i class="fa fa-chevron-right"></i></button>
						<button class="btn btn-secondary branchMappingRemoveButton branchMappingButton"><i class="fa fa-chevron-left"></i></button>
					</td>
					<td class="branchMappingBoundItemCell branchMappingListCell"></td>
					<td class="branchMappingBoundBranchCell branchMappingListCell"></td>
				</tr>
			</table>
    	</div>
      	<div class="modal-footer">
			<button id="branchMappingOKButton" class="btn btn-primary">
				<i class="fa fa-check"></i> 
				<span><fmt:message key="authoring.fla.ok.button" /></span>
			</button>
      	</div>
    </div>
  </div>
</div>
</c:if>

</body>
</lams:html>