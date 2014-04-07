<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>

<%-- If you change this file, remember to update the copy made for CNG-12 --%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
<lams:head>
	<title><fmt:message key="tool.display.name" /></title>
	<html:base />
	<%@ include file="/common/header.jsp"%>

	<link href="<html:rewrite page='/includes/css/tool_custom.css'/>"
		rel="stylesheet" type="text/css">

	<script type="text/javascript">
		function submitCount(tUrl){
			var lockOnFinished = <c:out value="${sessionMap.lockOnFinish}"/>;
			var uploadFileNum = <c:choose><c:when test="${empty learner.filesUploaded}">0</c:when><c:otherwise>1</c:otherwise></c:choose>;
			if(lockOnFinished && uploadFileNum==0){
				if(confirm("<fmt:message key='learner.finish.without.upload'/>"))
					location.href= tUrl;
				else
					return false;
//			}else{
//				if(confirm("<fmt:message key='messsage.learner.finish.confirm'/>"))
//					location.href= tUrl;
//				else
//					return false;
//			}
			}else if(uploadFileNum==0){
				if(confirm("<fmt:message key='messsage.learner.finish.confirm'/>"))
					location.href= tUrl;
				else
					return false;
			}else
				location.href= tUrl;
		}
		function finish(){
			document.getElementById("finishButton").disabled = true;
			var finishUrl= "<html:rewrite page='/learner.do?method=finish&sessionMapID=${sessionMapID}'/>";
			return submitCount(finishUrl);
		}
		function notebook(){
			var continueUrl= "<html:rewrite page='/learning/newReflection.do?sessionMapID=${sessionMapID}'/>";
			return submitCount(continueUrl);
		}
	</script>

</lams:head>

