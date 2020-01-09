<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE))%></c:set>
<c:set var="lessonMode" value="${not empty param.activityID}" />

<lams:html>
<lams:head>
	<lams:css/>
	<link rel="stylesheet" href="${lams}css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="${lams}css/orgGroup.css" type="text/css" media="screen" />

	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.cookie.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/dialog.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/orgGroup.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/portrait.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/upload.js"></script>
	<script type="text/javascript">;
		var grouping = ${grouping},
			organisationId = grouping.organisationId,
			unassignedUsers = ${unassignedUsers},
			canEdit = ${canEdit},
			groupingActivityId = '${param.activityID}',
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
				SAVED_SUCCESSFULLY_LABEL : decoderDiv.html('<c:out value="${SAVED_SUCCESSFULLY_LABEL_VAR}" />').text(),
				<fmt:message key="label.import.warning.replace.groups" var="WARNING_REPLACE_GROUPS_VAR"/>
				WARNING_REPLACE_GROUPS_LABEL : decoderDiv.html('<c:out value="${WARNING_REPLACE_GROUPS_VAR}" />').text(),
				<fmt:message key="error.general.1" var="ERROR1_VAR"/>
				<fmt:message key="error.general.2" var="ERROR2_VAR"/>
				<fmt:message key="error.general.3" var="ERROR3_VAR"/>
				GENERAL_ERROR_LABEL : decoderDiv.html('<c:out value="${ERROR1_VAR}" />\n<c:out value="${ERROR2_VAR}" />\n<c:out value="${ERROR3_VAR}" />').text(),
				<fmt:message key="error.file.required" var="ERROR_FILE_REQUIRED_VAR"/>
				ERROR_FILE_REQUIRED_LABEL : decoderDiv.html('<c:out value="${ERROR_FILE_REQUIRED_VAR}" />').text(),
				<fmt:message key="error.file.wrong.format" var="ERROR_FILE_WRONG_FORMAT_VAR"/>
				ERROR_FILE_WRONG_FORMAT_LABEL : decoderDiv.html('<c:out value="${ERROR_FILE_WRONG_FORMAT_VAR}" />').text(),
				<fmt:message key="label.import.successful" var="LABEL_IMPORT_SUCCESSFUL_VAR"><fmt:param value="%1"/><fmt:param value="%2"/></fmt:message>
				LABEL_IMPORT_SUCCESSFUL_LABEL : decoderDiv.html('<c:out value="${LABEL_IMPORT_SUCCESSFUL_VAR}" />').text(),
			};
	</script>
	<!-- LDEV_NTU-7 Page jumps to the top when clicking the link in Grouping -->		
	<script type="text/javascript">
		jQuery(document).ready(function(){
		    jQuery('#noscrollinputid').on('click', function(event) {  
		         jQuery('#collapseUploadGroupFile').toggle('show');
		    });
		});

		jQuery(document).ready(function(){
		    jQuery('#noscrolladvancedid').on('click', function(event) {  
		         jQuery('#collapseAdvanced').toggle('show');
		    });
		});
	</script>
</lams:head>
<body>

<c:if test="${not lessonMode and (empty usedForBranching or usedForBranching eq false)}">

	<!-- It is shown when user tries to save a grouping with empty name -->
	<span id="grouping-name-blank-error" class="errorMessage">
		<fmt:message key="label.course.groups.name.blank" />
	</span>

	<!-- It is shown when user tries to save a grouping with a non unique name -->
	<span id="grouping-name-non-unique-error" class="errorMessage">
		<fmt:message key="label.course.groups.name.not.unique" />
	</span>
		
	<div id="titleDiv">
		<fmt:message key="label.course.groups.name" />
		<input id="groupingName" type="text"
			<c:if test="${not canEdit or lessonMode}">
				readonly="readonly"
			</c:if>
		/>
		
		<c:if test="${canEdit}">
			<button class="pull-right btn btn-default btn-disable-on-downupload" onClick="javascript:saveGroups()">
				<i class="fa fa-save"></i>
				<span><fmt:message key="button.save" /></span>
			</button>
		</c:if>
		<button class="pull-right btn btn-default btn-disable-on-downupload" 
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
	<span class="pull-right"><a href="#none" onClick="javascript:showPrintPage();"><i id="print-button" class="fa fa-print roffset10" title="<fmt:message key="label.print"/>"></i></a></span>
</div>

