<html:form action="learning/addNewComment" method="post" enctype="multipart/form-data">

	<p>${item.description}</p>
<!-- 	
					<c:if test="${mode != 'teacher' && (!itemDTO.commentRequirementsMet || !itemDTO.attachmentRequirementsMet)}">
						<div class="info space-bottom">
							<c:choose>
								<c:when test="${!itemDTO.commentRequirementsMet && !itemDTO.attachmentRequirementsMet}">
									<fmt:message key="label.learning.info.comment.and.attachment.required" />		
								</c:when>
								<c:when test="${!itemDTO.commentRequirementsMet}">
									<fmt:message key="label.learning.info.add.comment.required" />		
								</c:when>							
								<c:when test="${!itemDTO.attachmentRequirementsMet}">
									<fmt:message key="label.learning.info.upload.file.required" />	
								</c:when>							
							</c:choose>
						</div>
					</c:if>
 -->
	<c:if test="${item.commentsFilesAllowed}">
				
		<!-- Comments Part -->
		<c:if test="${item.commentsAllowed}">

			<c:choose>
				<c:when test="${(mode != 'teacher') && !itemDTO.commentRequirementsMet}">
					<div class="info space-bottom">
						<fmt:message key="label.learning.info.add.comment.required" />
					</div>
				</c:when>
				<c:otherwise>
					<br/>
				</c:otherwise>
			</c:choose>
					
			<%@ include file="commentlist.jsp"%>
				
			<c:if test="${mode != 'teacher'}">
				<div class="field-name">
					<fmt:message key="label.preview.add.comment" />
				</div>
				
				<div >
 					<html:textarea property="comment" rows="2" style="width: 65%;" />  
<!--					<lams:STRUTS-textarea property="comment" rows="2" style="width: 65%;"/>-->
	
					<html:button property="commentButton" onclick="javascript:addNewComment(this.form, ${item.uid});" styleClass="button" style="position:relative; left:40px; top:-35px;">
						<fmt:message key="label.preview.post" />
					</html:button>
				</div>
	
			</c:if>
		</c:if>
						
		<!-- Uploaded Attachments -->	
		<c:if test="${item.filesAllowed}">
		
			<c:choose>
				<c:when test="${(mode != 'teacher') && !itemDTO.attachmentRequirementsMet}">
					<div class="info space-bottom">
						<fmt:message key="label.learning.info.upload.file.required" />
					</div>
				</c:when>
				<c:otherwise>
					<br/>
				</c:otherwise>
			</c:choose>
		
			<%@ include file="filelist.jsp"%>
						
			<c:if test="${sessionMap.mode != 'teacher'}">
				<div class="field-name">
					<fmt:message key="label.preview.upload.file" />
				</div>
				<html:file property="uploadedFile">
					<fmt:message key="label.authoring.choosefile.button" />
				</html:file>
									 
				<html:button property="uploadedFileButton" onclick="javascript:doUpload(this.form, ${item.uid});" styleClass="button">
					<fmt:message key="label.preview.upload.button" /> 
				</html:button>
									
			</c:if>
		</c:if>
			
	</c:if>

</html:form>

