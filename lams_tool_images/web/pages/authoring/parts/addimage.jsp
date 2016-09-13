<%@ include file="/common/taglibs.jsp"%>
	
<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title">
			<fmt:message key="label.authoring.basic.add.image" />
		</div>
	</div>
		
	<div class="panel-body">

		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveOrUpdateImage" method="post" styleId="imageGalleryItemForm" enctype="multipart/form-data">
			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<c:set var="sessionMap"	value="${sessionScope[formBean.sessionMapID]}" />
			<html:hidden property="sessionMapID" />
			<html:hidden property="imageIndex" />
	
			<div class="form-group">
			    <label for="file-title">
			    	<fmt:message key="label.authoring.basic.resource.title.input"/>
			    </label>
			    <html:text property="title" styleClass="form-control" styleId="file-title" tabindex="1"/>
			</div>
		
			<div class="form-group">
				<label for="description">
					<fmt:message key="label.authoring.basic.resource.description.input" />
				</label>
	            	
				<lams:CKEditor id="description" value="${formBean.description}" width="99%" 
					contentFolderID="${sessionMap.imageGalleryForm.contentFolderID}" 
					resizeParentFrameName="reourceInputArea" />
			</div>
				
			<div class="form-group">
			    <label for="itemAttachmentArea">
			    	<fmt:message key="label.authoring.basic.resource.file.input"/>
			    </label>
				    
				<c:set var="itemAttachment" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				<div id="itemAttachmentArea">
					<%@ include file="/pages/authoring/parts/imagefile.jsp"%>
				</div>
			</div>
				
			<c:if test="${empty formBean.imageIndex}">
				<a href="javascript:showMessage('<html:rewrite page="/authoring/initMultipleImages.do?sessionMapID=${formBean.sessionMapID}"/>');" 
						class="btn btn-default btn-xs">
					<fmt:message key="label.authoring.basic.upload.multiple.images" />
				</a>
			</c:if>		
	
		</html:form>
	
		<div class="voffset10 pull-right">
		    <a href="#nogo" onclick="javascript:cancelImageGalleryItem()" class="btn btn-sm btn-default loffset5">
				<fmt:message key="label.cancel" /> 
			</a>
			<a href="#nogo" onclick="javascript:submitImageGalleryItem()" class="btn btn-sm btn-default button-add-item">
				<fmt:message key="label.authoring.basic.add.image" /> 
			</a>
			
		</div>
		
	</div>
</div>
