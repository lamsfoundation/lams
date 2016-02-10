<!DOCTYPE html>
		
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
		<lams:css style="main"/>

		<script type="text/javascript">
		   <%-- used for  imageGalleryitem.js --%>
	       var removeItemAttachmentUrl = "<c:url value='/authoring/removeImageFile.do'/>";
		</script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/imageGalleryitem.js'/>"></script>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
		<script type="text/javascript">	
			$.noConflict();
			jQuery(function() {
				//change size of an iframe on ckeditor's autogrow 
				CKEDITOR.instances.description.on("instanceReady", function(e) {
				    e.editor.on('resize',function(reEvent){
				    	var iframe = window.parent.document.getElementById("reourceInputArea");
				    	iframe.style.height = eval(iframe.contentWindow.document.body.scrollHeight) + 'px';
				    });
				});
			});
		</script>
		
	</lams:head>
	<body>

		<!-- Basic Info Form-->

		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveOrUpdateImage" method="post"
			styleId="imageGalleryItemForm" enctype="multipart/form-data">
			<html:hidden property="sessionMapID" />
			<html:hidden property="imageIndex" />


			<h2 class="no-space-left">
				<fmt:message key="label.authoring.basic.add.image" />
			</h2>

			<div class="field-name">
				<fmt:message key="label.authoring.basic.resource.title.input" />
			</div>
			<html:text property="title" size="55" tabindex="1" />
	
			<div class="field-name space-top">
            	<fmt:message key="label.authoring.basic.resource.description.input" />
			</div>
			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<c:set var="sessionMap"	value="${sessionScope[formBean.sessionMapID]}" />
			<div class="small-space-bottom" >
				<lams:CKEditor id="description" value="${formBean.description}" width="99%" 
					contentFolderID="${sessionMap.imageGalleryForm.contentFolderID}" resizeParentFrameName="reourceInputArea" />
			</div>

			<div class="field-name space-top">
				<fmt:message key="label.authoring.basic.resource.file.input" />
			</div>
			<c:set var="itemAttachment" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<div id="itemAttachmentArea">
				<%@ include file="/pages/authoring/parts/imagefile.jsp"%>
			</div>
			
			<c:if test="${empty formBean.imageIndex}">
				<div style="margin-bottom: 10px; margin-top: 15px;">
					<a href="javascript:showMessage('<html:rewrite page="/authoring/initMultipleImages.do?sessionMapID=${formBean.sessionMapID}"/>');" >
						<fmt:message key="label.authoring.basic.upload.multiple.images" />
					</a>
				</div>	
			</c:if>		

		</html:form>

		<lams:ImgButtonWrapper>
			<a href="#" onclick="document.imageGalleryItemForm.submit();" class="button-add-item" style="padding-bottom: 40px;">
				<fmt:message key="label.authoring.basic.add.image" /> 
			</a>
			<a href="javascript:;" onclick="cancelImageGalleryItem()" class="button space-left">
				<fmt:message key="label.cancel" /> 
			</a>
		</lams:ImgButtonWrapper>
	</body>
</lams:html>
