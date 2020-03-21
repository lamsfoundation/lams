<%@ include file="/common/taglibs.jsp"%>

<div class="row">
	<div class="col-12">
		<div class="panel-body">${item.description}</div>
		
		<!-- Comments Part -->
		<c:if test="${item.commentsAllowed}">
			<br>
			<div id="comment-list-${item.uid}">
				<%@ include file="commentlist.jsp"%>
			</div>
		</c:if>

		<!-- Uploaded Attachments -->
		<c:if test="${item.filesAllowed}">
			<br>
						
			<form:form action="uploadFile.do?sessionMapID=${sessionMapID}&mode=${mode}&itemUid=${item.uid}"
					modelAttribute="taskListItemForm" method="post" enctype="multipart/form-data" onsubmit="return validateFiles();">
				<c:if test="${(mode != 'teacher') && !itemDTO.attachmentRequirementsMet}">
					<lams:Alert id="fileRequired" close="true" type="info">
						<fmt:message key="label.learning.info.upload.file.required" />
					</lams:Alert>
				</c:if>

				<h5>
					<fmt:message key="label.preview.upload.file" />
				</h5>
				<%@ include file="filelist.jsp"%>
				<c:if test="${sessionMap.mode != 'teacher'}">
					<lams:FileUpload fileFieldId="uploadButton"  fileFieldname="uploadedFile" fileInputMessageKey="label.authoring.choosefile.button"
						maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
					<input type="submit" name="uploadedFileButton" value='<fmt:message key="label.preview.upload.button" />'
						class="btn btn-default btn-disable-on-submit voffset5" />
				</c:if>
			</form:form>
				
			<lams:WaitingSpinner id="attachmentArea_Busy"/>
		</c:if>

	</div>
</div>