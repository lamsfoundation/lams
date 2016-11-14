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
	
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				<fmt:message key="label.authoring.basic.add.multiple.images" />
			</div>
		</div>
			
		<div class="panel-body">
		
			<%@ include file="/common/messages.jsp"%>
			
			<html:form action="/monitoring/saveMultipleImages" method="post" styleId="multipleImagesForm" enctype="multipart/form-data">
				<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				<html:hidden property="sessionMapID" />
	
				<div class="form-group">
					<label>
						<fmt:message key="label.authoring.basic.resource.files.input" />
					</label>
					
					<div>
						<input type="file" name="file1" />
					</div>
					<div class="voffset5">
						<input type="file" name="file2" />
					</div>
					<div class="voffset5">
						<input type="file" name="file3" />
					</div>
					<div class="voffset5">
						<input type="file" name="file4" />
					</div>
					<div class="voffset5">
						<input type="file" name="file5" />
					</div>
				</div>
				
				<a href="<html:rewrite page='/monitoring/newImageInit.do?sessionMapID='/>${formBean.sessionMapID}&KeepThis=true&TB_iframe=true&modal=true" 
						class="thickbox btn btn-default btn-xs">  
					<fmt:message key="label.authoring.basic.upload.single.image" />
				</a>
				
			</html:form>

			<div class="voffset10 pull-right">
				<a href="#nogo" onclick="javascript:document.multipleImagesForm.submit();" class="btn btn-default button-add-item">
					<fmt:message key="label.authoring.basic.add.images" /> 
				</a>
				<a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-default loffset5">
					<fmt:message key="label.cancel" /> 
				</a>
			</div>
		
		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->
	</div>		
</body>
</lams:html>
