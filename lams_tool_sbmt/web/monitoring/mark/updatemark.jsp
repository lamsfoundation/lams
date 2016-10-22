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
							<%@include file="/common/messages.jsp"%>
				</logic:messagesPresent>
			
			<dl class="dl-horizontal">		
				<dt>
						<fmt:message key="label.learner.marks" />:
				</dt>
				<dd>
						<html:text property="marks" />
				</dd>
				
				</dl>
				<br/>
				<dl class="dl-horizontal">
				<dt>
						<fmt:message key="label.monitor.mark.updoad" />:
				</dt>
				<c:choose>
					<c:when test="${empty fileInfo.markFileUUID}">
						<dd>
								<html:file property="markFile" />
						</dd>
					</c:when>
					<c:otherwise>
						<dd>
								<c:out value='${fileInfo.markFileName}' escapeXml='true'/>

								<div id="actionButtons" class="pull-right">						
								<c:set var="viewMarkFileURL">
									<html:rewrite page="/download/?uuid=${fileInfo.markFileUUID}&versionID=${fileInfo.markFileVersionID}&preferDownload=false" />
								</c:set>
								<html:link href="javascript:launchInstructionsPopup('${viewMarkFileURL}')" styleClass="btn btn-xs btn-default">
									<i class="fa fa-eye" title="<fmt:message key="label.view" />"></i>
								</html:link>
								
								<c:set var="downloadMarkFileURL">
									<html:rewrite page="/download/?uuid=${fileInfo.markFileUUID}&versionID=${fileInfo.markFileVersionID}&preferDownload=true" />
								</c:set>
								<html:link href="${downloadMarkFileURL}" styleClass="btn btn-xs btn-default loffset10">
									<i class="fa fa-download" title="<fmt:message key="label.download" />"></i>
								</html:link>
								
								<html:link href="javascript:removeMarkFile()" styleClass="btn btn-xs btn-danger loffset10">
									<i class="fa fa-trash" title="<fmt:message key="label.monitoring.file.delete" />"></i>
								</html:link>
								</div>
								
								<div class="offset2">
									<small>
									<fmt:message key="label.monitor.mark.replaceFile" />:
									<html:file property="markFile" />
									</small>
								</div>
						</dd>		
					</c:otherwise>
				</c:choose>
				
				<div id="commentsWrapper" class="voffset10">		
					<fmt:message key="label.learner.comments" />
					<lams:CKEditor id="comments"
						value="${fileInfo.comments}"
						toolbarSet="DefaultMonitor"></lams:CKEditor>
				</div>

				<hr width="100%" />
				<div id="buttons" class="pull-right">	
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
					<html:link href="javascript:document.getElementById('updateMarkForm').submit()" styleClass="btn  btn-primary loffset10">
						<fmt:message key="label.monitoring.saveMarks.button" />
					</html:link>
				</div>
			</html:form>
		</c:forEach>


<div id="footer"></div>
</lams:Page>
</body>
</lams:html>