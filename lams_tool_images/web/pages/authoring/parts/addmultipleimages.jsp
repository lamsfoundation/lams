<!DOCTYPE html>
		
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
		<lams:css style="main"/>

		<script type="text/javascript">
		   <%-- user for  imageGalleryitem.js --%>
	       var removeItemAttachmentUrl = "<c:url value='/authoring/removeItemAttachment.do'/>";
		</script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/imageGalleryitem.js'/>"></script>
	</lams:head>
	<body>

		<!-- Basic Info Form-->

		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveMultipleImages" method="post" styleId="multipleImagesForm" enctype="multipart/form-data">
			<html:hidden property="sessionMapID" />

			<h2 class="no-space-left">
				<fmt:message key="label.authoring.basic.add.multiple.images" />
			</h2>

			<div class="field-name space-top">
				<fmt:message key="label.authoring.basic.resource.files.input" />
			</div>
			<div style="margin-left: 15px;">
				<input type="file" name="file1" />
			</div>
			<div style="margin-top: 4px; margin-left: 15px;">
				<input type="file" name="file2" />
			</div>
			<div style="margin-top: 4px; margin-left: 15px;">
				<input type="file" name="file3" />
			</div>
			<div style="margin-top: 4px; margin-left: 15px;">
				<input type="file" name="file4" />
			</div>
			<div style="margin-top: 4px; margin-left: 15px;">
				<input type="file" name="file5" />
			</div>

			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />			
			<div style="margin-bottom: 10px; margin-top: 15px;">
				<a href="javascript:showMessage('<html:rewrite page="/authoring/newImageInit.do?sessionMapID=${formBean.sessionMapID}"/>');" >
					<fmt:message key="label.authoring.basic.upload.single.image" />
				</a>
			</div>			

		</html:form>

		<lams:ImgButtonWrapper>
			<a href="#" onclick="document.multipleImagesForm.submit();" class="button-add-item"><fmt:message
					key="label.authoring.basic.add.images" /> </a>
			<a href="javascript:;" onclick="cancelImageGalleryItem()" class="button space-left"><fmt:message
					key="label.cancel" /> </a>
		</lams:ImgButtonWrapper>
	</body>
</lams:html>
