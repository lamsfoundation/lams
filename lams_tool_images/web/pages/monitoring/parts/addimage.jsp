<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
		
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title>
			<fmt:message key="label.learning.title" />
		</title>
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	
	<body class="stripes">
		<div id="content" >
			<%@ include file="/common/messages.jsp"%>
			
			<html:form action="/monitoring/saveNewImage" method="post" styleId="imageGalleryItemForm" enctype="multipart/form-data">
				<html:hidden property="sessionMapID" />
	
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
					<lams:STRUTS-textarea rows="5" cols="20" tabindex="4" styleClass="text-area" property="description" />
				</div>
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.resource.file.input" />
				</div>
				<div id="itemAttachmentArea">
					<input type="file" name="file" />			
				</div>
				
				<div style="margin-bottom: 0px; margin-top: 15px;">
					<a href="<html:rewrite page='/monitoring/initMultipleImages.do?sessionMapID='/>${formBean.sessionMapID}&KeepThis=true&TB_iframe=true&height=500&width=480&modal=true" class="thickbox">  
						<fmt:message key="label.authoring.basic.upload.multiple.images" />
					</a>				
				</div>				
			</html:form>

			<br><br>
	
			<lams:ImgButtonWrapper>
				<a href="#" onclick="document.imageGalleryItemForm.submit();" class="button-add-item">
					<fmt:message key="label.authoring.basic.add.image" /> 
				</a>
				<a href="#" onclick="self.parent.tb_remove();" class="button space-left">
					<fmt:message key="label.cancel" /> 
				</a>
			</lams:ImgButtonWrapper>
		
		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->		
	</body>
</lams:html>
