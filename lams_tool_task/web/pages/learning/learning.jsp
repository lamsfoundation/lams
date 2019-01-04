<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>

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

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/upload.js"></script>
	<script type="text/javascript">

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
			var fileSelect = document.getElementById('uploadButton');
			var files = fileSelect.files;
			if (files.length == 0) {
				return false;
			} else {
				var file = files[0];
				if ( ! validateShowErrorNotExecutable(file, '<fmt:message key="error.attachment.executable"/>', false, '${EXE_FILE_TYPES}')
						 || ! validateShowErrorFileSize(file, '${UPLOAD_FILE_MAX_SIZE}', '<fmt:message key="errors.maxfilesize"/>') ) {
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
				}
			);
		}

	</script>
	<script type="text/javascript" src="/lams/includes/javascript/jquery.timeago.js"></script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${taskList.title}"> 

		<!--Task Information-->

		<lams:errors/>

		<c:if test="${not empty taskList.submissionDeadline}">
			<lams:Alert id="submissionDeadline" type="info" close="true">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${sessionMap.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert>
		</c:if>


		<div class="panel" id="instructions">
			<c:out value="${taskList.instructions}" escapeXml="false" />
		</div>

		<c:if test="${(mode != 'teacher') && taskList.lockWhenFinished}">
			<lams:Alert id="lockWhenFinished" type="info" close="true">
				<c:choose>
					<c:when test="${finishedLock}">
						<fmt:message key="label.learning.responses.locked.reminder" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.learning.responses.locked" />
					</c:otherwise>
				</c:choose>
			</lams:Alert>
		</c:if>

		<c:if test="${(mode != 'teacher') && taskList.sequentialOrder}">
			<lams:Alert id="sequentialOrder" type="info" close="true">
				<fmt:message key="label.learning.info.sequential.order" />
			</lams:Alert>
		</c:if>

		<!--TaskListItems table-->

		<%@ include file="/pages/learning/parts/itemlist.jsp"%>

		<!--"Check for new" button-->

		<p class="help-block">
			* -
			<fmt:message key="label.learning.required.tasks" />
		</p>

		<c:if test="${mode != 'teacher'}">
			<p>
				<button onclick="return checkNew()" class="btn btn-sm btn-default btn-disable-on-submit"> 
				<fmt:message key="label.learning.check.for.new" />
				</button>
			</p>
		</c:if>

		<!--"Add new task" Area-->

		<c:if test="${mode != 'teacher' && (not finishedLock)}">
			<c:if test="${taskList.allowContributeTasks}">

				<p>
					<button onclick="javascript:showMessage('<lams:WebAppURL/>learning/addtask.do?sessionMapID=${sessionMapID}&mode=${mode}');"
						class="btn btn-sm btn-default btn-disable-on-submit"><i class="fa fa-sm fa-plus-circle"></i>&nbsp;<fmt:message
							key="label.authoring.basic.add.task" /> </button>
				<p>

					<iframe onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
						id="reourceInputArea" name="reourceInputArea" style="width: 0px; height: 0px; border: 0px; display: none"
						frameborder="no" scrolling="no"> </iframe>
			</c:if>
		</c:if>

		<!--Reflection-->

		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
						<div class="panel panel-default">
							<div class="panel-heading panel-title">
								<fmt:message key="label.monitoring.summary.title.reflection" />
							</div>
							<div class="panel-body">
								<div class="panel">
									<lams:out escapeHtml="true" value="${sessionMap.reflectInstructions}" />
								</div>

								<div class="form-group">

									<c:choose>
										<c:when test="${empty sessionMap.reflectEntry}">
											<p>
												<em> <fmt:message key="message.no.reflection.available" />
												</em>
											</p>
										</c:when>
										<c:otherwise>
											<p>
												<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
											</p>
										</c:otherwise>
									</c:choose>

									<c:if test="${mode != 'teacher'}">
										<button type="submit" id="finishButton" onclick="return continueReflect()"
											class="btn btn-sm btn-default pull-left voffset10 btn-disable-on-submit">
											<fmt:message key="label.edit" />
										</button>
									</c:if>


								</div>

							</div>
						</div>
		</c:if>

		<!--Bottom buttons-->

		<c:set var="isRequiredTasksCompleted" value="${true}" />
		<c:forEach var="itemDTO" items="${sessionMap.itemDTOs}">
			<c:if test="${itemDTO.taskListItem.required && not itemDTO.taskListItem.complete}">
				<c:set var="isRequiredTasksCompleted" value="${false}" />
			</c:if>
		</c:forEach>

		<c:if test="${(mode != 'teacher') && isRequiredTasksCompleted}">
			<div class="space-bottom-top align-right">

				<c:choose>
					<c:when test="${taskList.monitorVerificationRequired && !sessionMap.userVerifiedByMonitor && (mode != 'author')}">
						<fmt:message key="label.learning.wait.for.monitor.verification" />
					</c:when>

					<c:when test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<button type="submit" id="finishButton" onclick="return continueReflect()"
							class="btn btn-primary btn-disable-on-submit voffset10 pull-right">
							<fmt:message key="label.continue" />
						</button>
					</c:when>

					<c:otherwise>
						<button type="submit" id="finishButton" onclick="return finishSession()"
							class="btn btn-primary btn-disable-on-submit voffset10 pull-right na">
							<span class="nextActivity"> <c:choose>
									<c:when test="${sessionMap.isLastActivity}">
										<fmt:message key="label.submit" />
									</c:when>
									<c:otherwise>
										<fmt:message key="label.finished" />
									</c:otherwise>
								</c:choose>
							</span>
						</button>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

	</lams:Page>
</body>
</lams:html>
