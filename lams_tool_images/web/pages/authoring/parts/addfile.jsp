<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed"/>

		<script type="text/javascript">
	   <%-- user for  imageGalleryitem.js --%>
       var removeItemAttachmentUrl = "<c:url value='/authoring/removeItemAttachment.do'/>";
	</script>
		<script type="text/javascript"
			src="<html:rewrite page='/includes/javascript/imageGalleryitem.js'/>"></script>
	</lams:head>
	<body>

		<!-- Basic Info Form-->

		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveOrUpdateItem" method="post"
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
				<lams:FCKEditor id="description" value="${formBean.description}" contentFolderID="${sessionMap.imageGalleryForm.contentFolderID}" />
			</div>

			<div class="field-name space-top">
				<fmt:message key="label.authoring.basic.resource.file.input" />
			</div>
			<c:set var="itemAttachment"
				value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<div id="itemAttachmentArea">
				<%@ include file="/pages/authoring/parts/itemattachment.jsp"%>
			</div>

		</html:form>

		<lams:ImgButtonWrapper>
			<a href="#" onclick="document.imageGalleryItemForm.submit();" class="button-add-item"><fmt:message
					key="label.authoring.basic.add.image" /> </a>
			<a href="javascript:;" onclick="cancelImageGalleryItem()" class="button space-left"><fmt:message
					key="label.cancel" /> </a>
		</lams:ImgButtonWrapper>
	</body>
</lams:html>
