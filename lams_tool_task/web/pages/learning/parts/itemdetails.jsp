<div class="row">
	<div class="col-xs-12">
		<div class="panel-body">${item.description}</div>

		<c:if test="${item.commentsFilesAllowed}">
			<hr class="msg-hr">
			<!-- Comments Part -->
			<c:if test="${item.commentsAllowed}">
				<html:form action="/learning/addNewComment.do?sessionMapID=${sessionMapID}&mode=${mode}&itemUid=${item.uid}"
					method="post" enctype="multipart/form-data" onsubmit="disableButtons()">

					<%@ include file="commentlist.jsp"%>

					<c:if test="${mode != 'teacher'}">

						<div class="form-group voffset5">
							<label for="comment"><fmt:message key="label.preview.add.comment" /></label>
							<lams:STRUTS-textarea property="comment" rows="2" styleId="comment" styleClass="form-control" index="${item.uid}" />
							<c:if test="${(mode != 'teacher') && !itemDTO.commentRequirementsMet}">
								<div class="help-block">
									<fmt:message key="label.learning.info.add.comment.required" />
								</div>
							</c:if>

							<input type="submit" name="commentButton" value='<fmt:message key="label.preview.post" />'
								class="btn btn-default btn-disable-on-submit voffset5" />
						</div>

					</c:if>
				</html:form>
			</c:if>

			<!-- Uploaded Attachments -->
			<c:if test="${item.filesAllowed}">
				<hr class="msg-hr">				
				<html:form action="/learning/uploadFile.do?sessionMapID=${sessionMapID}&mode=${mode}&itemUid=${item.uid}"
					method="post" enctype="multipart/form-data" onsubmit="return validateFiles();">
					<c:choose>
						<c:when test="${(mode != 'teacher') && !itemDTO.attachmentRequirementsMet}">
							<lams:Alert id="fileRequired" close="true" type="info">
								<fmt:message key="label.learning.info.upload.file.required" />
							</lams:Alert>
						</c:when>
						<c:otherwise>
					</c:otherwise>
					</c:choose>

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
				</html:form>
				
				<lams:WaitingSpinner id="attachmentArea_Busy"/>
			</c:if>
		</c:if>

	</div>
</div>