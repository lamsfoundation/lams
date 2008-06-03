<html:form action="learning/addNewComment" method="post" enctype="multipart/form-data">

	<p>${item.description}</p>

	<c:if test="${item.commentsFilesAllowed}">
				
		<!-- Comments Part -->
		<c:if test="${item.commentsAllowed}">
		
			<br/><br/>
			<%@ include file="commentlist.jsp"%>
				
			<c:if test="${sessionMap.mode != 'teacher'}">
				<div class="field-name">
					<fmt:message key="label.preview.add.comment" />
				</div>
				
				<div >
					<html:textarea property="comment" rows="2" style="width: 65%;"/>
	
					<html:button property="commentButton" onclick="javascript:addNewComment(this.form, ${item.uid});" styleClass="button" style="position:relative; left:40px; top:-35px;">
						<fmt:message key="label.preview.post" />
					</html:button>
				</div>
	
			</c:if>
		</c:if>
						
		<!-- Uploaded Attachments -->	
		<c:if test="${item.filesAllowed}">
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

