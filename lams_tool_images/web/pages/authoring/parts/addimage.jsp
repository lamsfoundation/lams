<%@ include file="/common/taglibs.jsp"%>
<c:choose>
	<c:when test="${sessionMap.mode == 'author'}">
		<c:set var="FORM_ACTION" value="/authoring/saveOrUpdateImage"/>
	</c:when>
	<c:otherwise>
		<c:set var="FORM_ACTION" value="/learning/saveNewImage.do"/>
	</c:otherwise>
</c:choose>
				
<html:form action="${FORM_ACTION}" method="post" styleId="imageGalleryItemForm" enctype="multipart/form-data">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap"	value="${sessionScope[formBean.sessionMapID]}" />
	
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				<fmt:message key="label.authoring.basic.add.image" />
				<div class="pull-right">
					<c:if test="${empty formBean.imageIndex}">
						<a href="javascript:showMessage('<html:rewrite page="/authoring/initMultipleImages.do?sessionMapID=${formBean.sessionMapID}"/>');" 
								class="btn btn-default btn-xs">
							<fmt:message key="label.authoring.basic.upload.multiple.images" />
						</a>
					</c:if>		
				</div>
			</div>
		</div>
			
		<div class="panel-body">
			<%@ include file="/common/messages.jsp"%>
			<html:hidden property="sessionMapID" />
			<html:hidden property="imageIndex" />
	
			<div class="form-group">
			    <label for="file-title">
			    	<fmt:message key="label.authoring.basic.resource.title.input"/>
			    </label>
			    <html:text property="title" styleClass="form-control input-sm" styleId="file-title" tabindex="1"/>
			</div>
		
			<div class="form-group">
				<label for="description">
					<fmt:message key="label.authoring.basic.resource.description.input" />
				</label>
				
				<c:choose>
					<c:when test="${sessionMap.mode == 'author'}">
						<lams:CKEditor id="description" value="${formBean.description}" width="99%" 
							contentFolderID="${sessionMap.imageGalleryForm.contentFolderID}" 
							resizeParentFrameName="new-image-input-area" />
					</c:when>
					<c:otherwise>
						<lams:STRUTS-textarea rows="5" tabindex="2" styleClass="text-area form-control" property="description" />
					</c:otherwise>
				</c:choose>
				
			</div>
				
			<div class="form-group">
				<c:set var="itemAttachment" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
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

</html:form>