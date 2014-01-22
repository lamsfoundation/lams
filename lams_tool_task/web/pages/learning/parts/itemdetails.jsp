

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
			<html:form action="/learning/addNewComment.do?sessionMapID=${sessionMapID}&mode=${mode}&itemUid=${item.uid}"
					   method="post" enctype="multipart/form-data">
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
	 					<!-- <html:textarea property="comment" rows="2" style="width: 65%;" />  -->
						<lams:STRUTS-textarea property="comment" rows="2" style="width: 65%;" index="${item.uid}"/>
						
						<input type="submit" name="commentButton" value='<fmt:message key="label.preview.post" />'
							   class="button"style="position:relative; left:40px; top:-35px;" />
					</div>
		
				</c:if>
			</html:form>
		</c:if>
			
		<!-- Uploaded Attachments -->	
		<c:if test="${item.filesAllowed}">
			<html:form action="/learning/uploadFile.do?sessionMapID=${sessionMapID}&mode=${mode}&itemUid=${item.uid}"
					   method="post" enctype="multipart/form-data">
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
										 
					<input type="submit" name="uploadedFileButton" value='<fmt:message key="label.preview.upload.button" />'
						   class="button" />										
				</c:if>
			</html:form>
		</c:if>
	</c:if>