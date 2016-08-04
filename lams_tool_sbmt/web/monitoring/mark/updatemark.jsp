<!DOCTYPE html>

<%@include file="/common/taglibs.jsp"%>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<lams:css/>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/common.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	
	<script type="text/javascript">
		function removeMarkFile() {
			var answer = confirm("<fmt:message key="message.monitor.mark.confirmDeleteFile"/>");
			if (answer) {	
				document.getElementById("method").value = "removeMarkFile";
				document.getElementById("updateMarkForm").submit();
			}
		}
	</script>
	
</lams:head>

<body class="stripes">
<c:set var="title"><fmt:message key="label.monitoring.updateMarks.button" /></c:set>
<lams:Page title="${title}" type="monitor">

	<table class="table table-condensed">
		<c:forEach var="fileInfo" items="${report}" varStatus="status">
			<html:form action="/mark.do" method="post" styleId="updateMarkForm" enctype="multipart/form-data">
			
				<html:hidden property="method" value="updateMark" styleId="method" />
				<html:hidden property="toolSessionID" />
				<html:hidden property="reportID" />
				<html:hidden property="detailID"  />
				<html:hidden property="userID"  />
				<html:hidden property="updateMode" />
				<html:hidden property="markFileUUID" />
				<html:hidden property="markFileVersionID" />
				
				<%@include file="fileinfo.jsp"%>
				
				<logic:messagesPresent>
					<tr>
						<td colspan="3">
							<%@include file="/common/messages.jsp"%>
						</td>
					</tr>
				</logic:messagesPresent>
				
				<tr>
					<td>
						<fmt:message key="label.learner.marks" />
					</td>
					<td colspan="2">
						<html:text property="marks" />
					</td>
				</tr>
				
				<c:choose>
					<c:when test="${empty fileInfo.markFileUUID}">
						<tr>
							<td>
								<fmt:message key="label.monitor.mark.updoad" />
							</td>
							<td colspan="2">
								<html:file property="markFile" />
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr>
							<td rowspan="2">
								<fmt:message key="label.monitor.mark.updoad" />
							</td>
							<td>
								<c:out value='${fileInfo.markFileName}' escapeXml='true'/>
							</td>
							<td>
								<c:set var="viewMarkFileURL">
									<html:rewrite page="/download/?uuid=${fileInfo.markFileUUID}&versionID=${fileInfo.markFileVersionID}&preferDownload=false" />
								</c:set>
								<html:link href="javascript:launchInstructionsPopup('${viewMarkFileURL}')" styleClass="btn btn-default">
									<fmt:message key="label.view" />
								</html:link>
								
								<c:set var="downloadMarkFileURL">
									<html:rewrite page="/download/?uuid=${fileInfo.markFileUUID}&versionID=${fileInfo.markFileVersionID}&preferDownload=true" />
								</c:set>
								<html:link href="${downloadMarkFileURL}" styleClass="btn btn-default loffset10">
									<fmt:message key="label.download" />
								</html:link>
								
								<html:link href="javascript:removeMarkFile()" styleClass="btn btn-default loffset10">
									<fmt:message key="label.monitoring.file.delete" />
								</html:link>
							</td>
						</tr>
						<tr>
							<td>
								<fmt:message key="label.monitor.mark.replaceFile" />:
							</td>
							<td>
								<html:file property="markFile" />
							</td>
						</tr>
					</c:otherwise>
				</c:choose>
				
				<tr>
					<td colspan="3">
						<fmt:message key="label.learner.comments" />
						<lams:CKEditor id="comments"
							value="${fileInfo.comments}"
							toolbarSet="DefaultMonitor"></lams:CKEditor>
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<br />
						<c:if test="${updateMode == 'listMark'}">
							<c:set var="cancelUrl">
								<c:url value="/monitoring.do"/>?method=listMark&userID=${fileInfo.owner.userID}&toolSessionID=${toolSessionID}
							</c:set>
						</c:if>
						<c:if test="${updateMode == 'listAllMarks'}">
							<c:set var="cancelUrl">
								<c:url value="/monitoring.do"/>?method=listAllMarks&toolSessionID=${toolSessionID}
							</c:set>
						</c:if>
						<html:link href="${cancelUrl}" styleClass="btn btn-default">
							<fmt:message key="label.cancel" />
						</html:link>
						<html:link href="javascript:document.getElementById('updateMarkForm').submit()" styleClass="btn btn-default loffset10">
							<fmt:message key="label.monitoring.saveMarks.button" />
						</html:link>
					</td>
				</tr>
			</html:form>
		</c:forEach>
	</table>

<div id="footer"></div>
</lams:Page>
</body>
</lams:html>