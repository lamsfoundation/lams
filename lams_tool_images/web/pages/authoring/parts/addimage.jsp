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
	
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				<fmt:message key="label.authoring.basic.add.image" />
			</div>
		</div>
			
		<div class="panel-body">
			<lams:errors/>
			<form:hidden path="sessionMapID" />
			<form:hidden path="imageIndex" />
	
			<div class="form-group">
			    <label for="file-title">
			    	<fmt:message key="label.authoring.basic.resource.title.input"/>
			    </label>
			    <input type="text" name="title" value="${imageGalleryItemForm.title}" class="form-control input-sm" id="file-title" maxlength="255"/>
			</div>
		
			<div class="form-group">
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
				
			<div class="form-group">
				<c:set var="itemAttachment" value="${imageGalleryItemForm}"/>
				<div id="itemAttachmentArea">
					<%@ include file="/pages/authoring/parts/imagefile.jsp"%>
				</div>
			</div>
	
			<div class="panel-body text-center" style="display:none" id="itemAttachmentArea_Busy">
				<i class="fa fa-refresh fa-spin fa-2x fa-fw text-primary"></i>
			</div>
		
			<div id="uploadButtons" class="voffset10 pull-right">
			    <a href="#nogo" onclick="javascript:cancelImageGalleryItem()" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.cancel" /> 
				</a>
				<a href="#nogo" id="addImageBtn" onclick="javascript:submitImageGalleryItem()" class="btn btn-sm btn-default button-add-item">
					<fmt:message key="label.authoring.basic.add.image" /> 
				</a>
			</div>
			
		</div>
	</div>

</form:form>
