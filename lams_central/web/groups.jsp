<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE))%></c:set>
<c:set var="lessonMode" value="${not empty param.activityID}" />
<c:if test="${empty title}">
	<c:set var="title"><fmt:message key="label.course.groups.viewonly.title" /></c:set>
</c:if>

<lams:html>
	<lams:head>
		<title>${title}</title>

		<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme5.css">
		<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
		<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
		<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
		<link rel="stylesheet" href="<lams:LAMSURL/>css/groups.css">

		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.cookie.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
		<lams:JSImport src="includes/javascript/dialog5.js" />
		<lams:JSImport src="includes/javascript/groups.js" />
		<lams:JSImport src="includes/javascript/portrait5.js" />
		<lams:JSImport src="includes/javascript/upload.js" />
		<script type="text/javascript">;
		var grouping = ${grouping},
				organisationId = grouping.organisationId,
				unassignedUsers = ${unassignedUsers},
				groupingActivityId = '<c:out value="${param.activityID}" />',
				lessonId = '${lessonID}',
				lessonMode = ${lessonMode},
				// This attribute can be empty.
				// When true, it means that it shows already existing groups so no calls to server are needed.
				skipAssigningWhenCreatingGroup = ${skipInitialAssigning eq true},
				// This attribute can be empty.
				// When true, it means that groups can not be added or removed, but user can be still moved.
				usedForBranching = ${usedForBranching eq true},
				warnBeforeUpload = false,
				csrfTokenName = '<csrf:tokenname/>',
				csrfTokenValue = '<csrf:tokenvalue/>',

				LAMS_URL = '<lams:LAMSURL/>',
				LABELS = {
					<fmt:message key="label.course.groups.prefix" var="GROUP_PREFIX_LABEL_VAR"/>
					GROUP_PREFIX_LABEL : "<spring:escapeBody javaScriptEscape='true'>${GROUP_PREFIX_LABEL_VAR}</spring:escapeBody>",

					<fmt:message key="label.course.groups.remove.confirm" var="GROUP_REMOVE_LABEL_VAR"/>
					GROUP_REMOVE_LABEL : "<spring:escapeBody javaScriptEscape='true'>${GROUP_REMOVE_LABEL_VAR}</spring:escapeBody>",

					<fmt:message key="label.course.groups.remove.empty.confirm" var="EMPTY_GROUP_SAVE_LABEL_VAR"/>
					EMPTY_GROUP_SAVE_LABEL : "<spring:escapeBody javaScriptEscape='true'>${EMPTY_GROUP_SAVE_LABEL_VAR}</spring:escapeBody>",

					<fmt:message key="label.course.groups.locked" var="GROUP_LOCK_LABEL_VAR"/>
					GROUP_LOCK_LABEL : "<spring:escapeBody javaScriptEscape='true'>${GROUP_LOCK_LABEL_VAR}</spring:escapeBody>",

					<fmt:message key="label.course.groups.locked.transfer" var="TRANSFER_LOCKED_LABEL_VAR"/>
					TRANSFER_LOCKED_LABEL : "<spring:escapeBody javaScriptEscape='true'>${TRANSFER_LOCKED_LABEL_VAR}</spring:escapeBody>",

					<fmt:message key="label.save.as.course.grouping" var="SAVE_AS_COURSE_GROUPING_LABEL_VAR"/>
					SAVE_AS_COURSE_GROUPING_LABEL : "<spring:escapeBody javaScriptEscape='true'>${SAVE_AS_COURSE_GROUPING_LABEL_VAR}</spring:escapeBody>",

					<fmt:message key="label.course.groups.name.blank" var="NAME_BLANK_LABEL_VAR"/>
					NAME_BLANK_LABEL : "<spring:escapeBody javaScriptEscape='true'>${NAME_BLANK_LABEL_VAR}</spring:escapeBody>",

					<fmt:message key="label.course.groups.name.not.unique" var="NAME_NOT_UNIQUE_LABEL_VAR"/>
					NAME_NOT_UNIQUE_LABEL : "<spring:escapeBody javaScriptEscape='true'>${NAME_NOT_UNIQUE_LABEL_VAR}</spring:escapeBody>",

					<fmt:message key="label.enter.course.grouping.name" var="ENTER_COURSE_GROUPING_NAME_LABEL_VAR"/>
					ENTER_COURSE_GROUPING_NAME_LABEL : "<spring:escapeBody javaScriptEscape='true'>${ENTER_COURSE_GROUPING_NAME_LABEL_VAR}</spring:escapeBody>",

					<fmt:message key="authoring.msg.save.success" var="SAVED_SUCCESSFULLY_LABEL_VAR"/>
					SAVED_SUCCESSFULLY_LABEL : "<spring:escapeBody javaScriptEscape='true'>${SAVED_SUCCESSFULLY_LABEL_VAR}</spring:escapeBody>",

					<fmt:message key="label.import.warning.replace.groups" var="WARNING_REPLACE_GROUPS_VAR"/>
					WARNING_REPLACE_GROUPS_LABEL : "<spring:escapeBody javaScriptEscape='true'>${WARNING_REPLACE_GROUPS_VAR}</spring:escapeBody>",

					<fmt:message key="error.general.1" var="ERROR1_VAR"/>
					<fmt:message key="error.general.2" var="ERROR2_VAR"/>
					<fmt:message key="error.general.3" var="ERROR3_VAR"/>
					GENERAL_ERROR_LABEL : "<spring:escapeBody javaScriptEscape='true'>${ERROR1_VAR}</spring:escapeBody>\n<spring:escapeBody javaScriptEscape='true'>${ERROR2_VAR}</spring:escapeBody>\n<spring:escapeBody javaScriptEscape='true'>${ERROR3_VAR}</spring:escapeBody>",

					<fmt:message key="error.file.required" var="ERROR_FILE_REQUIRED_VAR"/>
					ERROR_FILE_REQUIRED_LABEL : "<spring:escapeBody javaScriptEscape='true'>${ERROR_FILE_REQUIRED_VAR}</spring:escapeBody>",

					<fmt:message key="error.file.wrong.format" var="ERROR_FILE_WRONG_FORMAT_VAR"/>
					ERROR_FILE_WRONG_FORMAT_LABEL : "<spring:escapeBody javaScriptEscape='true'>${ERROR_FILE_WRONG_FORMAT_VAR}</spring:escapeBody>",

					<fmt:message key="label.import.successful" var="LABEL_IMPORT_SUCCESSFUL_VAR">
					<fmt:param value="%1"/>
					<fmt:param value="%2"/>
					</fmt:message>
					LABEL_IMPORT_SUCCESSFUL_LABEL : "<spring:escapeBody javaScriptEscape='true'>${LABEL_IMPORT_SUCCESSFUL_VAR}</spring:escapeBody>",

				};

		<!-- LDEV_NTU-7 Page jumps to the top when clicking the link in Grouping -->
		jQuery(document).ready(function(){
			$('#advanced-settings-accordion .collapse').on('shown.bs.collapse', function() {
				var collapseElement = $(this);
				$('html, body').animate({
					scrollTop: $('.accordion-body', collapseElement).offset().top
				}, 0);
			});
		});
		</script>
	</lams:head>

	<body class="component my-2">
	<lams:Page5 title="${title}" type="monitoring">
		<c:if test="${param.displayReturnToMonitoringLink eq 'true'}">
			<a class="btn btn-primary mb-2" href="<lams:LAMSURL/>home/monitorLesson.do?lessonID=${param.lessonID}">
				<i class="fa-solid fa-arrow-left"></i>&nbsp;<fmt:message key="label.grouping.return.to.monitoring"/>
			</a>
		</c:if>
		<div class="card mb-2">
			<div class="card-body">
				<c:if test="${not empty description}">
					<p><c:out value="${description}"/></p>
				</c:if>

				<h4 class="card-title"><fmt:message key="label.grouping.general.instructions.heading"/></h4>

				<p class="card-text">
					<fmt:message key="label.grouping.general.instructions.line2"/>
				</p>

				<c:if test="${usedForBranching}">
					<p class="card-text"><fmt:message key="label.grouping.general.instructions.branching"/></p>
				</c:if>
			</div>
		</div>

		<div class="card mb-2">
			<div class="card-body">
				<c:if test="${not lessonMode and (empty usedForBranching or usedForBranching eq false)}">
					<!-- It is shown when user tries to save a grouping with empty name -->

					<h5 id="grouping-name-blank-error" class="text-danger text-center d-none">
						<fmt:message key="label.course.groups.name.blank" />
					</h5>

					<!-- It is shown when user tries to save a grouping with a non unique name -->
					<h5 id="grouping-name-non-unique-error" class="text-danger text-center d-none">
						<fmt:message key="label.course.groups.name.not.unique" />
					</h5>

					<div id="titleDiv" class="clearfix text-center mb-2">
						<fmt:message key="label.course.groups.name" />
						<input id="groupingName" type="text" class="form-control form-control-sm d-inline w-25 ms-1"
								<c:if test="${lessonMode}">
									readonly="readonly"
								</c:if>
						/>

						<button class="float-end btn btn-secondary btn-disable-on-downupload" onClick="javascript:saveGroups()">
							<i class="fa fa-save"></i> <fmt:message key="button.save" />
						</button>
					</div>
				</c:if>

				<div id="titleInstructions" class="clearfix text-center pb-2 border-bottom border-secondary">
					<span class="d-inline-block fst-italic mt-2">
						
					<fmt:message key="label.course.groups.edit.title" />
					</span>
					<button type="button" onClick="javascript:showPrintPage()" class="float-end btn btn-secondary">
						<i class="fa fa-print"></i> <fmt:message key="label.print"/>
					</button>
				</div>

				<div class="row" id="groupsTable">
					<div class="col-3 border-end border-secondary p-1" id="unassignedUserCell">
						<div class="text-center fw-bold my-2">
							<fmt:message key="label.course.groups.unassigned" />
							<span class="sortUsersButton"
								  title="<fmt:message key='label.course.groups.sort.tooltip' />">&#9650;</span>
						</div>
						<div class="userContainer"></div>
					</div>
					<div class="col-9 d-flex flex-wrap align-content-start" id="groupsCell">
						<c:if test="${not usedForBranching}">
							<div id="newGroupPlaceholder" class="groupContainer ms-2 mt-2">
								<div class="text-center text-primary"><fmt:message key="label.course.groups.add" /></div>
							</div>
						</c:if>
					</div>
				</div>
			</div>
		</div>

		<div class="accordion" id="advanced-settings-accordion">
			<c:if test="${lessonMode}">
				<div class="accordion-item">
					<h2 class="accordion-header" id="headingAdvanced">
						<button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" aria-expanded="false"
								data-bs-target="#course-grouping-advanced-settings" aria-controls="course-grouping-advanced-settings">
							<fmt:message key="label.advanced.settings" />
						</button>
					</h2>
					<div id="course-grouping-advanced-settings" class="accordion-collapse collapse" data-bs-parent="#advanced-settings-accordion"
						 aria-labelledby="headingAdvanced">
						<div class="accordion-body">
							<fmt:message key="label.save.as.course.grouping.hint" />
							<button id="save-course-grouping-button" class="btn btn-primary btn-disable-on-downupload d-block mt-2"
									onClick="javascript:saveAsCourseGrouping();">
								<i class="fa fa-save"></i>
								<fmt:message key="label.save.as.course.grouping" />
							</button>
						</div>
					</div>
				</div>
			</c:if>

			<div class="accordion-item">
				<h2 class="accordion-header" id="headingUploadGroupFile">
					<button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" aria-expanded="false"
							data-bs-target="#upload-group-file-settings" aria-controls="upload-group-file-settings">
						<fmt:message key="label.import.groups.from.template" />
					</button>
				</h2>
				<div id="upload-group-file-settings" class="accordion-collapse collapse" data-bs-parent="#advanced-settings-accordion"
					 aria-labelledby="headingUploadGroupFile">
					<div class="accordion-body">
						<fmt:message key="label.import.groups.from.template.description" />
						<button id="download-template" class="btn btn-primary btn-disable-on-downupload d-block mt-2 mb-4"
								onClick="javascript:downloadTemplate();">
							<i class="fa fa-download"></i> <fmt:message key="label.download.template" />
						</button>
						<form action="../monitoring/groupingUpload/importLearnersForGrouping.do" enctype="multipart/form-data" id="uploadForm">
							<input type="hidden" name="activityID" value='<c:out value="${param.activityID}" />' />
							<input type="hidden" name="lessonID" value='<c:out value="${lessonID}" />' />
							<lams:FileUpload5 fileFieldname="groupUploadFile" fileInputMessageKey="label.upload.group.spreadsheet"
											  uploadInfoMessageKey="-" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
							<button id="import" type="button" class="btn btn-primary btn-disable-on-downupload ms-3"
									onClick="javascript:importGroupsFromSpreadsheet()">
								<i class="fa fa-upload"></i> <fmt:message key="button.import" />
							</button>
						</form>
					</div>
				</div>
			</div>
		</div>

		<!-- A template which gets cloned when a group is added -->
		<div id="groupTemplate" class="groupContainer d-none p-1 ms-2 mt-2">
			<div class="userContainerTitle mb-2 d-flex justify-content-between align-middle">
				<c:if test="${not usedForBranching}">
					<i class="removeGroupButton fa fa-remove fs-3 text-danger ms-1 mt-1"
					   title="<fmt:message key='label.course.groups.remove.tooltip' />" ></i>
				</c:if>
				<input type="text" class="form-control form-control-sm d-inline" />
				<span class="sortUsersButton me-1 mt-1"
					  title="<fmt:message key='label.course.groups.sort.tooltip' />">&#9650;</span>
			</div>
			<div class="userContainer"></div>
		</div>

		<!-- Inner dialog placeholders -->
		<div id="save-course-grouping-dialog-contents" class="dialogContainer p-2">
			<span id="span-tooltip">
				<fmt:message key="label.enter.course.grouping.name"/>
			</span>

			<input id="dialog-course-grouping-name" type="text" class="form-control my-3" />

			<div class="text-end">
				<button id="dialog-close-button" class="btn btn-secondary btn-disable-on-downupload">
					<fmt:message key="label.cancel" />
				</button>
				<button id="dialog-save-button" class="btn btn-primary btn-disable-on-downupload">
					<fmt:message key="button.save" />
				</button>
			</div>
		</div>

		<div id="confirmationDialog" class="modal dialogContainer fade" tabindex="-1" role="dialog">
			<div class="modal-dialog  modal-dialog-centered">
				<div class="modal-content">
					<div class="modal-body">
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary" id="confirmationDialogCancelButton">Cancel</button>
						<button type="button" class="btn btn-primary" id="confirmationDialogConfirmButton">Confirm</button>
					</div>
				</div>
			</div>
		</div>
	</lams:Page5>
	</body>
</lams:html>