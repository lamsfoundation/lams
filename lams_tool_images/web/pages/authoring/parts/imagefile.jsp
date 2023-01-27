<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />

<input type="hidden" name="hasFile" value="${itemAttachment.hasFile}" id="has-file"/>
<c:if test="${itemAttachment.hasFile}">
	<p>
		<fmt:message key="label.authoring.image" />: <c:out value="${itemAttachment.fileName}" />
		<c:set var="viewURL">
			<lams:WebAppURL />download/?uuid=${itemAttachment.fileDisplayUuid}&preferDownload=false
		</c:set>					
		&nbsp;&nbsp;&nbsp;
		<a href="#nogo" onclick="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')" class="btn btn-default btn-xs">
			<fmt:message key="label.view" />
		</a>
	</p>
	<p>
		<fmt:message key="label.authoring.basic.replace.image" />
	</p>
</c:if>

<input type="hidden" name="tmpFileUploadId" value="${imageGalleryItemForm.tmpFileUploadId}" />
<div id="image-upload-area" class="voffset20"></div>
<script>
	initFileUpload('${imageGalleryItemForm.tmpFileUploadId}', ${itemAttachment.hasFile or saveUsingLearningAction}, '<lams:user property="localeLanguage"/>');
</script>
