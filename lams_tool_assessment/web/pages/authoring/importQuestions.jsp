<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="authoring.title.import" /></title>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/upload.js"></script>
	<script type="text/javascript">
		function closeWin() {
			window.close();
		}

		function isXML(filename) {
			var extname = filename.substr((~-filename.lastIndexOf(".") >>> 0) + 2);
			if ( extname.length > 0 ) {
				extname = extname.toUpperCase();
				extname = "." + extname;
				return '.XML' === extname;
			}
			return false;
		}
		
		function verifyAndSubmit() {
			var fileSelect = document.getElementById('UPLOAD_FILE');
			var files = fileSelect.files;
			if (files.length == 0) {
				clearFileError();
				showFileError('<fmt:message key="button.select.importfile"/>');
				return;
			} else {
				var file = files[0];
				clearFileError();
				if  ( ! isXML(file.name) ) {
					showFileError('<fmt:message key="error.import.file.format"/>');
					return;
				} 
				if ( ! validateShowErrorFileSize(file, '${UPLOAD_FILE_MAX_SIZE}', '<fmt:message key="errors.maxfilesize"/>') ) {
					return;
				}
			}

			document.getElementById('itemAttachment_Busy').style.display = '';
			var options = { 
		    	   	target:  parent.jQuery('#itemArea'), 
		    	   	success: afterRatingSubmit  // post-submit callback
	    	}; 							
	    	$('#importForm').ajaxSubmit(options);

		}

		// post-submit callback 
   		function afterRatingSubmit(responseText, statusText) { 
   			self.parent.refreshThickbox();
   			self.parent.tb_remove();
   		}  
			
	</script>
</lams:head>

<body class="stripes">

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				<fmt:message key="authoring.title.import" />
			</div>
		</div>
			
		<div class="panel-body">
				
			<form action="<c:url value="/authoring/importQuestions.do"/>?sessionMapID=${sessionMapID}" method="post" enctype="multipart/form-data" id="importForm">	
				<lams:FileUpload fileFieldname="UPLOAD_FILE" fileInputMessageKey="label.file" 
					uploadInfoMessageKey="msg.import.file.format" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
				<lams:WaitingSpinner id="itemAttachment_Busy"/>
			
				<div class="pull-right voffset10" id="buttonsDiv">
					<input class="btn btn-sm btn-default" value='<fmt:message key="label.authoring.cancel.button"/>' type="button"
						onClick="javascript:self.parent.tb_remove();" />
					<a href="#nogo" class="btn btn-sm btn-primary" onclick="javascript:verifyAndSubmit();">
						<i class="fa fa-sm fa-upload"></i>&nbsp;<fmt:message key="button.import" />
					</a>
				</div>
			</form>
			
		</div>
		<!--closes content-->

		<div id="footer">
		</div>
		<!--closes footer-->
	</body>
</lams:html>
