<%@ include file="/common/taglibs.jsp"%>
<c:choose>
	<c:when test="${saveUsingLearningAction}">
		<c:set var="FORM_ACTION" value="../learning/saveNewImage.do"/>
	</c:when>
	<c:otherwise>
		<c:set var="FORM_ACTION" value="saveOrUpdateImage.do"/>
	</c:otherwise>
</c:choose>

<c:set var="csrfToken"><csrf:token/></c:set>
<form:form action="${FORM_ACTION}?${csrfToken}" method="post" modelAttribute="imageGalleryItemForm" id="imageGalleryItemForm">
	<c:set var="sessionMap"	value="${sessionScope[imageGalleryForm.sessionMapID]}" />
	
	<div class="card lcard">
		<div class="card-header">
			<fmt:message key="label.authoring.basic.add.image" />
		</div>
			
		<div class="card-body">
			<lams:errors/>
			<form:hidden path="sessionMapID" />
			<form:hidden path="imageIndex" />
	
			<div class="mb-3">
			    <label for="file-title">
			    	<fmt:message key="label.authoring.basic.resource.title.input"/>
			    </label>
			    <input type="text" name="title" value="${imageGalleryItemForm.title}" class="form-control input-sm" id="file-title" maxlength="255"/>
			</div>
		
			<div class="mb-3">
				<label for="description">
					<fmt:message key="label.authoring.basic.resource.description.input" />
				</label>

				<c:choose>
					<c:when test="${saveUsingLearningAction}">
						<lams:textarea rows="5" class="text-area form-control" name="description"></lams:textarea>
					</c:when>
					<c:otherwise>
						<lams:CKEditor id="description" value="${imageGalleryItemForm.description}" width="99%" 
							contentFolderID="${sessionMap.imageGalleryForm.contentFolderID}" 
							resizeParentFrameName="new-image-input-area" />
					</c:otherwise>
				</c:choose>
			</div>
				
			<div class="mb-3">
				<c:set var="itemAttachment" value="${imageGalleryItemForm}"/>
				<div id="itemAttachmentArea">
					<%@ include file="/pages/authoring/parts/imagefile.jsp"%>
				</div>
			</div>
	
			<div class="text-center" style="display:none" id="itemAttachmentArea_Busy">
				<i class="fa fa-refresh fa-spin fa-2x fa-fw text-primary"></i>
			</div>
		
			<div id="uploadButtons" class="mt-3 float-end">
			    <button type="button" onclick="javascript:cancelImageGalleryItem()" class="btn btn-sm btn-secondary btn-icon-cancel me-2">
					<fmt:message key="label.cancel" /> 
				</button>
				
				<button type="button" id="addImageBtn" onclick="javascript:submitImageGalleryItem()" class="btn btn-sm btn-secondary btn-icon-add">
					<fmt:message key="label.authoring.basic.add.image" /> 
				</button>
			</div>
			
		</div>
	</div>

</form:form>
