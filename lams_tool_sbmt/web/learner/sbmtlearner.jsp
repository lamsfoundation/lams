<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
<lams:head>
	<title><fmt:message key="tool.display.name" /></title>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
		$(document).ready(function() {
			$("time.timeago").timeago();
		});

		function submitCount(tUrl) {
			var lockOnFinished = <c:out value="${sessionMap.lockOnFinish}"/>;
			var uploadFileNum = <c:choose><c:when test="${empty learner.filesUploaded}">0</c:when><c:otherwise>1</c:otherwise></c:choose>;
			if (lockOnFinished && uploadFileNum == 0) {
				if (confirm("<fmt:message key='learner.finish.without.upload'/>"))
					location.href = tUrl;
				else
					return false;
				//			}else{
				//				if(confirm("<fmt:message key='messsage.learner.finish.confirm'/>"))
				//					location.href= tUrl;
				//				else
				//					return false;
				//			}
			} else if (uploadFileNum == 0) {
				if (confirm("<fmt:message key='messsage.learner.finish.confirm'/>")) {
					disableButtons();
					location.href = tUrl;
				} else {
					return false;
				}
			} else {
				disableButtons();
				location.href = tUrl;
			}
		}
		function finish() {
			var finishUrl = "<html:rewrite page='/learner.do?method=finish&sessionMapID=${sessionMapID}'/>";
			return submitCount(finishUrl);
		}
		function notebook() {
			var continueUrl = "<html:rewrite page='/learning/newReflection.do?sessionMapID=${sessionMapID}'/>";
			return submitCount(continueUrl);
		}
		function disableButtons() {
			// do not disable the file button or the file will be missing on the upload.
			$('.btn-disable-on-submit').prop('disabled', true);
			$('a.btn-disable-on-submit').hide(); // links must be hidden, cannot be disabled
		}
	</script>

</lams:head>

