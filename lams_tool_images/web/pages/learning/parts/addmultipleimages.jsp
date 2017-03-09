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
			
			function submitMultipleImages(){
				
				var UPLOAD_FILE_MAX_SIZE = ${UPLOAD_FILE_MAX_SIZE};
				var LABEL_ITEM_BLANK = '<fmt:message key="error.resource.item.file.blank"/>';
				var LABEL_MAX_FILE_SIZE = '<fmt:message key="errors.maxfilesize"><param>{0}</param></fmt:message>';
				var LABEL_NOT_ALLOWED_FORMAT = '<fmt:message key="error.resource.image.not.alowed.format"/>';	
				
				var formData = new FormData();
				formData.append('sessionMapID', $("#sessionMapID").val());
				
				// Get selected files from the inputs
				var validateFiles = [];
				var errorDivs = [];
				if ($('#file1').val()) { 
					var file = document.getElementById('file1').files[0];
					validateFiles.push(file);
					errorDivs.push('fileerror1');
					formData.append('file1', file, file.name);
				}
				if ($('#file2').val()) { 
					var file = document.getElementById('file2').files[0];
					validateFiles.push(file);
					errorDivs.push('fileerror2');
					formData.append('file2', file, file.name);
				}
				if ($('#file3').val()) { 
					var file = document.getElementById('file3').files[0];
					validateFiles.push(file);
					errorDivs.push('fileerror3');
					formData.append('file3', file, file.name);
				}
				if ($('#file4').val()) {
					var file = document.getElementById('file4').files[0];
					validateFiles.push(file);
					errorDivs.push('fileerror4');
					formData.append('file4', file, file.name);
				}
				if ($('#file5').val()) { 
					var file = document.getElementById('file5').files[0];
					validateFiles.push(file);
					errorDivs.push('fileerror5');
					formData.append('file5', file, file.name);
				}
					
				// validate files 
				if (validateFiles.length == 0) {
					alert(LABEL_ITEM_BLANK);
					return;
				}
				var valid = true;
				for (var i = 0; i < validateFiles.length; i++) {
				    // Check the file type and file size
					var file = validateFiles[i];
					var errorDivId = errorDivs[i];
					if ( ! validateShowErrorImageType(file, LABEL_NOT_ALLOWED_FORMAT, false, errorDivId ) || 
							! validateShowErrorFileSize(file, UPLOAD_FILE_MAX_SIZE, LABEL_MAX_FILE_SIZE, false, errorDivId) ) {
						valid=false;
					}
				}
				if ( ! valid ) {
					return;
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
				<fmt:message key="label.authoring.basic.add.multiple.images" />
				<a href="<html:rewrite page='/learning/newImageInit.do'/>?sessionMapID=${sessionMapID}&KeepThis=true&TB_iframe=true&modal=true" 
						class="thickbox btn btn-default pull-right btn-xs">  
					<fmt:message key="label.authoring.basic.upload.single.image" />
				</a>				
			</div>
		</div>
			
		<div class="panel-body">
		
			<%@ include file="/common/messages.jsp"%>
			
			<html:form action="/learning/saveMultipleImages" method="post" styleId="multipleImagesForm" enctype="multipart/form-data">
				<html:hidden property="sessionMapID" styleId="sessionMapID"/>
	
				<div class="form-group">
					<label>
						<fmt:message key="label.authoring.basic.resource.files.input" />
					</label>

					<div class="help-block">
						<fmt:message key="label.upload.info">
						<fmt:param>${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}</fmt:param>
						</fmt:message>
					</div>
					
					<lams:FileUpload fileButtonBrowse="fileButtonBrowse1" fileFieldname="file1" errorMsgDiv="fileerror1" uploadInfoMessageKey="-"
						fileInputNameFieldname="fileInputName1" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
		
					<div class="voffset5"></div>
					<lams:FileUpload fileButtonBrowse="fileButtonBrowse2" fileFieldname="file2" errorMsgDiv="fileerror2" uploadInfoMessageKey="-"
						fileInputNameFieldname="fileInputName2" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
	
					<div class="voffset5"></div>
					<lams:FileUpload fileButtonBrowse="fileButtonBrowse3" fileFieldname="file3" errorMsgDiv="fileerror3" uploadInfoMessageKey="-"
						fileInputNameFieldname="fileInputName3" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
	
					<div class="voffset5"></div>
					<lams:FileUpload fileButtonBrowse="fileButtonBrowse4" fileFieldname="file4" errorMsgDiv="fileerror4" uploadInfoMessageKey="-"
						fileInputNameFieldname="fileInputName4" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
	
					<div class="voffset5"></div>
					<lams:FileUpload fileButtonBrowse="fileButtonBrowse5" fileFieldname="file5" errorMsgDiv="fileerror5" uploadInfoMessageKey="-"
						fileInputNameFieldname="fileInputName5" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
				</div>
				
			</html:form>
			
			<div class="panel-body text-center" style="display:none" id="itemAttachmentArea_Busy">
				<i class="fa fa-refresh fa-spin fa-2x fa-fw text-primary"></i>
			</div>			
	
			<div id="uploadButtons">
				<div class="voffset10 pull-right">
					<a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-default loffset5">
						<fmt:message key="label.cancel" /> 
					</a>
					<a href="#nogo" onclick="javascript:submitMultipleImages();" class="btn btn-default button-add-item">
						<fmt:message key="label.authoring.basic.add.images" /> 
					</a>
				</div>
			</div>
		</div>	
	
		<div id="footer"></div>
		<!--closes footer-->
	</div>		
</body>
</lams:html>
