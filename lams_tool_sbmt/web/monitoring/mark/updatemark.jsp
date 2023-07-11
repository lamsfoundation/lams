<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>

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
	
	<lams:JSImport src="includes/javascript/common.js" />
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<lams:JSImport src="includes/javascript/upload.js" />
	<script type="text/javascript">
		function removeMarkFile() {
			var answer = confirm("<fmt:message key="message.monitor.mark.confirmDeleteFile"/>");
			if (answer) {	
				document.getElementById("method").value = "removeMarkFile";
				document.getElementById("updateMarkForm").submit();
			}
		}
		
		function validate() {
			var fileSelect = document.getElementById('markFile');
			var files = fileSelect.files;
			if (files.length > 0) {
				var file = files[0];
				if ( ! validateShowErrorNotExecutable(file, '<fmt:message key="error.attachment.executable"/>', false, '${EXE_FILE_TYPES}')
						 || ! validateShowErrorFileSize(file, '${UPLOAD_FILE_MAX_SIZE}', '<fmt:message key="errors.maxfilesize"/>') ) {
					return false;
				}
			}
			var div = document.getElementById("attachmentArea_Busy");
			if(div != null){
				div.style.display = '';
			}
			return true;
		}
	</script>
	
</lams:head>

<body class="stripes">
<c:set var="title"><fmt:message key="label.monitoring.updateMarks.button" /></c:set>
<lams:Page title="${title}" type="monitor">
		<c:set var="csrfToken"><csrf:token/></c:set>
		<c:forEach var="fileInfo" items="${report}" varStatus="status">
			
			<form:form action="updateMark.do?${csrfToken}" method="post" modelAttribute="markForm" id="updateMarkForm" enctype="multipart/form-data" onsubmit="return validate();">
			
				<form:hidden path="toolSessionID" />
				<form:hidden path="reportID" />
				<form:hidden path="detailID"  />
				<form:hidden path="userID"  />
				<form:hidden path="updateMode" />
				<form:hidden path="markFileUUID" />
				<form:hidden path="markFileVersionID" />
				
				<%@include file="fileinfo.jsp"%>
				
				<lams:errors/>
			
			<dl class="dl-horizontal">		
				<dt>
						<fmt:message key="label.learner.marks" />:
				</dt>
				<dd>
						<input type="text" name="marks" />
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
							<lams:FileUpload fileFieldname="markFile" fileInputMessageKey="label.learner.filePath"
									uploadInfoMessageKey="label.learner.uploadMessage" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
						</dd>
					</c:when>
					<c:otherwise>
						<dd>
								<c:out value='${fileInfo.markFileName}' escapeXml='true'/>

								<div id="actionButtons" class="pull-right">						
								<c:set var="viewMarkFileURL">
									<lams:WebAppURL />download/?uuid=${fileInfo.markFileDisplayUuid}&versionID=${fileInfo.markFileVersionID}&preferDownload=false
								</c:set>
								<a href="javascript:launchInstructionsPopup('${viewMarkFileURL}')" class="btn btn-xs btn-default">
									<i class="fa fa-eye" title="<fmt:message key="label.view" />"></i>
								</a>
								
								<c:set var="downloadMarkFileURL">
									<lams:WebAppURL />download/?uuid=${fileInfo.markFileDisplayUuid}&versionID=${fileInfo.markFileVersionID}&preferDownload=true
								</c:set>
								<a href="${downloadMarkFileURL}" class="btn btn-xs btn-default loffset10">
									<i class="fa fa-download" title="<fmt:message key="label.download" />"></i>
								</a>
								
								<a href="javascript:removeMarkFile()" class="btn btn-xs btn-danger loffset10">
									<i class="fa fa-trash" title="<fmt:message key="label.monitoring.file.delete" />"></i>
								</a>
								</div>
								
								<div class="offset5">
									<lams:FileUpload fileFieldname="markFile" fileInputMessageKey="label.monitor.mark.replaceFile"
									uploadInfoMessageKey="label.learner.uploadMessage" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
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

				<lams:WaitingSpinner id="attachmentArea_Busy"/>

				<hr width="100%" />
				<div id="buttons" class="pull-right">	
					<c:if test="${updateMode == 'listMark'}">
						<c:set var="cancelUrl">
							<c:url value="/monitoring/listMark.do"/>?userID=${fileInfo.owner.userID}&toolSessionID=${toolSessionID}
						</c:set>
					</c:if>
					<c:if test="${updateMode == 'listAllMarks'}">
						<c:set var="cancelUrl">
							<c:url value="/monitoring/listAllMarks.do"/>?&toolSessionID=${toolSessionID}
						</c:set>
					</c:if>
					<a href="${cancelUrl}" class="btn btn-default">
						<fmt:message key="label.cancel" />
					</a>
					<button type="submit" class="btn  btn-primary loffset10">
						<fmt:message key="label.monitoring.saveMarks.button" />
					</button>
				</div>
			</form:form>
		</c:forEach>

<div id="footer"></div>
</lams:Page>
</body>
</lams:html>