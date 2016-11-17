<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="UPLOAD_FILE_LARGE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>

<lams:html>
<lams:head>
	<title>
		<fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>
		<script type="text/javascript">
			$(document).ready(function(){ 
				document.getElementById("imageTitle").focus();
				
			});
			
			function submitImage(){

				var UPLOAD_FILE_LARGE_MAX_SIZE = ${UPLOAD_FILE_LARGE_MAX_SIZE};
				var LABEL_ITEM_BLANK = '<fmt:message key="error.resource.item.file.blank"/>';
				var LABEL_MAX_FILE_SIZE = '<fmt:message key="errors.maxfilesize"/>';
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
						alert(LABEL_ITEM_BLANK);
						return;
					}
					var file = files[0];

				    // Check the file type.
					if (file.type != 'image/png' && file.type != 'image/jpg' && file.type != 'image/gif' && file.type != 'image/jpeg' ) {
						alert(LABEL_NOT_ALLOWED_FORMAT);
						return;
					} else if (file.size > UPLOAD_FILE_LARGE_MAX_SIZE) {
						alert(LABEL_MAX_FILE_SIZE + ' ' + (UPLOAD_FILE_LARGE_MAX_SIZE/1024/1000) + ' MBs');
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
			        	alert(errorMessage);
			    	}
				});
			} 		
				
		</script>	
</lams:head>
	
<body class="stripes">
	<html:form action="/monitoring/saveNewImage" method="post" styleId="imageGalleryItemForm" enctype="multipart/form-data">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap"	value="${sessionScope[formBean.sessionMapID]}" />

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				<fmt:message key="label.authoring.basic.add.image" />
				<a href="<html:rewrite page='/monitoring/initMultipleImages.do?sessionMapID='/>${formBean.sessionMapID}&KeepThis=true&TB_iframe=true&modal=true" 
					class="thickbox btn btn-default pull-right btn-xs">  
						<fmt:message key="label.authoring.basic.upload.multiple.images" />
				</a>				
				
			</div>
		</div>
			
		<div class="panel-body">
			<%@ include file="/common/messages.jsp"%>
			
				<html:hidden property="sessionMapID" />
	
				<div class="form-group">
				    <label for="file-title">
				    	<fmt:message key="label.authoring.basic.resource.title.input"/>
				    </label>
				    <html:text property="title" styleClass="form-control" styleId="imageTitle" tabindex="1"/>
				</div>
		
				<div class="form-group">
					<label for="description">
						<fmt:message key="label.authoring.basic.resource.description.input" />
					</label>
					<lams:STRUTS-textarea rows="3" styleClass="text-area form-control" tabindex="2" property="description" />
				</div>
			
				<div class="input-group">
				    <span class="input-group-btn">
							<button id="fileButtonBrowse" type="button" tabindex="3" class="btn btn-sm btn-default">
							<i class="fa fa-upload"></i> <fmt:message key="label.authoring.basic.resource.file.input"/>
						</button>
					</span>
					<input type="file" id="file" name="file" multiple style="display:none"> 
					<input type="text" id="fileInputName" style="display:none" disabled="disabled" placeholder="File not selected" class="form-control input-sm">
				</div>
						
				<script type="text/javascript">
					// Fake file upload
					document.getElementById('fileButtonBrowse').addEventListener('click', function() {
						document.getElementById('file').click();
					});
					
					document.getElementById('file').addEventListener('change', function() {
						$('#fileInputName').show();
						document.getElementById('fileInputName').value = this.value.replace(/^.*\\/, "");
						
					});
				</script>  
				
			</html:form>
			
			<div class="panel-body text-center" style="display:none" id="itemAttachmentArea_Busy">
				<i class="fa fa-refresh fa-spin fa-2x fa-fw text-primary"></i>
			</div>
			
	
			<div id="uploadButtons" class="voffset10 pull-right">
				<a href="#nogo" tabindex="4" onclick="javascript:self.parent.tb_remove();" class="btn btn-default btn-sm loffset5">
					<fmt:message key="label.cancel" /> 
				</a>
				<a href="#nogo"  tabindex="5" onclick="javascript:submitImage();" class="btn btn-default btn-sm button-add-item">
					<fmt:message key="label.authoring.basic.add.image" /> 
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
