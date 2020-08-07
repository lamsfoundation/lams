<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set>

<c:set var="itemAttachment" value="${topicFormId}" />
<input type="hidden" name="hasAttachment" value="${not empty itemAttachment and itemAttachment.hasAttachment}" />
<c:choose>
	<c:when test="${not empty itemAttachment and itemAttachment.hasAttachment}">
		<ul>
			<li>
				<c:forEach var="file" items="${topic.message.attachments}">
					<c:set var="downloadURL">
						<lams:WebAppURL />download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true
					</c:set>
					<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
						<c:out value="${file.fileName}" /> 
					</a>
				</c:forEach>	
				
				<a href="#" onclick="removeItemAttachment()" class="btn btn-default btn-xs"> 
					<fmt:message key="label.delete" /> 
				</a>
			</li>
		</ul>

	</c:when>
	<c:otherwise>
		<input type="hidden" id="tmpFileUploadId" name="tmpFileUploadId"
			   value="${empty topicFormId.tmpFileUploadId ? tmpFileUploadId : topicFormId.tmpFileUploadId}" />
		<label for="image-upload-area"><fmt:message key="message.label.attachment" /></label>
		<div id="image-upload-area" class="voffset20"></div>
		<script>
			initFileUpload('${{empty topicFormId.tmpFileUploadId ? tmpFileUploadId : topicFormId.tmpFileUploadId}', true, '<lams:user property="localeLanguage"/>');
		</script>
	</c:otherwise>
</c:choose>

<lams:WaitingSpinner id="itemAttachmentArea_Busy"/>
				
