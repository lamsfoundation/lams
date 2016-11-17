<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="UPLOAD_FILE_LARGE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>

<!DOCTYPE html>	
<lams:html>
<lams:head>
	<title>
		<fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>
		<script type="text/javascript">
			
			function submitMultipleImages(){
				
				var UPLOAD_FILE_LARGE_MAX_SIZE = ${UPLOAD_FILE_LARGE_MAX_SIZE};
				var LABEL_ITEM_BLANK = '<fmt:message key="error.resource.item.file.blank"/>';
				var LABEL_MAX_FILE_SIZE = '<fmt:message key="errors.maxfilesize"/>';
				var LABEL_NOT_ALLOWED_FORMAT = '<fmt:message key="error.resource.image.not.alowed.format"/>';	
				
				var formData = new FormData();
				formData.append('sessionMapID', $("#sessionMapID").val());
				
				// Get selected files from the inputs
				var validateFiles = [];
				if ($('#file1').val()) { 
					var file = document.getElementById('file1').files[0];
					validateFiles.push(file);
					formData.append('file1', file, file.name);
				}
				if ($('#file2').val()) { 
					var file = document.getElementById('file2').files[0];
					validateFiles.push(file);
					formData.append('file2', file, file.name);
				}
				if ($('#file3').val()) { 
					var file = document.getElementById('file3').files[0];
					validateFiles.push(file);
					formData.append('file3', file, file.name);
				}
				if ($('#file4').val()) {
					var file = document.getElementById('file4').files[0];
					validateFiles.push(file);
					formData.append('file4', file, file.name);
				}
				if ($('#file5').val()) { 
					var file = document.getElementById('file5').files[0];
					validateFiles.push(file);
					formData.append('file5', file, file.name);
				}
					
				// validate files 
				if (validateFiles.length == 0) {
					alert(LABEL_ITEM_BLANK);
					return;
				}
				for (var i = 0; i < validateFiles.length; i++) {
					  var file = validateFiles[i];
			
					  // Check the file type.
					  if (file.size > ${UPLOAD_FILE_LARGE_MAX_SIZE}) {
						  alert(LABEL_MAX_FILE_SIZE + ' ' + (UPLOAD_FILE_LARGE_MAX_SIZE/1024/1000) + ' MBs');
						  return;
					  } else if (file.type != 'image/png' && file.type != 'image/jpg' && file.type != 'image/gif' && file.type != 'image/jpeg' ) {
						  alert(LABEL_NOT_ALLOWED_FORMAT);
						  return;
					  }
			
					  // Add the file to the request.
				}
				
				$('#uploadButtons').hide();
				$('#itemAttachmentArea_Busy').show();
			
			
				$.ajax({
			    	type: 'POST',
			    	url: $("#multipleImagesForm").attr('action'),
			    	data: formData,
			        processData: false,
			        contentType: false,
			    	success: function(data) {
			    		self.parent.checkNew();
			    	},
			    	error: function(jqXHR, textStatus, errorMessage) {
			        	alert(errorMessage);
			    	}
				});
			} 
		</script>		
</lams:head>
	
