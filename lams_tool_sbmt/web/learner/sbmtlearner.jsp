<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}"/>
<c:set var="isLeadershipEnabled" value="${sessionMap.useSelectLeaderToolOuput}"/>
<c:set var="hasEditRight" value="${sessionMap.hasEditRight}"/>
<c:set var="language"><lams:user property="localeLanguage"/></c:set>


<lams:PageLearner title="${sessionMap.title}" toolSessionID="${sessionMap.toolSessionID}" >

	<link href="<lams:LAMSURL />css/uppy.min.css"    rel="stylesheet" type="text/css" />
	<link href="<lams:LAMSURL />css/uppy.custom5.css" rel="stylesheet" type="text/css" />
	<style>
		.btn-hide-on-min-not-met {
			display: none;
		}
	</style>

	<script src="<lams:LAMSURL />includes/javascript/jquery.timeago.js"></script>
	<script src="<lams:LAMSURL />includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(language)}.js"></script>
	<script src="<lams:LAMSURL />includes/javascript/uppy/uppy.min.js"></script>
	<c:choose>
		<c:when test="${language eq 'es'}">
			<script src="<lams:LAMSURL />includes/javascript/uppy/es_ES.min.js"></script>
		</c:when>
		<c:when test="${language eq 'fr'}">
			<script src="<lams:LAMSURL />includes/javascript/uppy/fr_FR.min.js"></script>
		</c:when>
		<c:when test="${language eq 'el'}">
			<script src="<lams:LAMSURL />includes/javascript/uppy/el_GR.min.js"></script>
		</c:when>
	</c:choose>

	<div id="instructions" class="instructions" aria-label="<fmt:message key='label.authoring.basic.instruction'/>">
		<c:out value="${sessionMap.instruction}" escapeXml="false" />
	</div>

	<div class="container-xxl">
		<div class="row">
			<div class="col-12 text-primary">
				<hr class="mx-5">
				<!-- notices and announcements -->
				<c:if test="${(sessionMap.mode == 'author' || sessionMap.mode == 'learner') && hasEditRight}">
					<c:if test="${sessionMap.lockOnFinish} && ${sessionMap.userFinished}">
						<!--  Lock when finished -->
						<lams:Alert5 id="lockWhenFinished" type="info">
							<fmt:message key="message.activityLocked" />
						</lams:Alert5>
					</c:if>

					<c:if test="${not empty sessionMap.submissionDeadline}">
						<lams:Alert5 id="submissionDeadline" type="warning" >
							<fmt:message key="authoring.info.teacher.set.restriction">
								<fmt:param>
									<lams:Date value="${sessionMap.submissionDeadline}" />
								</fmt:param>
							</fmt:message>
						</lams:Alert5>
					</c:if>

					<c:if test="${not sessionMap.userFinished || not sessionMap.lockOnFinish}">
						<c:choose>
							<c:when test="${sessionMap.minLimitUploadNumber != null && sessionMap.isMaxLimitUploadEnabled}">
								<c:set var="fileRestrictions">
									<fmt:message key="label.learner.upload.restrictions"/>: 
									<fmt:message key="label.should.upload.another">
										<fmt:param value="${sessionMap.minLimitUploadNumber}" />
									</fmt:message>&nbsp;<fmt:message key="label.learner.upload.restrictions.and"/>&nbsp;<fmt:message key="message.left.upload.limit">
										<fmt:param value="${sessionMap.maxLimitUploadNumber}" />
									</fmt:message>&nbsp;<fmt:message key="label.learner.upload.restrictions.files"/>
								</c:set>
							</c:when>
							<c:when test="${sessionMap.minLimitUploadNumber != null}">
								<c:set var="fileRestrictions">
									<fmt:message key="label.learner.upload.restrictions"/>: 
									<fmt:message key="label.should.upload.another">
										<fmt:param value="${sessionMap.minLimitUploadNumber}" />
									</fmt:message>&nbsp;<fmt:message key="label.learner.upload.restrictions.files"/>
								</c:set>
							</c:when>
							<c:when test="${sessionMap.isMaxLimitUploadEnabled}">
								<c:set var="fileRestrictions">
									<fmt:message key="label.learner.upload.restrictions"/>: 
									<fmt:message key="message.left.upload.limit">
										<fmt:param value="${sessionMap.maxLimitUploadNumber}" />
									</fmt:message>&nbsp;<fmt:message key="label.learner.upload.restrictions.files"/>
								</c:set>
							</c:when>						
						</c:choose>
						<c:if test="${not empty fileRestrictions}">
							<lams:Alert5 id="minLimitUploadNumber" close="true" type="warning">
								<c:out value="${fileRestrictions}" escapeXml="false" /> 
							</lams:Alert5>	
						</c:if>

					</c:if>
				</c:if>
				<lams:errors5/>

			</div>
		</div>

		<div class="row">
			<div class="col-12">
			<!-- upload form (we display it only if the user is not finished and lockedWhenFinished or no more files allowed) -->
			<c:if test="${!sessionMap.finishLock && !sessionMap.maxLimitReached && hasEditRight && sessionMap.mode != 'teacher'}">
				<form:form action="uploadFile.do" modelAttribute="learnerForm" id="learnerForm" method="post" enctype="multipart/form-data" onsubmit="return validateFileUpload();" >
					<input type="hidden" name="sessionMapID" value="${sessionMapID}"/>
					<input type="hidden" name="toolSessionID" value="${toolSessionID}" />
					<input type="hidden" name="tmpFileUploadId" value="${learnerForm.tmpFileUploadId}" />

					<!--File path row -->
					<div class="card lcard lcard-no-borders shadow">
						<div class="card-header lcard-header-lg lcard-header-button-border">
							<fmt:message key="label.learner.upload" />
						</div>

						<div class="card-body">
							<div class="form-floating">
								<label for="file-upload-area" class="d-none"><fmt:message key="label.learner.filePath" />&nbsp;<span style="color: red">*</span></label>

								<div id="file-upload-area" name="file-upload-area" class="m-1"></div>
							</div>

							<!--File Description -->
							<div class="form-floating m-3">
								<form:textarea id="description" aria-multiline="true" aria-required="true" required="true" cssClass="form-control" path="description" placeholder="-"></form:textarea>
								<label for="description"><fmt:message key="label.learner.fileDescription" /></label>
								<div id="desc-error-msg" class="text-danger" style="display: none;"></div>
							</div>

							<div id="fileHelpBlock" class="form-text mx-3 mb-4 mt-2" >
								<c:if test="${sessionMap.lockOnFinish}">
									<i class="fa-solid fa-triangle-exclamation text-danger" style="font-size: 1.2rem" aria-hidden="true"></i>&nbsp;<fmt:message key="message.warnLockOnFinish" />
								</c:if>
							</div>

							<c:if test="${hasEditRight}">
								<div class="form-group text-center">
									<button id="uploadButton" type="submit" <c:if test="${sessionMap.finishLock || sessionMap.maxLimitReached}">disabled="disabled"</c:if>
											class="btn btn-default btn-success btn-disable-on-submit"
											title='<fmt:message key="label.add.tip" />' >
										<i class="fa-solid fa-paper-plane" aria-hidden="true"></i> <fmt:message key="label.add" />
									</button>
								</div>
							</c:if>

						</div>
					</div>
				</form:form>

				<lams:WaitingSpinner id="attachmentArea_Busy"/>

				<hr/>
			</c:if>
			<!-- end div uppy form-->
			</div>
		</div>



		<!--Checks if the filesUploaded property of the SbmtLearnerForm is set -->
		<c:choose>
			<c:when test="${empty learner.filesUploaded && hasEditRight}">
				<div class="alert">
					<fmt:message key="label.learner.noUpload" />
				</div>
			</c:when>

			<c:otherwise>
				<h4 id="submittedFiles" class="mt-5">
					<fmt:message key="monitoring.user.submittedFiles" />
				</h4>
				<small class="text-muted"><c:out value="${fn:length(learner.filesUploaded)}" />&nbsp;<fmt:message key="label.files" /></small>

				<div role="list" class="mt-2 px-4" aria-labelledby="submittedFiles">
					<c:forEach var="file" items="${learner.filesUploaded}" varStatus="status">
						<div role="listitem" class="card my-3">
						<!--The name of the File -->
							<c:if test="${file.currentLearner}">
								<div class="card-header">
									<c:set var="downloadURL">
										<c:url value="/download?uuid=${file.displayUuid}&versionID=${file.versionID}&preferDownload=true" />
									</c:set>
									<i class="fa-regular fa-file" aria-label="false"></i> &nbsp;<a class="fw-bold" href="${downloadURL}" aria-label="<fmt:message key="label.download" />"><c:out value="${file.filePath}" /></a>
									<div class="float-end">
										<c:if test="${empty file.marks && hasEditRight}">
											<a href="javascript:deleteLearnerFile(${file.submissionID}, '${file.filePath}');" class="btn btn-primary btn-disable-on-submit">
												<i class="fa fa-trash" aria-label="<fmt:message key="label.monitoring.original.learner.file.delete"/>"></i>
											</a>
										</c:if>

										<a href="${downloadURL}"  class="btn btn-primary btn-disable-on-submit ">
											<i class="fa fa-download" title="<fmt:message key="label.download" />" aria-label="<fmt:message key="label.download" />"></i>
										</a>
									</div>
									<br>
									<small class="text-muted">
										<fmt:message key="label.learner.time" />&nbsp;<lams:Date value="${file.dateOfSubmission}" timeago="true"/>									
									</small>
								</div>
							</c:if>
						<div class="card-body">
						<!--The description of the File -->
							<div class="my-2" id="fileDescription">
								<lams:out value="${file.fileDescription}" escapeHtml="true" />
							</div>

						<!--Marks-->
						<c:if test="${sessionMap.isMarksReleased and not empty file.marks}">
							<div class="my-2" id="fileMarks">
								<hr>

									<span class="fw-bold"><fmt:message key="label.learner.marks" />:</span>
									<c:out value="${file.marks}" escapeXml="true" />
							</div>		

						</c:if>

						<!--Comments -->
						<c:if test="${sessionMap.isMarksReleased and not empty file.comments}">
							<div class="my-2" id="teacherComments">

									<span class="fw-bold"><fmt:message key="label.learner.comments" />:</span>
									<br/>
									<c:out value="${file.comments}" escapeXml="false" />
							</div>
						</c:if>

						<!--Marked file-->
						<c:if  test="${sessionMap.isMarksReleased and not empty file.markFileUUID}">
							<fmt:message key="label.monitor.mark.markedFile" />: <i class="fa fa-download" aria-hidden="true"></i>&nbsp;
								
									

									<c:set var="markFileDownloadURL">
										<c:url value="/download?uuid=${file.markFileUUID}&versionID=${file.markFileVersionID}&preferDownload=true" />
									</c:set>
									<a id="markedFile" href="${markFileDownloadURL}" title="<fmt:message key='label.monitor.mark.markedFile' />, <fmt:message key='label.download' />" class="">
										<c:out value="${file.markFileName}" />
									</a>

						</c:if>
						</div>
						</div>
					</c:forEach>
				</div>				

			</c:otherwise>
		</c:choose>

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
								class="btn btn-sm btn-primary btn-disable-on-submit" >
							<fmt:message key="label.edit" />
						</button>
					</c:if>
				</div>
			</div>
		</c:if>

		<!-- submit buttons -->
		<div class="activity-bottom-buttons">
			<c:if test="${sessionMap.mode != 'teacher'}">
				<c:choose>
					<c:when test="${sessionMap.reflectOn and (not sessionMap.userFinished)}">
						<button id="notebookButton" onclick="javascript:notebook();"
								class="btn btn-primary btn-disable-on-submit 
								   ${sessionMap.mode eq 'author' or empty sessionMap.minLimitUploadNumber ? '' : 'btn-hide-on-min-not-met'}">
							<fmt:message key="label.continue" />
						</button>
					</c:when>
					<c:otherwise>
						<button type="button"
								class="btn btn-primary btn-disable-on-submit na
								   ${sessionMap.mode eq 'author' or empty sessionMap.minLimitUploadNumber ? '' : 'btn-hide-on-min-not-met'}"
								id="finishButton">
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
		</div>
	</div>
	<div id="footer"></div>

	<script>
		checkNextGateActivity('finishButton', '${sessionMap.toolSessionID}', '', finish);
	
		var UPLOAD_FILE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_MAX_SIZE}"/>',
				// convert Java syntax to JSON
				EXE_FILE_TYPES = JSON.parse("[" + "${EXE_FILE_TYPES}".replace(/\.\w+/g, '"$&"') + "]"),
				<fmt:message key="error.attachment.executable" var="EXE_FILE_ERROR_VAR" />
				EXE_FILE_ERROR = decoderDiv.html('<c:out value="${EXE_FILE_ERROR_VAR}" />').text();
	
	
		$(document).ready(function() {
			$("time.timeago").timeago();
	
			if ($('#file-upload-area').length == 1) {
				initFileUpload('${learnerForm.tmpFileUploadId}', '${language}');
			}
	
			<c:if test="${sessionMap.mode != 'author' and sessionMap.minLimitUploadNumber != null}">
			var uploadedFilesNumber = +${learner.filesUploaded.size()};
			if (uploadedFilesNumber >= ${sessionMap.minLimitUploadNumber}) {
				$('.btn-hide-on-min-not-met').removeClass('btn-hide-on-min-not-met');
			}
			</c:if>
	
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
		function initFileUpload(tmpFileUploadId, language) {
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
				height: '410px',
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
			uppy.use(ScreenCapture, { target: Uppy.Dashboard });
	
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
				if (${sessionMap.mode eq 'author'}) {
					alert('<fmt:message key="label.should.upload.another"><fmt:param value="${sessionMap.minLimitUploadNumber}" /></fmt:message>' +
							'\n<fmt:message key="label.min.limit.preview"/>');
				} else {
					alert('<fmt:message key="label.should.upload.another"><fmt:param value="${sessionMap.minLimitUploadNumber}" /></fmt:message>');
					return false;
				}
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
</lams:PageLearner>

