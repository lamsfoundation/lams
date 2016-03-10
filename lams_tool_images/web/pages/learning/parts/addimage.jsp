<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title>
			<fmt:message key="label.learning.title" />
		</title>
		<%@ include file="/common/header.jsp"%>
		
		<script type="text/javascript">
			$(document).ready(function(){ 
				document.getElementById("image-title").focus();
			});
		</script>
	</lams:head>
	
	<body class="stripes">
		<c:set var="title"><fmt:message key="label.authoring.basic.add.image" /></c:set>
		<lams:Page type="learner" title="${title}">
		
			<%@ include file="/common/messages.jsp"%>
			
			<html:form action="/learning/saveNewImage" method="post" styleId="imageGalleryItemForm" enctype="multipart/form-data">
				<html:hidden property="sessionMapID" />
	
				<div class="field-name">
					<fmt:message key="label.authoring.basic.resource.title.input" />
				</div>
				<html:text property="title" tabindex="1" styleClass="form-control" styleId="image-title"/>
		
				<div class="field-name space-top">
	            	<fmt:message key="label.authoring.basic.resource.description.input" />
				</div>
				<div class="small-space-bottom" >
					<lams:STRUTS-textarea rows="5" tabindex="4" styleClass="text-area form-control" property="description" />
				</div>
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.resource.file.input" />
				</div>
				<div id="itemAttachmentArea">
					<input type="file" name="file" />			
				</div>
				
				<div style="margin-bottom: 0px; margin-top: 15px;">
					<a href="<html:rewrite page='/learning/initMultipleImages.do?sessionMapID='/>${sessionMapID}&KeepThis=true&TB_iframe=true&height=500&width=480&modal=true" class="thickbox">  
						<fmt:message key="label.authoring.basic.upload.multiple.images" />
					</a>				
				</div>
			</html:form>

			<br><br>
	
			<lams:ImgButtonWrapper>
				<a href="#" onclick="document.imageGalleryItemForm.submit();" class="button-add-item btn btn-primary ">
					<fmt:message key="label.authoring.basic.add.image" /> 
				</a>
				<a href="#" onclick="self.parent.tb_remove();" class="btn btn-primary  space-left" >
					<fmt:message key="label.cancel" /> 
				</a>
			</lams:ImgButtonWrapper>
		
		</lams:Page>
		<!--closes content-->
	
		<div id="footer"></div>
		<!--closes footer-->		
	</body>
</lams:html>
