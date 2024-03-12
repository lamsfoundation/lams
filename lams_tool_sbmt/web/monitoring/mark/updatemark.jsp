<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>
<c:set var="csrfToken"><csrf:token/></c:set>

<c:set var="title"><fmt:message key="label.monitoring.updateMarks.button" /></c:set>
<lams:PageMonitor title="${title}" hideHeader="true">
	<lams:JSImport src="includes/javascript/readmore.min.js" />
	<lams:JSImport src="includes/javascript/upload.js" />
	<script type="text/javascript">
		function removeMarkFile() {
			var answer = confirm("<spring:escapeBody javaScriptEscape='true'><fmt:message key='message.monitor.mark.confirmDeleteFile'/></spring:escapeBody>");
			if (answer) {	
				document.getElementById("updateMarkForm").action = "removeMarkFile.do?${csrfToken}";
				document.getElementById("updateMarkForm").submit();
			}
		}
		
		function validate() {
			var fileSelect = document.getElementById('markFile');
			var files = fileSelect.files;
			if (files.length > 0) {
				var file = files[0];
				if ( ! validateShowErrorNotExecutable(file, '<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.attachment.executable"/></spring:escapeBody>', false, '${EXE_FILE_TYPES}')
						 || ! validateShowErrorFileSize(file, '${UPLOAD_FILE_MAX_SIZE}', '<spring:escapeBody javaScriptEscape="true"><fmt:message key="errors.maxfilesize"/></spring:escapeBody>') ) {
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

	<div class="container-main">
		<h1 class="fs-3 mb-4">
			${title}
		</h1>
		
		<c:forEach var="fileInfo" items="${report}" varStatus="status">			
			<form:form action="updateMark.do?${csrfToken}" method="post" modelAttribute="markForm" id="updateMarkForm" enctype="multipart/form-data" onsubmit="return validate();">
				<form:hidden path="toolSessionID" />
				<form:hidden path="reportID" />
				<form:hidden path="detailID"  />
				<form:hidden path="userID"  />
				<form:hidden path="updateMode" />
				<form:hidden path="markFileUUID" />
				<form:hidden path="markFileVersionID" />
				
				<lams:errors5/>
				
				<div class="div-hover">
					<%@include file="fileinfo.jsp"%>
				
					<div class="row px-2">
						<div class="col-2 fw-bold">
							<fmt:message key="label.learner.marks" />:
						</div>
						<div class="col">
							<input type="text" name="marks" class="form-text" value="${fileInfo.marks}"/>
						</div>
					</div>
						
					<div class="row px-2">
						<div class="col-2 fw-bold">
							<fmt:message key="label.monitor.mark.updoad" />:
						</div>
						
						<div class="col">
							<c:choose>
								<c:when test="${empty fileInfo.markFileUUID}">
									<div class="py-1">
										<lams:FileUpload fileFieldname="markFile" fileInputMessageKey="label.learner.filePath"
												uploadInfoMessageKey="label.learner.uploadMessage" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
									</div>
								</c:when>
								<c:otherwise>
									<c:out value='${fileInfo.markFileName}' escapeXml='true'/>
		
									<div id="actionButtons" class="float-end">						
										<c:set var="viewMarkFileURL">
											<lams:WebAppURL />download/?uuid=${fileInfo.markFileDisplayUuid}&versionID=${fileInfo.markFileVersionID}&preferDownload=false
										</c:set>
										<button type="button" onclick="launchInstructionsPopup('${viewMarkFileURL}')" class="btn btn-sm btn-light">
											<i class="fa fa-eye" title="<fmt:message key="label.view" />"></i>
										</button>
										
										<c:set var="downloadMarkFileURL">
											<lams:WebAppURL />download/?uuid=${fileInfo.markFileDisplayUuid}&versionID=${fileInfo.markFileVersionID}&preferDownload=true
										</c:set>
										<a href="${downloadMarkFileURL}" class="btn btn-sm btn-light ms-2">
											<i class="fa fa-download" title="<fmt:message key="label.download" />"></i>
										</a>
										
										<button type="button" onclick="removeMarkFile()" class="btn btn-sm btn-light ms-2">
											<i class="fa fa-trash" title="<fmt:message key="label.monitoring.file.delete" />"></i>
										</button>
									</div>
										
									<div class="py-1">
										<lams:FileUpload fileFieldname="markFile" fileInputMessageKey="label.monitor.mark.replaceFile"
												uploadInfoMessageKey="label.learner.uploadMessage" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
									</div>	
								</c:otherwise>
							</c:choose>
						</div>
					</div>
						
					<div class="row px-2">
						<div class="col-2 fw-bold">
							<fmt:message key="label.learner.comments" />
						</div>
						<div class="col">
							<lams:CKEditor id="comments" value="${fileInfo.comments}" toolbarSet="DefaultMonitor"></lams:CKEditor>
						</div>
					</div>
	
					<lams:WaitingSpinner id="attachmentArea_Busy"/>
				</div>

				<div class="activity-bottom-buttons">
					<button type="submit" class="btn btn-primary ms-2">
						<i class="fa-regular fa-circle-check me-1"></i>
						<fmt:message key="label.monitoring.saveMarks.button" />
					</button>
					
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
					<a href="${cancelUrl}" class="btn btn-secondary btn-icon-cancel">
						<fmt:message key="label.cancel" />
					</a>
				</div>
			</form:form>
		</c:forEach>
	</div>
</lams:PageMonitor>
