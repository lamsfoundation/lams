<%@ include file="/common/taglibs.jsp"%>

<div>${item.description}</div>

<!-- Comments Part -->
<c:if test="${item.commentsAllowed}">
	<div id="comment-list-${item.uid}">
		<%@ include file="commentlist.jsp"%>
	</div>
</c:if>

<!-- Uploaded Attachments -->
<c:if test="${item.filesAllowed}">
	<form:form action="uploadFile.do?sessionMapID=${sessionMapID}&mode=${mode}&itemUid=${item.uid}"
		modelAttribute="taskListItemForm" method="post"
		enctype="multipart/form-data" onsubmit="return validateFiles();">
		<c:if test="${(mode != 'teacher') && !itemDTO.attachmentRequirementsMet}">
			<lams:Alert5 id="fileRequired" close="true" type="info">
				<fmt:message key="label.learning.info.upload.file.required" />
			</lams:Alert5>
		</c:if>

		<div class="card-subheader mt-3">
			<fmt:message key="label.preview.upload.file" />
		</div>
		<%@ include file="filelist.jsp"%>

		<c:if test="${sessionMap.mode != 'teacher'}">
			<input type="hidden" id="tmpFileUploadId" name="tmpFileUploadId" value="${taskListItemForm.tmpFileUploadId}_${item.uid}" />

			<div class="mt-2 d-flex align-items-center text-nowrap">
				<div id="file-upload-area-${item.uid}" class="flex-fill me-2">
				</div>
				<button type="submit" name="uploadedFileButton" class="btn btn-secondary btn-disable-on-submit order-last">
					<fmt:message key="label.preview.upload.button" />
					<i class="fa-solid fa-upload ms-1"></i>
				</button>
			</div>
			<script>
				initFileUpload('#file-upload-area-${item.uid}', '${taskListItemForm.tmpFileUploadId}_${item.uid}', '<lams:user property="localeLanguage"/>');
			</script>
		</c:if>
	</form:form>

	<lams:WaitingSpinner id="attachmentArea_Busy" />
</c:if>
