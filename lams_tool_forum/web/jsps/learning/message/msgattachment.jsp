<%@ include file="/common/taglibs.jsp" %>

<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<input type="hidden" name="hasAttachment" value="${topic.hasAttachment}"/>
<c:if test="${topic.hasAttachment}">
		<c:forEach var="file" items="${topic.message.attachments}">
		    <a id="removeAttachmentButton" href="#" onclick="removeAtt('${sessionMapID}')" class="btn btn-default btn-xs"><i class="fa fa-trash-o"></i></a>
			<fmt:message key="message.label.attachment"/>: 
			<c:set var="downloadURL">
				<lams:WebAppURL />download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true
			</c:set>
			<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <c:out value="${file.fileName}" /> </a>
	  		<BR/>
  		</c:forEach>
	</ul>
</c:if>

<c:if test="${not topic.hasAttachment && allowUpload}">
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
	<lams:FileUpload fileFieldname="attachmentFile" maxFileSize="${sessionMap.uploadMaxFileSize}"/>
</c:if>