<body class="stripes">
	
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				<fmt:message key="label.authoring.basic.add.multiple.images" />
				<a href="<html:rewrite page='/monitoring/newImageInit.do?sessionMapID='/>${formBean.sessionMapID}&KeepThis=true&TB_iframe=true&modal=true" 
						class="thickbox pull-right btn btn-default btn-xs">  
					<fmt:message key="label.authoring.basic.upload.single.image" />
				</a>
			</div>
		</div>
			
		<div class="panel-body">
		
			<%@ include file="/common/messages.jsp"%>
			
			<html:form action="/monitoring/saveMultipleImages" method="post" styleId="multipleImagesForm" enctype="multipart/form-data">
				<html:hidden styleId="sessionMapID" property="sessionMapID" />
	
				<div class="form-group">
					<label>
						<fmt:message key="label.authoring.basic.resource.files.input" />
					</label>
					
					<div>
						<div class="input-group">
						    <span class="input-group-btn">
									<button id="fileButtonBrowse1" type="button" class="btn btn-sm btn-default">
									<i class="fa fa-upload"></i> <fmt:message key="label.authoring.basic.resource.file.input"/>
								</button>
							</span>
							<input type="file" id="file1" name="file1" style="display:none"> 
							<input type="text" id="fileInputName1" style="display:none" disabled="disabled" class="form-control input-sm">
						</div>
								
						<script type="text/javascript">
							document.getElementById('fileButtonBrowse1').addEventListener('click', function() {
								document.getElementById('file1').click();
							});
							
							document.getElementById('file1').addEventListener('change', function() {
								$('#fileInputName1').show();
								document.getElementById('fileInputName1').value = this.value.replace(/^.*\\/, "");
								
							});
						</script>    
				
						<div class="input-group voffset5">
						    <span class="input-group-btn">
									<button id="fileButtonBrowse2" type="button" class="btn btn-sm btn-default">
									<i class="fa fa-upload"></i> <fmt:message key="label.authoring.basic.resource.file.input"/>
								</button>
							</span>
							<input type="file" id="file2" name="file2" style="display:none"> 
							<input type="text" id="fileInputName2" style="display:none" disabled="disabled" class="form-control input-sm">
						</div>
								
						<script type="text/javascript">
							document.getElementById('fileButtonBrowse2').addEventListener('click', function() {
								document.getElementById('file2').click();
							});
							
							document.getElementById('file2').addEventListener('change', function() {
								$('#fileInputName2').show();
								document.getElementById('fileInputName2').value = this.value.replace(/^.*\\/, "");
								
							});
						</script>    
									
						<div class="input-group voffset5">
						    <span class="input-group-btn">
									<button id="fileButtonBrowse3" type="button" class="btn btn-sm btn-default">
									<i class="fa fa-upload"></i> <fmt:message key="label.authoring.basic.resource.file.input"/>
								</button>
							</span>
							<input type="file" id="file3" name="file3" style="display:none"> 
							<input type="text" id="fileInputName3" style="display:none" disabled="disabled" class="form-control input-sm">
						</div>
								
						<script type="text/javascript">
							document.getElementById('fileButtonBrowse3').addEventListener('click', function() {
								document.getElementById('file3').click();
							});
							
							document.getElementById('file3').addEventListener('change', function() {
								$('#fileInputName3').show();
								document.getElementById('fileInputName3').value = this.value.replace(/^.*\\/, "");
								
							});
						</script>    
						
						<div class="input-group voffset5">
						    <span class="input-group-btn">
									<button id="fileButtonBrowse4" type="button" class="btn btn-sm btn-default">
									<i class="fa fa-upload"></i> <fmt:message key="label.authoring.basic.resource.file.input"/>
								</button>
							</span>
							<input type="file" id="file4" name="file4" style="display:none"> 
							<input type="text" id="fileInputName4" style="display:none" disabled="disabled" class="form-control input-sm">
						</div>
								
						<script type="text/javascript">
							document.getElementById('fileButtonBrowse4').addEventListener('click', function() {
								document.getElementById('file4').click();
							});
							
							document.getElementById('file4').addEventListener('change', function() {
								$('#fileInputName4').show();
								document.getElementById('fileInputName4').value = this.value.replace(/^.*\\/, "");
								
							});
						</script>    
						
						<div class="input-group voffset5">
						    <span class="input-group-btn">
									<button id="fileButtonBrowse5" type="button" class="btn btn-sm btn-default">
									<i class="fa fa-upload"></i> <fmt:message key="label.authoring.basic.resource.file.input"/>
								</button>
							</span>
							<input type="file" id="file5" name="file5" style="display:none"> 
							<input type="text" id="fileInputName5" style="display:none" disabled="disabled" class="form-control input-sm">
						</div>
								
						<script type="text/javascript">
							document.getElementById('fileButtonBrowse5').addEventListener('click', function() {
								document.getElementById('file5').click();
							});
							
							document.getElementById('file5').addEventListener('change', function() {
								$('#fileInputName5').show();
								document.getElementById('fileInputName5').value = this.value.replace(/^.*\\/, "");
								
							});
						</script>
				</div>
								
			</html:form>

			<div class="panel-body text-center" style="display:none" id="itemAttachmentArea_Busy">
				<i class="fa fa-refresh fa-spin fa-2x fa-fw text-primary"></i>
			</div>

			<div id="uploadButtons" class="voffset10 pull-right">
				<a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-default loffset5">
					<fmt:message key="label.cancel" /> 
				</a>
				<a href="#nogo" onclick="javascript:submitMultipleImages();" class="btn btn-default button-add-item">
					<fmt:message key="label.authoring.basic.add.images" /> 
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