<body class="stripes">

	<lams:Page type="learner" title="${sessionMap.title}">
		<div class="panel">
			<c:out value="${sessionMap.instruction}" escapeXml="false" />
		</div>

		<!-- notices and announcements -->
		<c:if test="${sessionMap.mode == 'author' || sessionMap.mode == 'learner'}">
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

			<c:if test="${sessionMap.limitUpload}">
				<c:choose>
					<c:when test="${not sessionMap.userFinished || not sessionMap.lockOnFinish}">
						<lams:Alert id="limitUploads" close="true" type="info">
							<fmt:message key="message.left.upload.limit">
								<fmt:param value="${learner.limitUploadLeft}" />
							</fmt:message>
						</lams:Alert>
					</c:when>
				</c:choose>
			</c:if>

		</c:if>
		<!-- End notices and announcements -->

		<%@include file="/common/messages.jsp"%>


		<!--Checks if the filesUploaded property of the SbmtLearnerForm is set -->
		<c:choose>

			<c:when test="${empty learner.filesUploaded}">
				<div class="alert">
					<fmt:message key="label.learner.noUpload" />
				</div>
			</c:when>

			<c:otherwise>
			
			<table class="table table-condensed">
				<tr>
					<th class="active">
						<fmt:message key="monitoring.user.submittedFiles" />
					</th>
					<th colspan="2" class="active">
						<c:out value="${fn:length(learner.filesUploaded)}" />
					</th>
				<tr>
				<c:forEach var="file" items="${learner.filesUploaded}" varStatus="status">
					<tr class="active">
						<!--First Row displaying the name of the File -->
						<td colspan="${file.currentLearner ? 2 : 3}">
							<c:out value="${status.count}" />) <c:out value="${file.filePath}" />
							<c:if test="${file.currentLearner}">
								<c:set var="downloadURL">
									<c:url value="/download?uuid=${file.uuID}&versionID=${file.versionID}&preferDownload=true" />
								</c:set>
								</td>
								<td>
									<a href="${downloadURL}" title="<fmt:message key="label.download" />" class="btn btn-default btn-disable-on-submit pull-right">
										<i class="fa fa-download" ></i>
									</a>
								</td>
							</c:if>
						</td>
					</tr>
					<tr>
						<!--Second Row displaying the description of the File -->
						<td style="vertical-align:top"><fmt:message key="label.learner.fileDescription" /></td>
						<td colspan="2"><lams:out value="${file.fileDescription}" escapeHtml="true" /></td>
					</tr>

					<tr>
						<!--Third row displaying the date of submission of the File -->
						<td><fmt:message key="label.learner.time" /></td>
						<td colspan="2">
							<lams:Date value="${file.dateOfSubmission}" timeago="true"/>
						</td>
					</tr>

				<!--Fourth row displaying the comments -->
				<c:if test="${not empty file.comments}">
					<tr>
						<td style="vertical-align:top"><fmt:message key="label.learner.comments" /></td>
						<td colspan="2">
							<c:out value="${file.comments}" escapeXml="false" />
						</td>
					</tr>
				</c:if>

				<!--Fifth row displaying the marks-->
				<c:if test="${not empty file.marks}">
					<tr>
						<td><fmt:message key="label.learner.marks" /></td>
						<td>
							<c:out value="${file.marks}" escapeXml="true" />
						</td>
					</tr>
				</c:if>
				<!--Sixth row displaying the marked file-->
				<c:if  test="${not empty file.markFileUUID}">	
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
					<tr style="margin-bottom: 5px; border-bottom: 5px solid #ddd">
						<td></td>
						<td></td>
						<td></td>
					</tr>
				</c:forEach>
			</table>

			</c:otherwise>
		</c:choose>

		<hr width="100%"/>
		<!-- Form -->

		<c:if test="${sessionMap.mode != 'teacher'}">
			<!-- dont display form if teacher -->

			<!-- now check if the user is (finished + lockedWhenFinished) or no more files allowed -->
			<c:set var="displayForm" value="${sessionMap.finishLock || sessionMap.arriveLimit}" />

			<c:if test="${!displayForm}">

				<html:form action="/learner?method=uploadFile" method="post" enctype="multipart/form-data" onsubmit="disableButtons();" >
					<html:hidden property="sessionMapID" />

					<!-- Hidden fields -->
					<html:hidden property="toolSessionID" value="${sessionMap.toolSessionID}" />

					<!--File path row -->
					<div class="panel panel-default">
						<div class="panel-heading panel-title">
							<fmt:message key="label.learner.upload" />
						</div>
						<div class="panel-body">

							<div class="form-group">
								<label for="file"><fmt:message key="label.learner.filePath" />&nbsp;<span style="color: red">*</span></label>
								<html:file property="file" styleClass="btn btn-sm btn-file"
									disabled="${sessionMap.finishLock || sessionMap.arriveLimit}" />
							</div>
							<div class="form-group">
								<!--File Description row -->
								<label for="description"><fmt:message key="label.learner.fileDescription" />&nbsp;<span
									style="color: red">*</span></label>
								<html:textarea styleClass="form-control" property="description"
									disabled="${sessionMap.finishLock || sessionMap.arriveLimit}" />
									<p class="help-block"><small><fmt:message key="errors.required"><fmt:param>*</fmt:param></fmt:message></small></p>
							</div>
							<div class="form-group">
								<button id="uploadButton" type="submit" <c:if test="${sessionMap.finishLock || sessionMap.arriveLimit}">disabled="disabled"</c:if>
									class="btn btn-sm btn-default btn-primary btn-disable-on-submit">
									<i class="fa fa-xs fa-upload"></i> <fmt:message key="label.learner.upload" />
								</button>
							</div>
						</div>
					</div>
				</html:form>
			</c:if>
		</c:if>
		<!-- end form -->



		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<!-- reflection -->
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

					<html:button property="notebookButton" style="margin-top: 10px" onclick="javascript:notebook();"
						styleClass="btn btn-sm btn-primary btn-disable-on-submit pull-left" >
						<fmt:message key="label.edit" />
					</html:button>
				</div>
			</div>
			<!-- end reflection -->
		</c:if>


		<c:choose>
			<c:when test="${sessionMap.reflectOn and (not sessionMap.userFinished)}">
				<html:button property="notebookButton" onclick="javascript:notebook();" styleClass="btn btn-primary btn-disable-on-submit pull-right">
					<fmt:message key="label.continue" />
				</html:button>
			</c:when>
			<c:otherwise>
				<html:button property="finishButton" onclick="javascript:finish();" styleClass="btn btn-primary btn-disable-on-submit pull-right na" styleId="finishButton">
					<c:choose>
						<c:when test="${activityPosition.last}">
							<fmt:message key="button.submit" />
						</c:when>
						<c:otherwise>
							<fmt:message key="button.finish" />
						</c:otherwise>
					</c:choose>
				</html:button>
			</c:otherwise>
		</c:choose>



		<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>
