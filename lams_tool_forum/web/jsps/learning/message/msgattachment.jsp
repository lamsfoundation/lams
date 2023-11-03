<%@ include file="/common/taglibs.jsp" %>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

<input type="hidden" name="hasAttachment" value="${topic.hasAttachment}"/>
<c:if test="${topic.hasAttachment}">
	<fmt:message key="message.label.attachment"/>: 
	
	<ul>
		<c:forEach var="file" items="${topic.message.attachments}">
		    <li>
		    	<c:out value="${file.fileName}" /> 
		    	
				<c:set var="downloadURL">
					<lams:WebAppURL />download/?uuid=${file.fileDisplayUuid}&versionID=${file.fileVersionId}&preferDownload=true
				</c:set>
				<a href="<c:out value='${downloadURL}' escapeXml='false'/>" class="btn btn-sm btn-light"> 
					<fmt:message key="label.download" />
					<i class="fa-solid fa-download ms-1"></i>
				</a>
		
			    <button type="button" id="removeAttachmentButton" onclick="removeAtt('${sessionMapID}')" class="btn btn-light btn-sm">
			    	<fmt:message key="label.delete" />
			    	<i class="fa-solid fa-trash text-danger ms-1"></i>
			    </button>
		  	</li>
	  	</c:forEach>
	</ul>
</c:if>

<c:if test="${not topic.hasAttachment && allowUpload}">
	<input type="hidden" id="tmpFileUploadId" name="tmpFileUploadId"
			   value="${empty messageForm.tmpFileUploadId ? tmpFileUploadId : messageForm.tmpFileUploadId}" />
	<label for="uppy-upload-button">
		<fmt:message key="message.label.attachment" />
	</label>
	<div id="image-upload-area"></div>
	<script>
		initFileUpload('${empty messageForm.tmpFileUploadId ? tmpFileUploadId : messageForm.tmpFileUploadId}', true, '<lams:user property="localeLanguage"/>');
	</script>
</c:if>
