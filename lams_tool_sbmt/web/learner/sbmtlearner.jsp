<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<html:html>
<head>
	<title><fmt:message key="tool.display.name" /></title>
	<html:base />
	<%@ include file="/common/header.jsp"%>

	<link href="<html:rewrite page='/includes/css/tool_custom.css'/>" rel="stylesheet" type="text/css">	
	
	<script type="text/javascript">
		function submitCount(tUrl){
			var lockOnFinished = <c:out value="${sessionMap.lockOnFinish}"/>;
			var uploadFileNum = <c:choose><c:when test="${empty learner.filesUploaded}">0</c:when><c:otherwise>1</c:otherwise></c:choose>;
			if(lockOnFinished && uploadFileNum==0){
				if(confirm("<fmt:message key='learner.finish.without.upload'/>"))
					location.href= tUrl;
				else
					return false;
			}else
				location.href= tUrl;
		
		}
		function finish(){
			var finishUrl= "<html:rewrite page='/learner.do?method=finish&sessionMapID=${sessionMapID}'/>";
			return submitCount(finishUrl);
		}
		function notebook(){
			var continueUrl= "<html:rewrite page='/learning/newReflection.do?sessionMapID=${sessionMapID}'/>";
			return submitCount(continueUrl);
		}
	</script>

</head>

<body class="stripes">

		<div id="content">
		<h1>
			<c:out value="${sessionMap.title}" escapeXml="false" />
		</h1>

			<table cellpadding="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="2">
						<h2>
							<c:out value="${sessionMap.instruction}" escapeXml="false" />
						</h2>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<%@include file="/common/messages.jsp"%>
						<c:if test="${sessionMap.limitUpload}">
							<fmt:message key="message.left.upload.limit">
								 <fmt:param value="${learner.limitUploadLeft}"/>
							</fmt:message>
						</c:if>
					</td>
				</tr>

				<!--Checks if the filesUploaded property of the SbmtLearnerForm is set -->
				<c:choose>

					<c:when test="${empty learner.filesUploaded}">
						<tr>
							<td>
							<fmt:message key="label.learner.noUpload" />
								<%-- 
								<p>
									<fmt:message key="label.learner.uploadMessage" />
								</p>
								--%>
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
									<c:out value="${file.filePath}" /> &nbsp;&nbsp;
									<c:if test="${file.currentLearner}">
										<c:set var="downloadURL">
											<c:url value="/download?uuid=${file.uuID}&versionID=${file.versionID}&preferDownload=true"/>
										</c:set>
										<a href="${downloadURL}"><fmt:message key="label.download"/> </a>
									</c:if>
								</td>
							</tr>
							<tr>
								<!--Second Row displaying the description of the File -->
								<td class="field-name">
									<fmt:message key="label.learner.fileDescription" />
								</td>
								<td>
									<lams:out value="${file.fileDescription}"/>
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
										<c:when test="${empty file.comments}">
											<fmt:message key="label.learner.notAvailable" />
										</c:when>
										<c:otherwise>
											<c:out value="${file.marks}" escapeXml="false" />
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<hr size="1" style="width:500px;"/>
								</td>
							</tr>
						</c:forEach>

					</c:otherwise>
				</c:choose>

			</table>

			<c:if test="${sessionMap.mode != 'teacher'}">
				<html:form action="/learner?method=uploadFile" method="post" enctype="multipart/form-data">
					<html:hidden property="sessionMapID"/>
					<table>
						<!-- Hidden fields -->
						<html:hidden property="toolSessionID" value="${sessionMap.toolSessionID}" />

						<!--File path row -->
						<tr>
							<td class="field-name">
								<fmt:message key="label.learner.filePath" />
							</td>
							<td>
								<html:file property="file" disabled="${sessionMap.finishLock || sessionMap.arriveLimit}" size="40" tabindex="1" />
							</td>
						</tr>
						<!--File Description row -->
						<tr>
							<td class="field-name">
								<fmt:message key="label.learner.fileDescription" />
							</td>
							<td>
								<html:textarea rows="5" cols="40" tabindex="2" property="description" disabled="${sessionMap.finishLock || sessionMap.arriveLimit}" />
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<html:submit disabled="${sessionMap.finishLock || sessionMap.arriveLimit}" styleClass="button">
									<fmt:message key="label.learner.upload" />
								</html:submit>
								<c:choose>
									<c:when test="${sessionMap.reflectOn}">
										<html:button property="notebookButton" onclick="javascript:notebook();" disabled="${sessionMap.finishLock}"  styleClass="button">
											<fmt:message key="label.continue" />
										</html:button>
									</c:when>
									<c:otherwise>
										<html:button property="finishButton" onclick="javascript:finish();" disabled="${sessionMap.finishLock}" styleClass="button">
											<fmt:message key="button.finish" />
										</html:button>
									</c:otherwise>
								</c:choose>								
							</td>
						</tr>
					</table>
				</html:form>
			</c:if>
		</div>
		<div id="footer"></div>
</body>
</html:html>
