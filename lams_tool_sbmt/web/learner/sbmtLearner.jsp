<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>

<html:html>
<head>
	<title><fmt:message key="tool.display.name" /></title>
	<html:base />
	<lams:headItems />

	<script type="text/javascript">
		var locked =  <c:out value="${learner.locked}"/>;
		function finish(){
			var lockOnFinished = <c:out value="${learner.contentLockOnFinished}"/>;
			var uploadFileNum = <c:choose><c:when test="${empty learner.filesUploaded}">0</c:when><c:otherwise>1</c:otherwise></c:choose>;
			var finishUrl= "<html:rewrite page='/learner.do?method=finish&toolSessionID=${learner.toolSessionID}'/>";
			if(lockOnFinished && uploadFileNum==0){
				if(confirm("<fmt:message key='learner.finish.without.upload'/>"))
					location.href= finishUrl;
				else
					return false;
			}else
				location.href= finishUrl;
		}
	</script>
	<html:javascript formName="SbmtLearnerForm" method="validateForm" />

</head>

<body>
	<div id="page-learner">
		<h1 class="no-tabs-below">
			<c:out value="${learner.contentTitle}" escapeXml="false" />
		</h1>

		<div id="header-no-tabs-learner">

		</div>

		<div id="content-learner">
			<table cellpadding="0">
				<tr>
					<td colspan="2">
						<p>
							<c:out value="${learner.contentInstruction}" escapeXml="false" />
						</p>
					</td>
				</tr>

				<!--Checks if the filesUploaded property of the SbmtLearnerForm is set -->
				<c:choose>

					<c:when test="${empty learner.filesUploaded}">
						<tr>
							<td>
							<fmt:message key="label.learner.noUpload" />
							<p>
								<fmt:message key="label.learner.uploadMessage" />
							</p>
							</center>
							</td>
						</tr>
					</c:when>

					<c:otherwise>

						<c:forEach var="file" items="${learner.filesUploaded}">
							<tr>
								<!--First Row displaying the name of the File -->
								<td class="field-name">
									<fmt:message key="label.learner.fileName" />
								</td>
								<td>
									<c:out value="${file.filePath}" />
								</td>
							</tr>
							<tr>
								<!--Second Row displaying the description of the File -->
								<td class="field-name">
									<fmt:message key="label.learner.fileDescription" />
								</td>
								<td>
									<c:out value="${file.fileDescription}" escapeXml="false" />
								</td>
							</tr>
							<tr>
								<!--Third row displaying the date of submission of the File -->
								<td class="field-name">
									<fmt:message key="label.learner.time" />
								</td>
								<td>
									<c:out value="${file.dateOfSubmission}" />
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
										<c:when test="${empty file.comments}">
											<fmt:message key="label.learner.notAvailable" />
										</c:when>
										<c:otherwise>
											<c:out value="${file.marks}" escapeXml="false" />
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:forEach>

					</c:otherwise>
				</c:choose>

			</table>

			<c:if test="${mode != 'teacher'}">
				<html:form action="/learner?method=uploadFile" method="post" enctype="multipart/form-data" focus="filePath" onsubmit="return validateForm(this);">
					<p>
						<html:errors />
					</p>
					<table>
						<!-- Hidden fields -->
						<html:hidden property="toolSessionID" value="${learner.toolSessionID}" />

						<!--File path row -->
						<tr>
							<td class="field-name">
								<fmt:message key="label.learner.filePath" />
							</td>
							<td>
								<html:file property="filePath" disabled="${learner.locked}" size="40" tabindex="1" />
							</td>
						</tr>
						<!--File Description row -->
						<tr>
							<td class="field-name">
								<fmt:message key="label.learner.fileDescription" />
							</td>
							<td>
								<lams:STRUTS-textarea rows="5" cols="40" tabindex="2" property="fileDescription" disabled="${learner.locked}" />
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<html:submit disabled="${learner.locked}" styleClass="button">
									<fmt:message key="label.learner.upload" />
								</html:submit>
								<html:button property="finished" onclick="finish()" disabled="${learner.locked}" styleClass="button">
									<fmt:message key="label.learner.finished" />
								</html:button>
							</td>
						</tr>
					</table>
				</html:form>
			</c:if>
		</div>
		<div id="footer-learner"></div>
	</div>
</body>
</html:html>
