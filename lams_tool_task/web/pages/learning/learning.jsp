<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="taskList" value="${sessionMap.taskList}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE))%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>
<c:set var="language"><lams:user property="localeLanguage"/></c:set>

<lams:PageLearner title="${taskList.title}" toolSessionID="${toolSessionID}">
	<link href="${lams}css/uppy.min.css" rel="stylesheet" type="text/css" />
	
	<script type="text/javascript" src="/lams/includes/javascript/uppy/uppy.min.js"></script>
	<c:choose>
		<c:when test="${language eq 'es'}">
			<script type="text/javascript" src="/lams/includes/javascript/uppy/es_ES.min.js"></script>
		</c:when>
		<c:when test="${language eq 'fr'}">
			<script type="text/javascript" src="/lams/includes/javascript/uppy/fr_FR.min.js"></script>
		</c:when>
		<c:when test="${language eq 'el'}">
			<script type="text/javascript" src="/lams/includes/javascript/uppy/el_GR.min.js"></script>
		</c:when>
		<c:when test="${language eq 'it'}">
			<script type="text/javascript" src="/lams/includes/javascript/uppy/it_IT.min.js"></script>
		</c:when>
	</c:choose>
	<lams:JSImport src="includes/javascript/upload.js" />
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${toolSessionID}', '', finishSession);
		
		var UPLOAD_FILE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_MAX_SIZE}"/>',
			// convert Java syntax to JSON
	       EXE_FILE_TYPES = JSON.parse("[" + "${EXE_FILE_TYPES}".replace(/\.\w+/g, '"$&"') + "]"),
		   EXE_FILE_ERROR = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.attachment.executable" /></spring:escapeBody>';

		function disableButtons() {
			// logic to disable all buttons depends on contained pages so to avoid future changes breaking this code and stopping the page working, wrap in a try.
			try {
				$('.btn-disable-on-submit').prop('disabled', true);
				var addTaskArea = document.getElementById('reourceInputArea');
				if ( addTaskArea && addTaskArea.contentWindow.disableButtons  ) {
					addTaskArea.contentWindow.disableButtons();
				}
				
				// show the waiting area during the upload
				var div = document.getElementById("attachmentArea_Busy");
				if(div != null){
					div.style.display = '';
				}
			} catch(err) {}
		}
	
		function validateFiles() {
			var files = uppy.getFiles();
			if (files.length == 0) {
				return false;
			} else {
				var file = files[0];
				if ( ! validateShowErrorNotExecutable(file, '<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.attachment.executable"/></spring:escapeBody>', false, '${EXE_FILE_TYPES}')
						 || ! validateShowErrorFileSize(file, '${UPLOAD_FILE_MAX_SIZE}', '<spring:escapeBody javaScriptEscape="true"><fmt:message key="errors.maxfilesize"/></spring:escapeBody>') ) {
					return false;
				}
			}
			disableButtons();
			return true;
		}
	
		function checkNew() {
			document.location.href = "<c:url value="/learning/start.do"/>?mode=${mode}&toolSessionID=${toolSessionID}";
			return false;
		}

		function completeItem(itemUid) {
			disableButtons() ;
			document.location.href = "<c:url value="/learning/completeItem.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&itemUid="
					+ itemUid;
			return false;
		}

		function finishSession() {
			disableButtons() ;
			document.location.href = '<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}

		function continueReflect() {
			disableButtons() ;
			document.location.href = '<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}

		function showMessage(url) {
			var area = document.getElementById("reourceInputArea");
			if (area != null) {
				area.style.width = "100%";
				area.src = url;
				area.style.display = "block";
			}
			var elem = document.getElementById("saveCancelButtons");
			if (elem != null) {
				elem.style.display = "none";
			}
			location.hash = "resourceInputArea";
		}

		function hideMessage() {
			var area = document.getElementById("reourceInputArea");
			if (area != null) {
				area.style.width = "0px";
				area.style.height = "0px";
				area.style.display = "none";
			}
			var elem = document.getElementById("saveCancelButtons");
			if (elem != null) {
				elem.style.display = "block";
			}
		}

		function addNewComment(itemUid) {
			var comment = $("#comment-" + itemUid).val();
			//skip submition of empty comments
			if (!comment) {
				return;
			}
			
			$("#comment-list-" + itemUid).load(
				"<c:url value="/learning/addNewComment.do"/>",
				{
					itemUid: itemUid,
					sessionMapID: "${sessionMapID}",
					comment: comment
				},
				function() {
					//show complete item button in case comment is required and only its absence prevented it from completion
					var i = $("#item-faminus-" + itemUid);
					if (eval(i.data("waiting-for-comment"))) {
						i.parent().replaceWith( '<button type="button" onClick="javascript:completeItem(' + itemUid + ')" class="complete-item-button btn btn-success no-shadow">' + 
											'<i class="fa-solid fa-pen-to-square fa-xl me-1"></i>' +
											'<fmt:message key="label.completed" />' +
										'</button> '
						);
					}
				}
			);
		}

		/**
		 * Initialised Uppy as the file upload widget
		 */
		var uppy;
		function initFileUpload(target, tmpFileUploadId, language) {
			  var uppyProperties = {
				  id : 'uppy-' + target,
				  // upload immediately 
				  autoProceed: true,
				  allowMultipleUploads: true,
				  debug: false,
				  restrictions: {
					// taken from LAMS configuration
				    maxFileSize: +UPLOAD_FILE_MAX_SIZE,
				    maxNumberOfFiles: 5
				  },
				  meta: {
					  // all uploaded files go to this subdir in LAMS tmp dir
					  // its format is: upload_<userId>_<timestamp>
					  'tmpFileUploadId' : tmpFileUploadId,
					  'largeFilesAllowed' : false
				  },
				  onBeforeFileAdded: function(currentFile, files) {
					  var name = currentFile.data.name,
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
				case 'it' : uppyProperties.locale = Uppy.locales.it_IT; break;
			  }
			  
			  
			  uppy = Uppy.Core(uppyProperties);
			  // upload using Ajax
			  uppy.use(Uppy.XHRUpload, {
				  endpoint: LAMS_URL + 'tmpFileUpload',
				  fieldName : 'file',
				  // files are uploaded one by one
				  limit : 1
			  });
			  
			  uppy.use(Uppy.DragDrop, {
				  target: target,
				  inline: true,
				  height: 120,
				  width: '100%'
				});
			  
			  uppy.use(Uppy.Informer, {
				  target: target
			  });
			  
			  uppy.use(Uppy.StatusBar, {
				  target: target,
				  hideAfterFinish: false,
				  hideUploadButton: true,
				  hideRetryButton: true,
				  hidePauseResumeButton: true,
				  hideCancelButton: true
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
	</script>
	<script type="text/javascript" src="/lams/includes/javascript/jquery.timeago.js"></script>

	<div id="container-main">
			
		<!--Task Information-->

		<lams:errors5/>

		<c:if test="${not empty taskList.submissionDeadline}">
			<lams:Alert5 id="submissionDeadline" type="info" close="true">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${sessionMap.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert5>
		</c:if>

		<c:if test="${(mode != 'teacher') && taskList.lockWhenFinished}">
			<lams:Alert5 id="lockWhenFinished" type="info" close="true">
				<c:choose>
					<c:when test="${finishedLock}">
						<fmt:message key="label.learning.responses.locked.reminder" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.learning.responses.locked" />
					</c:otherwise>
				</c:choose>
			</lams:Alert5>
		</c:if>

		<c:if test="${(mode != 'teacher') && taskList.sequentialOrder}">
			<lams:Alert5 id="sequentialOrder" type="info" close="true">
				<fmt:message key="label.learning.info.sequential.order" />
			</lams:Alert5>
		</c:if>

		<c:if test="${fn:length(taskList.minimumNumberTasksErrorStr) > 0}">
			<lams:Alert5 id="minimumNumberTasks" type="info">
				<c:out value="${taskList.minimumNumberTasksErrorStr}" />
			</lams:Alert5>
		</c:if>

		<div id="instructions" class="instructions">
			<c:out value="${taskList.instructions}" escapeXml="false" />
		</div>

		<!--TaskListItems table-->

		<%@ include file="/pages/learning/parts/itemlist.jsp"%>

		<!--"Check for new" button-->

		<p class="form-text">
			<i class="fa-solid fa-asterisk text-danger"></i>
			 - <fmt:message key="label.learning.required.tasks" />
		</p>

		<!--Reflection-->
		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<lams:NotebookReedit
				reflectInstructions="${sessionMap.reflectInstructions}"
				reflectEntry="${sessionMap.reflectEntry}"
				isEditButtonEnabled="${mode != 'teacher'}"
				notebookHeaderLabelKey="label.monitoring.summary.title.reflection"/>
		</c:if>

		<!--Bottom buttons-->

		<c:set var="isRequiredTasksCompleted" value="${true}" />
		<c:forEach var="itemDTO" items="${sessionMap.itemDTOs}">
			<c:if test="${itemDTO.taskListItem.required && not itemDTO.taskListItem.complete}">
				<c:set var="isRequiredTasksCompleted" value="${false}" />
			</c:if>
		</c:forEach>
		<c:if test="${(mode != 'teacher')}">
			<c:if test="${isRequiredTasksCompleted && taskList.monitorVerificationRequired && !sessionMap.userVerifiedByMonitor && (mode != 'author')}">
				<lams:Alert5 type="warning">
					<fmt:message key="label.learning.wait.for.monitor.verification" />
				</lams:Alert5>
			</c:if>
		
			<div class="activity-bottom-buttons">
				<c:if test="${isRequiredTasksCompleted}">
					<c:choose>
						<c:when test="${taskList.monitorVerificationRequired && !sessionMap.userVerifiedByMonitor && (mode != 'author')}">
						</c:when>
	
						<c:when test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
							<button type="button" id="finishButton" onclick="return continueReflect()"
								class="btn btn-primary btn-disable-on-submit na">
								<fmt:message key="label.continue" />
							</button>
						</c:when>
	
						<c:otherwise>
							<button type="submit" id="finishButton" class="btn btn-primary btn-disable-on-submit na">
								<c:choose>
									<c:when test="${sessionMap.isLastActivity}">
										<fmt:message key="label.submit" />
									</c:when>
									<c:otherwise>
										<fmt:message key="label.finished" />
									</c:otherwise>
								</c:choose>
							</button>
						</c:otherwise>
					</c:choose>
				</c:if>
				
				<button type="button" onclick="return checkNew()" class="btn btn-sm btn-secondary btn-disable-on-submit btn-icon-refresh me-2"> 
					<fmt:message key="label.learning.check.for.new" />
				</button>
				
				<!--"Add new task" Area-->
				
				<c:if test="${(not finishedLock) && taskList.allowContributeTasks}">
					<button type="button" onclick="javascript:showMessage('<lams:WebAppURL/>learning/addtask.do?sessionMapID=${sessionMapID}&mode=${mode}');"
							class="btn btn-sm btn-secondary btn-disable-on-submit me-2">
						<i class="fa fa-sm fa-plus-circle"></i>&nbsp;
						<fmt:message key="label.authoring.basic.add.task" />
					</button>
				</c:if>
			</div>
		</c:if>

		<c:if test="${(not finishedLock) && taskList.allowContributeTasks}">
			<iframe onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
					id="reourceInputArea" name="reourceInputArea" class="mt-3"
					style="width: 0px; height: 0px; border: 0px; display: none"
					frameborder="no" scrolling="no" title="<fmt:message key='label.authoring.basic.add.task' />"> </iframe>
		</c:if>
	</div>
</lams:PageLearner>
