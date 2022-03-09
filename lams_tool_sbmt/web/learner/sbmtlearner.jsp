<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}"/>
<c:set var="isLeadershipEnabled" value="${sessionMap.useSelectLeaderToolOuput}"/>
<c:set var="hasEditRight" value="${sessionMap.hasEditRight}"/>
<lams:html>
<lams:head>
		
	<title><fmt:message key="tool.display.name" /></title>
	<%@ include file="/common/header.jsp"%>
	
	<link href="/lams/css/uppy.min.css" rel="stylesheet" type="text/css" />
	
	<script type="text/javascript" src="${lams}/includes/javascript/uppy/uppy.min.js"></script>
	<c:choose>
		<c:when test="${language eq 'es'}">
			<script type="text/javascript" src="${lams}/includes/javascript/uppy/es_ES.min.js"></script>
		</c:when>
		<c:when test="${language eq 'fr'}">
			<script type="text/javascript" src="${lams}/includes/javascript/uppy/fr_FR.min.js"></script>
		</c:when>
		<c:when test="${language eq 'el'}">
			<script type="text/javascript" src="${lams}/includes/javascript/uppy/el_GR.min.js"></script>
		</c:when>
	</c:choose>
		<lams:JSImport src="learning/includes/javascript/gate-check.js" />
		
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${sessionMap.toolSessionID}', '', finish);
		
		var LAMS_URL = '<lams:LAMSURL/>',
			UPLOAD_FILE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_MAX_SIZE}"/>',
			// convert Java syntax to JSON
	       EXE_FILE_TYPES = JSON.parse("[" + "${EXE_FILE_TYPES}".replace(/\.\w+/g, '"$&"') + "]"),
	       EXE_FILE_ERROR = '<fmt:message key="error.attachment.executable"/>';
		
		
		$(document).ready(function() {
			$("time.timeago").timeago();
			
			if ($('#file-upload-area').length == 1) {
				initFileUpload('${learnerForm.tmpFileUploadId}', '<lams:user property="localeLanguage"/>');
			}

			<%-- Connect to command websocket only if it is learner UI --%>
			<c:if test="${isLeadershipEnabled and sessionMap.mode == 'learner'}">
				// command websocket stuff for refreshing
				// trigger is an unique ID of page and action that command websocket code in Page.tag recognises
				commandWebsocketHookTrigger = 'submit-files-leader-change-refresh-${sessionMap.toolSessionID}';
				// if the trigger is recognised, the following action occurs
				commandWebsocketHook = function() {
					// in case in the address bar there is refresh.do, direct to the main learner URL
					location.href = '<lams:WebAppURL />learning/learner.do?toolSessionID=${sessionMap.toolSessionID}';
				};
			</c:if>
		});
		
		/**
		 * Initialise Uppy as the file upload widget
		 */
		function initFileUpload(tmpFileUploadId, singleFileUpload, language) {
			  var uppyProperties = {
				  // upload immediately 
				  autoProceed: true,
				  allowMultipleUploads: true,
				  debug: false,
				  restrictions: {
					// taken from LAMS configuration
				    maxFileSize: +UPLOAD_FILE_MAX_SIZE,
				    maxNumberOfFiles: ${sessionMap.isMaxLimitUploadEnabled ? sessionMap.maxLimitUploadNumber - learner.filesUploaded.size() : 10}
				  },
				  meta: {
					  // all uploaded files go to this subdir in LAMS tmp dir
					  // its format is: upload_<userId>_<timestamp>
					  'tmpFileUploadId' : tmpFileUploadId,
					  'largeFilesAllowed' : false
				  },
				  onBeforeFileAdded: function(currentFile, files) {
					  var name = currentFile.data.name || currentFile.name,
					  	  extensionIndex = name.lastIndexOf('.'),
					  	  valid = extensionIndex < 0 || !EXE_FILE_TYPES.includes(name.substring(extensionIndex).trim().toLowerCase());
					  if (!valid) {
						  uppy.info(EXE_FILE_ERROR, 'error', 10000);
					  }
					  
					  return valid;
				    }
			  };
			  
			  switch(language) {
			  	case 'es' : uppyProperties.locale = Uppy.locales.es_ES; break; 
				case 'fr' : uppyProperties.locale = Uppy.locales.fr_FR; break; 
				case 'el' : uppyProperties.locale = Uppy.locales.el_GR; break; 
			  }
			  
			  
			  var uppy = Uppy.Core(uppyProperties);
			  // upload using Ajax
			  uppy.use(Uppy.XHRUpload, {
				  endpoint: LAMS_URL + 'tmpFileUpload',
				  fieldName : 'file',
				  // files are uploaded one by one
				  limit : 1
			  });
			  
			  uppy.use(Uppy.Dashboard, {
				  target: '#file-upload-area',
				  inline: true,
				  height: 300,
				  width: '100%',
				  showProgressDetails : true,
				  hideRetryButton : true,
				  hideCancelButton : true,
				  showRemoveButtonAfterComplete: true,
				  proudlyDisplayPoweredByUppy: false
			  });
			  
			  uppy.use(Uppy.Webcam, {
				  target: Uppy.Dashboard,
				  modes: ['picture']
			  });
			  
			  uppy.on('upload-success', (file, response) => {
				  // if file name was modified by server, reflect it in Uppy
				  file.meta.name = response.body.name;
			  });
			  
			  uppy.on('file-removed', (file, reason) => {
				  if (reason === 'removed-by-user') {
					 // delete file from temporary folder on server
				    $.ajax({
				    	url :  LAMS_URL + 'tmpFileUploadDelete',
				    	data : {
				    		'tmpFileUploadId' : tmpFileUploadId,
				    		'name' : file.meta.name
				    	}
				    })
				  }
			  })
		}
		
		function finish() {
			var finishUrl = "<lams:WebAppURL />learning/finish.do?sessionMapID=${sessionMapID}";
			return validateFinish(finishUrl);
		}
		function notebook() {
			var continueUrl = "<lams:WebAppURL />learning/newReflection.do?sessionMapID=${sessionMapID}";
			return validateFinish(continueUrl);
		}
		function validateFinish(tUrl) {
			var uploadedFilesNumber = +${learner.filesUploaded.size()};

			//enforce min files upload limit
			<c:if test="${sessionMap.minLimitUploadNumber != null}">
				if (uploadedFilesNumber < ${sessionMap.minLimitUploadNumber}) {
					alert('<fmt:message key="label.should.upload.another"><fmt:param value="${sessionMap.minLimitUploadNumber}" /></fmt:message>');
					return false;
				}
			</c:if>

			//let user confirm zero files upload
			if (uploadedFilesNumber == 0) {
				if (${sessionMap.lockOnFinish}) {
					if (!confirm("<fmt:message key='learner.finish.without.upload'/>")) {
						return false;
					}
				} else {
					if (!confirm("<fmt:message key='messsage.learner.finish.confirm'/>")) {
						return false;
					}
				}
			}

			disableButtons();
			location.href = tUrl;
		}
		
		function clearFileError(errDivId) {
			if ( ! errDivId || errDivId.length == 0 ) {
				errDivId = 'file-error-msg';
			}
			var errDiv = $('#'+errDivId);
			errDiv.empty();
			errDiv.css( "display", "none" );
		}
		
		function showFileError(error, errDivId) {
			if ( ! errDivId || errDivId.length == 0 ) {
				errDivId = 'file-error-msg';
			}
			var errDiv = $('#'+errDivId);
			if ( errDiv.length > 0 ) {
				errDiv.append(error);
				errDiv.css( "display", "block" );
			} else {
				alert(error);
			}
		}
		
		function validateFileUpload() {
			var valid = true;

			// check description
			clearFileError("desc-error-msg");
			if ( $('#description').val().trim().length == 0 ) {
				var requiredMsg = '<fmt:message key="errors.required"><fmt:param><fmt:message key="label.learner.fileDescription"/></fmt:param></fmt:message>';
				showFileError(requiredMsg, "desc-error-msg");
				valid = false;
			}
			
			if ( valid ) {
				disableButtons();
			}
			return valid;
		}
		
		function disableButtons() {
			// do not disable the file button or the file will be missing on the upload.
			$('.btn-disable-on-submit').prop('disabled', true);
			$('a.btn-disable-on-submit').hide(); // links must be hidden, cannot be disabled
			
			// show the waiting area during the upload
			var div = document.getElementById("attachmentArea_Busy");
			if(div != null){
				div.style.display = '';
			}
		}
		
		function deleteLearnerFile(detailId, filename) {
			var msg = '<fmt:message key="message.monitor.confirm.original.learner.file.delete"/>';
			msg = msg.replace('{0}', filename);
			var answer = confirm(msg);
			if (answer) {	
				$.ajax({
					url: '<c:url value="/learning/deleteLearnerFile.do"/>',
			        data: 'detailId=' + detailId,
			        success: function () {
			          	document.location.href = "<lams:WebAppURL />learning/${sessionMap.mode}.do?toolSessionID=${sessionMap.toolSessionID}";
			        },
			        error: function(error){
			         	alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
			            alert("responseText: "+xhr.responseText);
			        }
			    });
			}
		}
	</script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${sessionMap.title}" formID="learnerForm">
		<div class="panel">
			<c:out value="${sessionMap.instruction}" escapeXml="false" />
		</div>

		<!-- notices and announcements -->
		
		<c:if test="${(sessionMap.mode == 'author' || sessionMap.mode == 'learner') && hasEditRight}">
			<c:if test="${sessionMap.lockOnFinish}">
				<!--  Lock when finished -->
				<lams:Alert id="lockWhenFinished" type="info" close="true">
					<c:choose>
						<c:when test="${sessionMap.userFinished}">
							<fmt:message key="message.activityLocked" />
						</c:when>
						<c:otherwise>
							<fmt:message key="message.warnLockOnFinish" />
						</c:otherwise>
					</c:choose>
				</lams:Alert>
			</c:if>

			<c:if test="${not empty sessionMap.submissionDeadline}">
				<lams:Alert id="submissionDeadline" type="info" close="true">
					<fmt:message key="authoring.info.teacher.set.restriction">
						<fmt:param>
							<lams:Date value="${sessionMap.submissionDeadline}" />
						</fmt:param>
					</fmt:message>
				</lams:Alert>
			</c:if>

			<c:if test="${not sessionMap.userFinished || not sessionMap.lockOnFinish}">
				<c:if test="${sessionMap.isMaxLimitUploadEnabled}">
					<lams:Alert id="limitUploads" close="true" type="info">
						<fmt:message key="message.left.upload.limit">
							<fmt:param value="${sessionMap.maxLimitUploadNumber}" />
						</fmt:message>
					</lams:Alert>
				</c:if>
				
				<c:if test="${sessionMap.minLimitUploadNumber != null}">
					<lams:Alert id="minLimitUploadNumber" close="true" type="info">
						<fmt:message key="label.should.upload.another">
							<fmt:param value="${sessionMap.minLimitUploadNumber}" />
						</fmt:message>
					</lams:Alert>
				</c:if>
			</c:if>
		</c:if>
		<lams:errors/>

		<!--Checks if the filesUploaded property of the SbmtLearnerForm is set -->
		<c:choose>
			<c:when test="${empty learner.filesUploaded && hasEditRight}">
				<div class="alert">
					<fmt:message key="label.learner.noUpload" />
				</div>
			</c:when>

			<c:otherwise>
				<div class="voffset10">
					<fmt:message key="monitoring.user.submittedFiles" />: <c:out value="${fn:length(learner.filesUploaded)}" />
				</div>
				
				<table class="table table-condensed voffset20">
					<c:forEach var="file" items="${learner.filesUploaded}" varStatus="status">
					
						<!--The name of the File -->
						<tr class="active">
							<td colspan="${file.currentLearner ? 2 : 3}">
								<c:out value="${status.count}" />) <c:out value="${file.filePath}" />
								
								<c:if test="${file.currentLearner}">
									<c:set var="downloadURL">
										<c:url value="/download?uuid=${file.uuID}&versionID=${file.versionID}&preferDownload=true" />
									</c:set>
									</td>
									
									<td>
										<c:if test="${empty file.marks && hasEditRight}">
										 	<a href="javascript:deleteLearnerFile(${file.submissionID}, '${file.filePath}');" class="btn btn-default btn-disable-on-submit pull-right">
							                      <i class="fa fa-trash" title="<fmt:message key="label.monitoring.original.learner.file.delete" />"></i> <span class="hidden-xs"></span>
						                 	</a>
						                 </c:if>
					                 
										<a href="${downloadURL}" title="<fmt:message key="label.download" />" class="btn btn-default btn-disable-on-submit pull-right">
											<i class="fa fa-download" ></i>
										</a>
									</td>
								</c:if>
							</td>
						</tr>
						
						<!--The description of the File -->
						<tr>
							<td colspan="3">
								<lams:out value="${file.fileDescription}" escapeHtml="true" />
								
								<div class="text-muted voffset10">
									<fmt:message key="label.learner.time" />&nbsp;
									<lams:Date value="${file.dateOfSubmission}" timeago="true"/>
								</div>
							</td>
						</tr>
	
						<!--Comments -->
						<c:if test="${sessionMap.isMarksReleased and not empty file.comments}">
							<tr>
								<td colspan="3">
									<fmt:message key="label.learner.comments" />:
									<c:out value="${file.comments}" escapeXml="false" />
								</td>
							</tr>
						</c:if>
		
						<!--Marks-->
						<c:if test="${sessionMap.isMarksReleased and not empty file.marks}">
							<tr>
								<td colspan="3">
									<fmt:message key="label.learner.marks" />:
									<c:out value="${file.marks}" escapeXml="true" />
								</td>
							</tr>
						</c:if>
						
						<!--Marked file-->
						<c:if  test="${sessionMap.isMarksReleased and not empty file.markFileUUID}">	
							<tr>
								<td><fmt:message key="label.monitor.mark.markedFile" /></td>
								<td>
									<c:out value="${file.markFileName}" />
								</td>
								<td>
									<c:set var="markFileDownloadURL">
										<c:url value="/download?uuid=${file.markFileUUID}&versionID=${file.markFileVersionID}&preferDownload=true" />
									</c:set>
									<a href="${markFileDownloadURL}" title="<fmt:message key='label.download' />" class="btn btn-default btn-disable-on-submit pull-right">
										<i class="fa fa-download"></i>
									</a>
								</td>
							</tr>
						</c:if>	
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>

		<hr width="100%"/>

		<!-- upload form (we display it only if the user is not finished and lockedWhenFinished or no more files allowed) -->
		<c:if test="${!sessionMap.finishLock && !sessionMap.maxLimitReached && hasEditRight && sessionMap.mode != 'teacher'}">
			<form:form action="uploadFile.do" modelAttribute="learnerForm" id="learnerForm" method="post" enctype="multipart/form-data" onsubmit="return validateFileUpload();" >
				<input type="hidden" name="sessionMapID" value="${sessionMapID}"/>
				<input type="hidden" name="toolSessionID" value="${toolSessionID}" />
				<input type="hidden" name="tmpFileUploadId" value="${learnerForm.tmpFileUploadId}" />
				
				<!--File path row -->
				<div class="panel panel-default">
					<div class="panel-heading panel-title">
						<fmt:message key="label.learner.upload" />
					</div>
					
					<div class="panel-body">
						<div class="form-group">
							<label for="file-upload-area"><fmt:message key="label.learner.filePath" />&nbsp;<span style="color: red">*</span></label>
							
							<div id="file-upload-area" class="voffset20"></div>
						</div>
						
						<!--File Description -->
						<div class="form-group">	
							<label for="description"><fmt:message key="label.learner.fileDescription" />&nbsp;<span
								style="color: red">*</span></label>
							<form:textarea id="description" cssClass="form-control" path="description"></form:textarea>
							<div id="desc-error-msg" class="text-danger" style="display: none;"></div>
						</div>
						
						<p class="help-block"><small><fmt:message key="errors.required"><fmt:param>*</fmt:param></fmt:message></small></p>
						
							<c:if test="${hasEditRight}">
								<div class="form-group text-center">
									<h4><fmt:message key="label.add.hint" /></h4>
									<button id="uploadButton" type="submit" <c:if test="${sessionMap.finishLock || sessionMap.maxLimitReached}">disabled="disabled"</c:if>
										class="btn btn-lg btn-default btn-primary btn-disable-on-submit">
										<i class="fa fa-xs fa-plus"></i> <fmt:message key="label.add" />
									</button>
								</div>
							</c:if>
						
					</div>
				</div>
			</form:form>
				
			<lams:WaitingSpinner id="attachmentArea_Busy"/>
		</c:if>
		
		<!-- reflection -->

		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="panel panel-default">
				<div class="panel-heading panel-title">
					<fmt:message key="title.reflection" />
				</div>
				<div class="panel-body">
					<div class="panel">
						<lams:out escapeHtml="true" value="${sessionMap.reflectInstructions}" />
					</div>

					<div class="bg-warning" style="padding: 5px">
						<c:choose>
							<c:when test="${empty learner.reflect}">
								<p>
									<em> <fmt:message key="message.no.reflection.available" />
									</em>
								</p>
							</c:when>
							<c:otherwise>
								<p>
									<lams:out escapeHtml="true" value="${learner.reflect}" />
								</p>
							</c:otherwise>
						</c:choose>
					</div>

					<c:if test="${sessionMap.mode != 'teacher'}">
					<button id="notebookButton" name="notebookButton" style="margin-top: 10px" onclick="javascript:notebook();"
						class="btn btn-sm btn-primary btn-disable-on-submit pull-left" >
						<fmt:message key="label.edit" />
					</button>
					</c:if>
				</div>
			</div>
		</c:if>
		
		<!-- submit buttons -->

		<c:if test="${sessionMap.mode != 'teacher'}">
			<c:choose>
				<c:when test="${sessionMap.reflectOn and (not sessionMap.userFinished)}">
					<button id="notebookButton" onclick="javascript:notebook();" class="btn btn-primary btn-disable-on-submit pull-right">
						<fmt:message key="label.continue" />
					</button>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn btn-primary btn-disable-on-submit pull-right na" id="finishButton">
						<c:choose>
							<c:when test="${isLastActivity}">
								<fmt:message key="button.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.finish" />
							</c:otherwise>
						</c:choose>
					</button>
				</c:otherwise>
			</c:choose>
		</c:if>

		<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>