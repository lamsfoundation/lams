<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorSpringUtil" %>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorSpringUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set>

<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
<input type="hidden" name="hasAttachment" value="${itemAttachment.hasAttachment}" />
<c:choose>
	<c:when test="${itemAttachment.hasAttachment}">
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
		<lams:FileUpload fileFieldname="attachmentFile" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
	</c:otherwise>
</c:choose>

<lams:WaitingSpinner id="itemAttachmentArea_Busy"/>
				