<table id="groupsTable">
	<tr>
		<td id="unassignedUserCell">
			<div class="userContainerTitle">
				<fmt:message key="label.course.groups.unassigned" />
				<span class="sortUsersButton"
				      title="<fmt:message key='label.course.groups.sort.tooltip' />">?</span>
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
		      title="<fmt:message key='label.course.groups.sort.tooltip' />">?</span>
	</div>
	<div class="userContainer"></div>
</div>

<c:if test="${lessonMode}">
	<c:set var="adTitle"><fmt:message key="label.advanced.settings" /></c:set>
	<!-- LDEV_NTU-7 Page jumps to the top when clicking the link in Grouping -->	
	<!-- lams:AdvancedAccordian title="${adTitle}"-->
	
		<div class="panel-group" id="accordionAdvanced" role="tablist" aria-multiselectable="true"> 
    <div class="panel panel-default" >
		 <div class="panel-heading collapsable-icon-left" id="headingAdvanced">
        	<span class="panel-title">
	    	<a id="noscrolladvancedid" class="collapsed" role="button" data-toggle="collapse" aria-expanded="false" aria-controls="collapseAdvanced" >
          	${adTitle}
        	</a>
      		</span>
        </div>
        
        <div id="collapseAdvanced" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingAdvanced">
	        <div id="course-grouping-advanced-settings">
				<fmt:message key="label.save.as.course.grouping.hint" />
				<button id="save-course-grouping-button" class="pull-right btn btn-sm btn-primary btn-disable-on-downupload" onClick="javascript:saveAsCourseGrouping();">
					<i class="fa fa-save"></i>
					<fmt:message key="label.save.as.course.grouping" />
				</button>
			</div>
		</div>
	</div>
	</div>
		
	<!--  /lams:AdvancedAccordian-->
</c:if>

<c:if test="${canEdit}">
<div class="panel-group ${lessonMode?'voffset5':'voffset20'}" id="accordionUploadGroupFile" role="tablist" aria-multiselectable="true"> 
    <div class="panel panel-default" >
        <div class="panel-heading collapsable-icon-left" id="headingUploadGroupFile">
	        	<span class="panel-title">
	        		<a id="noscrollinputid" class="collapsed" role="button" data-toggle="collapse" aria-expanded="false" aria-controls="collapseUploadGroupFile" >
		          		<fmt:message key="label.import.groups.from.template" />
		        	</a>
		     </span>
        </div>
        <div id="collapseUploadGroupFile" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingUploadGroupFile">
        		<div id="upload-group-file-settings">
			<fmt:message key="label.import.groups.from.template.description" />
			<div>
				<button id="download-template" class="btn btn-sm btn-default btn-disable-on-downupload" onClick="javascript:downloadTemplate();">
					<i class="fa fa-save"></i>
					<fmt:message key="label.download.template" />
				</button>
			</div>
			<div class="voffset5">
				<form action="../monitoring/groupingUpload/importLearnersForGrouping.do" enctype="multipart/form-data" id="uploadForm">
					<input type="hidden" name="activityID" value="${param.activityID}"/>
					<input type="hidden" name="lessonID" value="${lessonID}"/>
					<button id="import" type="button" class="pull-right btn btn-sm btn-primary btn-disable-on-downupload" onClick="javascript:importGroupsFromSpreadsheet();return false;">
						<fmt:message key="button.import" />
					</button>
					<lams:FileUpload fileFieldname="groupUploadFile" fileInputMessageKey="label.upload.group.spreadsheet"
						uploadInfoMessageKey="-" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
				</form>
			</div>
			<lams:WaitingSpinner id="attachmentArea_Busy"/>
			</div>
		</div>
	</div>
</div>
</c:if>
	

<!-- Inner dialog placeholders -->
<div id="save-course-grouping-dialog-contents" class="dialogContainer">
	<span id="span-tooltip">
		<fmt:message key="label.enter.course.grouping.name"/>
	</span>
		
	<div>
		<input id="dialog-course-grouping-name" type="text" size="40"/>
	</div>
			
	<div class="btn-group pull-right">
		<button id="dialog-save-button" class="btn btn-default btn-disable-on-downupload roffset5 ">
			<fmt:message key="button.save" />
		</button>
		<button id="dialog-close-button" class="btn btn-default btn-disable-on-downupload">
			<fmt:message key="label.cancel" />
		</button>
	</div>
</div>
	
</body>
</lams:html>