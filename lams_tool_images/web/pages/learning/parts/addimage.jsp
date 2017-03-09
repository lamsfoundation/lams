<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:choose>
	<c:when test="${sessionMap.mode == 'teacher'}">
		<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
		<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
		<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE))%></c:set>
	</c:otherwise>
</c:choose>

<lams:html>
	<lams:head>
		<title>
			<fmt:message key="label.learning.title" />
		</title>
		<%@ include file="/common/header.jsp"%>
 		<script type="text/javascript" src="${lams}includes/javascript/upload.js"></script>
		
		<script type="text/javascript">
			$(document).ready(function(){
				document.getElementById("imageTitle").focus();
			});
			
			function submitImage(){

				var UPLOAD_FILE_MAX_SIZE = ${UPLOAD_FILE_MAX_SIZE};
				var LABEL_ITEM_BLANK = '<fmt:message key="error.resource.item.file.blank"/>';
				var LABEL_MAX_FILE_SIZE = '<fmt:message key="errors.maxfilesize"><param>{0}</param></fmt:message>';
				var LABEL_NOT_ALLOWED_FORMAT = '<fmt:message key="error.resource.image.not.alowed.format"/>';	
				
				if ( typeof CKEDITOR !== 'undefined' ) {
					for ( instance in CKEDITOR.instances ) {
						CKEDITOR.instances[instance].updateElement();
					}
				}

				var formData = new FormData();
				$.each($('#imageGalleryItemForm').serializeArray(), function(i, field) {
					formData.append(field.name, field.value);
				});
				
				// validate uploading file if we add it for the first time
				if (!eval($("#has-file").val())) {
					var fileSelect = document.getElementById('file');
					// Get the selected files from the input.
					var files = fileSelect.files;
					if (files.length == 0) {
						clearFileError();
						showFileError(LABEL_ITEM_BLANK);
						return;
					}

					var file = files[0];
					if ( ! validateShowErrorImageType(file, LABEL_NOT_ALLOWED_FORMAT, false) || ! validateShowErrorFileSize(file, UPLOAD_FILE_MAX_SIZE, LABEL_MAX_FILE_SIZE, false) ) {
						return;
					}

					// Add the file to the request.
					formData.append('file', file, file.name);
					$('#uploadButtons').hide();
					$('#itemAttachmentArea_Busy').show();
				}

 				$.ajax({
			    	type: 'POST',
			    	url: $("#imageGalleryItemForm").attr('action'),
			    	data: formData,
			        processData: false,
			        contentType: false,
			    	success: function(data) {
			   			self.parent.checkNew();
			    	},
			    	error: function(jqXHR, textStatus, errorMessage) {
						$('#uploadButtons').show();
						$('#itemAttachmentArea_Busy').hide();
			        	alert(jqXHR.responseText);
			    	}
				}); 
			} 		
				
		</script>
	</lams:head>
	
<body class="stripes">
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				<fmt:message key="label.authoring.basic.add.image" />
					<a href="<html:rewrite page='/learning/initMultipleImages.do?sessionMapID='/>${sessionMapID}&KeepThis=true&TB_iframe=true&height=500&modal=true" 
							class="thickbox btn btn-default pull-right btn-xs">  
						<fmt:message key="label.authoring.basic.upload.multiple.images" />
					</a>
				
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
				    <html:text property="title" styleClass="form-control" styleId="imageTitle" tabindex="1"/>
				</div>
		
				<div class="form-group">
					<label for="description">
						<fmt:message key="label.authoring.basic.resource.description.input" />
					</label>
					<lams:STRUTS-textarea rows="5" tabindex="2" styleClass="text-area form-control" property="description" />
				</div>
	
				<lams:FileUpload fileFieldname="file" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}" tabindex="3"/>
										
			</html:form>

			<div class="panel-body text-center" style="display:none" id="itemAttachmentArea_Busy">
				<i class="fa fa-refresh fa-spin fa-2x fa-fw text-primary"></i>
			</div>

			
			<div id="uploadButtons" class="voffset10 pull-right">
				<a href="#nogo" tabindex="4" onclick="javascript:self.parent.tb_remove();" class="btn btn-default btn-sm loffset5">
					<fmt:message key="label.cancel" /> 
				</a>
				<a href="#nogo" tabindex="5" onclick="javascript:submitImage();" class="btn btn-default btn-sm button-add-item">
					<fmt:message key="label.authoring.basic.add.image" /> 
				</a>
			</div>
		</div>
	
		<div id="footer"></div>
		<!--closes footer-->
	</div>	
</body>
</lams:html>
