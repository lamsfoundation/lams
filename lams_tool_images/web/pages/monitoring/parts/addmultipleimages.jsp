<!DOCTYPE html>
		
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
			
			<html:form action="/monitoring/saveMultipleImages" method="post" styleId="multipleImagesForm" enctype="multipart/form-data">
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
				<div style="margin-bottom: 0px; margin-top: 15px;">
					<a href="<html:rewrite page='/monitoring/newImageInit.do?sessionMapID='/>${formBean.sessionMapID}&KeepThis=true&TB_iframe=true&height=540&width=480&modal=true" class="thickbox">  
						<fmt:message key="label.authoring.basic.upload.single.image" />
					</a>				
				</div>
				
			</html:form>

			<br><br>
	
			<lams:ImgButtonWrapper>
				<a href="#" onclick="document.multipleImagesForm.submit();" class="button-add-item">
					<fmt:message key="label.authoring.basic.add.images" /> 
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