<body class="stripes">

	<div id="content">
		<h1>
			<c:out value="${sessionMap.title}" escapeXml="true" />
		</h1>

		<p>
			<c:out value="${sessionMap.instruction}" escapeXml="false" />
		</p>
	    <c:if test="${sessionMap.mode == 'author' || sessionMap.mode == 'learner'}">
			<c:if test="${sessionMap.lockOnFinish}">
				<div class="info">
					<c:choose>
						<c:when test="${sessionMap.userFinished}">
							<fmt:message key="message.activityLocked" />
						</c:when>
						<c:otherwise>
							<fmt:message key="message.warnLockOnFinish" />
						</c:otherwise>
					</c:choose>
				</div>
			</c:if>
			
			<c:if test="${not empty sessionMap.submissionDeadline}">
				<div class="info">
					<fmt:message key="authoring.info.teacher.set.restriction" >
						<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
					</fmt:message>
				</div>
			</c:if>			
		</c:if>

		<%@include file="/common/messages.jsp"%>
		<c:if test="${sessionMap.limitUpload}">
			<c:choose>
			 <c:when test="${not sessionMap.userFinished || not sessionMap.lockOnFinish}">
				<p>		
				<fmt:message key="message.left.upload.limit">
					<fmt:param value="${learner.limitUploadLeft}" />
				</fmt:message>
				</p>
			</c:when>
			</c:choose>
		</c:if>
		<!--Checks if the filesUploaded property of the SbmtLearnerForm is set -->
		<c:choose>

			<c:when test="${empty learner.filesUploaded}">
				<p>
					<fmt:message key="label.learner.noUpload" />
				</p>
			</c:when>

			<c:otherwise>
				<c:forEach var="file" items="${learner.filesUploaded}">

					<div class="shading-bg">
						<table>
							<tr>
								<!--First Row displaying the name of the File -->
								<td class="field-name">
									<fmt:message key="label.learner.fileName" />
								</td>
								<td>
									<c:out value="${file.filePath}" />
									<c:if test="${file.currentLearner}">
										<c:set var="downloadURL">
											<c:url
												value="/download?uuid=${file.uuID}&versionID=${file.versionID}&preferDownload=true" />
										</c:set>
										<a href="${downloadURL}"><fmt:message key="label.download" />
										</a>
									</c:if>
								</td>
							</tr>

							<tr>
								<!--Second Row displaying the description of the File -->
								<td class="field-name">
									<fmt:message key="label.learner.fileDescription" />
								</td>
								<td>
									<lams:out value="${file.fileDescription}" escapeHtml="true"/>
								</td>
							</tr>

							<tr>
								<!--Third row displaying the date of submission of the File -->
								<td class="field-name">
									<fmt:message key="label.learner.time" />
								</td>
								<td>
									<lams:Date value="${file.dateOfSubmission}" />
								</td>
							</tr>

							<tr>
								<!--Fourth row displaying the comments -->
								<td class="field-name">
									<fmt:message key="label.learner.comments" />
								</td>
								<td>
									<c:choose>
										<c:when test="${empty file.comments}">
											<fmt:message key="label.learner.notAvailable" />
										</c:when>
										<c:otherwise>
											<c:out value="${file.comments}" escapeXml="false" />
										</c:otherwise>
									</c:choose>
								</td>
							</tr>

							<tr>
								<!--Fifth row displaying the marks-->
								<td class="field-name">
									<fmt:message key="label.learner.marks" />
								</td>
								<td>
									<c:choose>
										<c:when test="${empty file.marks}">
											<fmt:message key="label.learner.notAvailable" />
										</c:when>
										<c:otherwise>
											<c:out value="${file.marks}" escapeXml="true" />
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<!--Sixth row displaying the marked file-->
								<td class="field-name">
									<fmt:message key="label.monitor.mark.markedFile" />
								</td>
								<td>
									<c:choose>
										<c:when test="${empty file.markFileUUID}">
											<fmt:message key="label.learner.notAvailable" />
										</c:when>
										<c:otherwise>
											<c:out value="${file.markFileName}" />
											<c:set var="markFileDownloadURL">
											<c:url
												value="/download?uuid=${file.markFileUUID}&versionID=${file.markFileVersionID}&preferDownload=true" />
											</c:set>
											<a href="${markFileDownloadURL}"><fmt:message key="label.download" />
											</a>

										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</table>
					</div>
				</c:forEach>

			</c:otherwise>
		</c:choose>



		<div class="last-item"></div>

		<c:if test="${sessionMap.mode != 'teacher'}">
			<html:form action="/learner?method=uploadFile" method="post"
				enctype="multipart/form-data">
				<html:hidden property="sessionMapID" />

				<!-- Hidden fields -->
				<html:hidden property="toolSessionID"
					value="${sessionMap.toolSessionID}" />

				<!--File path row -->
				<div class="field-name">
					<fmt:message key="label.learner.filePath" />
				</div>

				<html:file property="file"
					disabled="${sessionMap.finishLock || sessionMap.arriveLimit}"
					size="40" tabindex="1" />

				<!--File Description row -->
				<div class="field-name space-top">
					<fmt:message key="label.learner.fileDescription" />
				</div>
				<html:textarea rows="5" cols="40" tabindex="2"
					styleClass="text-area" property="description"
					disabled="${sessionMap.finishLock || sessionMap.arriveLimit}" />

				<div class="small-space-top">
					<html:submit
						disabled="${sessionMap.finishLock || sessionMap.arriveLimit}"
						styleClass="button">
						<fmt:message key="label.learner.upload" />
					</html:submit>
				</div>

				<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
					<div class="small-space-top">
						<h2>
							${sessionMap.reflectInstructions}
						</h2>

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

						<html:button property="notebookButton"
							onclick="javascript:notebook();" styleClass="button">
							<fmt:message key="label.edit" />
						</html:button>

					</div>
				</c:if>

				<div class="space-bottom-top" align="right">
					<c:choose>
						<c:when
							test="${sessionMap.reflectOn and (not sessionMap.userFinished)}">
							<html:button property="notebookButton"
								onclick="javascript:notebook();" styleClass="button">
								<fmt:message key="label.continue" />
							</html:button>
						</c:when>
						<c:otherwise>
							<html:link href="#nogo" property="finishButton"
								onclick="finish();return false" styleClass="button"
								styleId="finishButton">
								<span class="nextActivity">
									<c:choose>
										<c:when test="${activityPosition.last}">
											<fmt:message key="button.submit" />
										</c:when>
										<c:otherwise>
											<fmt:message key="button.finish" />
										</c:otherwise>
									</c:choose>
								</span>
							</html:link>
						</c:otherwise>
					</c:choose>
				</div>

			</html:form>
		</c:if>
	</div>
	<div id="footer"></div>
</body>
</lams:html>
