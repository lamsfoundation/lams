<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<lams:css/>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/common.js"></script>
	
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
		<div id="content">
			<h1>
				<fmt:message key="label.monitoring.updateMarks.button"/>
			</h1>
			<c:forEach var="fileInfo"  items="${report}" varStatus="status">	
				<html:form action="/mark.do" method="post" styleId="updateMarkForm" enctype="multipart/form-data">
				
					<html:hidden property="method" value="updateMark" styleId="method" />
					<html:hidden property="toolSessionID" />
					<html:hidden property="reportID" />
					<html:hidden property="detailID"  />
					<html:hidden property="userID"  />
					<html:hidden property="updateMode" />
					<html:hidden property="markFileUUID" />
					<html:hidden property="markFileVersionID" />
					
					<table cellpadding="0">
						<%@include file="fileinfo.jsp"%>
						<tr>
							<td colspan="2">
								<%@include file="/common/messages.jsp"%>
							</td>
						</tr>
					
						<tr>
							<td class="field-name"  style="text-align: left;">
								<fmt:message key="label.learner.marks" />
							</td>
							<td>
								<html:text property="marks" />
							</td>
						</tr>
						<tr>
							<td class="field-name"  style="text-align: left;">
								<fmt:message key="label.monitor.mark.updoad" />
							</td>
							<td>
								<c:if test="${not empty fileInfo.markFileUUID}">
									${fileInfo.markFileName}
									
									
									<c:set var="viewMarkFileURL">
										<html:rewrite page="/download/?uuid=${fileInfo.markFileUUID}&versionID=${fileInfo.markFileVersionID}&preferDownload=false" />
									</c:set>
									<a href="javascript:launchInstructionsPopup('<c:out value='${viewMarkFileURL}' escapeXml='false'/>')" class="button"> <fmt:message key="label.view" /> </a>&nbsp;
									
									<c:set var="downloadMarkFileURL">
										<html:rewrite page="/download/?uuid=${fileInfo.markFileUUID}&versionID=${fileInfo.markFileVersionID}&preferDownload=true" />
									</c:set>
									<a href="<c:out value='${downloadMarkFileURL}' escapeXml='false'/>"  class="button"> <fmt:message key="label.download" /> </a>&nbsp;
									
									<a href="javascript:removeMarkFile()"  class="button"> <fmt:message key="label.monitoring.file.delete" /> </a>
									
									<br />
									<br />
									<fmt:message key="label.monitor.mark.replaceFile" />: 
								</c:if>
								<html:file property="markFile" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<span  class="field-name"><fmt:message key="label.learner.comments" /><BR></span>
								<lams:CKEditor id="comments"
									value="${fileInfo.comments}"
									toolbarSet="DefaultMonitor"></lams:CKEditor>
							</td>
						</tr>
						<tr>
							<td colspan="2">
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
								<a href="${cancelUrl}" class="button">
									<fmt:message key="label.cancel" />
								</a>
								
								&nbsp;&nbsp;
								
								<a href="javascript:document.getElementById('updateMarkForm').submit();" class="button">
									<fmt:message key="label.monitoring.saveMarks.button" />
								</a>
							</td>
						</tr>
					</table>
				</html:form>
			</c:forEach>
		</div>

		<div id="footer"></div>
</body>
</lams:html>
