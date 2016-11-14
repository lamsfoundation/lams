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
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				<fmt:message key="label.authoring.basic.add.image" />
			</div>
		</div>
			
		<div class="panel-body">
		
			<%@ include file="/common/messages.jsp"%>
			
			<html:form action="/learning/saveNewImage" method="post" styleId="imageGalleryItemForm" enctype="multipart/form-data">
				<html:hidden property="sessionMapID" />
	
				<div class="form-group">
				    <label for="image-title">
				    	<fmt:message key="label.authoring.basic.resource.title.input"/>
				    </label>
				    <html:text property="title" styleClass="form-control" styleId="image-title" tabindex="1"/>
				</div>
		
				<div class="form-group">
					<label for="description">
						<fmt:message key="label.authoring.basic.resource.description.input" />
					</label>
					<lams:STRUTS-textarea rows="5" tabindex="4" styleClass="text-area form-control" property="description" />
				</div>
	
				<div class="form-group">
				    <label for="file">
				    	<fmt:message key="label.authoring.basic.resource.file.input"/>
				    </label>
					<input type="file" name="file" />
				</div>
				
				<a href="<html:rewrite page='/learning/initMultipleImages.do?sessionMapID='/>${sessionMapID}&KeepThis=true&TB_iframe=true&height=500&modal=true" 
						class="thickbox btn btn-default btn-xs">  
					<fmt:message key="label.authoring.basic.upload.multiple.images" />
				</a>
			</html:form>
			
			<div class="voffset10 pull-right">
				<a href="#nogo" onclick="document.imageGalleryItemForm.submit();" class="btn btn-default btn-sm button-add-item">
					<fmt:message key="label.authoring.basic.add.image" /> 
				</a>
				<a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-default btn-sm loffset5">
					<fmt:message key="label.cancel" /> 
				</a>
			</div>
	
		<div id="footer"></div>
		<!--closes footer-->
	</div>	
</body>
</lams:html>
