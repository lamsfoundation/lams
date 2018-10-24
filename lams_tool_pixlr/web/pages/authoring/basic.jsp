<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />
<c:set var="sessionMapID" value="${authoringForm.sessionMapID}" />

<script type="text/javascript">
	var image;
	var origImageHeight;
	var origImageWidth;
	
	function submitForm(actionMethod) {
        var form = document.getElementById("authoringForm");
		form.action= actionMethod+".do";
		form.submit();
		}
	
	function openImage(url) {
		openPopup(url, origImageHeight, origImageWidth);
	}	
</script>

<!-- ========== Basic Tab ========== -->
<div class="form-group">
    <label for="title">
    	<fmt:message key="label.authoring.basic.title"/>
    </label>
    <form:input path="title" cssClass="form-control"/>
</div>

<div class="form-group">
    <label for="instructions">
    	<fmt:message key="label.authoring.basic.instructions"/>
    </label>
	<lams:CKEditor id="instructions" value="${authoringForm.instructions}"
			contentFolderID="${sessionMap.contentFolderID}"/>
</div>

<div class="form-group">			
	<c:choose>
		<c:when test='${imageExists}'>
			<div style="text-align:center;">
				<img id="image" title="<fmt:message key="tooltip.openfullsize" />" 
						onclick="openImage('${imageURL}')" src="${imageURL}"/>
				<br>
				
				<a href="javascript:submitForm('deleteImage')" class="btn btn-xs btn-default voffset10">
					<fmt:message key="label.authoring.remove"/>
				</a>
			</div>
		</c:when>
		
		<c:otherwise>
		    <lams:FileUpload fileFieldname="file" fileInputMessageKey="label.authoring.basic.add.image" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
		</c:otherwise>
	</c:choose>
</div>

<lams:WaitingSpinner id="imageAttachmentDiv_Busy"/>
