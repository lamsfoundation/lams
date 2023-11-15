<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileUtil" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="tmpFileUploadId"><%=FileUtil.generateTmpFileUploadId()%></c:set>
<c:set var="language"><lams:user property="localeLanguage"/></c:set>

<lams:html>
<lams:head>
	<title>x</title>
	<lams:css/>
	<link href="/lams/css/uppy.min.css" rel="stylesheet" type="text/css" />
	<link href="/lams/css/uppy.custom.css" rel="stylesheet" type="text/css" />

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
	<%@ include file="/common/uppylang.jsp"%>
	<script type="text/javascript">
		function closeWin() {
			window.close();
		}

		
		function submit() {
			var options = { 
				target:  parent.jQuery('#itemArea'), 
				success: function (responseText, statusText) {  // post-submit callback 
		   			self.parent.location.reload();;
		   		}
	    	}; 							
	    	$('#importForm').ajaxSubmit(options);
		}
		/**
		 * Initialise Uppy as the file upload widget
		 */
		var LAMS_URL = '<lams:LAMSURL/>',
	 		UPLOAD_FILE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_MAX_SIZE}"/>';
			
		function initFileUpload(tmpFileUploadId, language) {
			var allowedFileTypes = ['.xml'],
		  	  	uppyProperties = {
					  // upload immediately 
					  autoProceed: true,
					  allowMultipleUploads: true,
					  debug: false,
					  restrictions: {
						// taken from LAMS configuration
					    maxFileSize: +UPLOAD_FILE_MAX_SIZE,
					    maxNumberOfFiles: 1,
					    allowedFileTypes : allowedFileTypes
					  },
					  meta: {
						  // all uploaded files go to this subdir in LAMS tmp dir
						  // its format is: upload_<userId>_<timestamp>
						  'tmpFileUploadId' : tmpFileUploadId,
						  'largeFilesAllowed' : true
					  }
				  };
			  
			  switch(language) {
			  	case 'es' : uppyProperties.locale = Uppy.locales.es_ES; break; 
				case 'fr' : uppyProperties.locale = Uppy.locales.fr_FR; break; 
				case 'el' : uppyProperties.locale = Uppy.locales.el_GR; break; 
				case 'it' : uppyProperties.locale = Uppy.locales.it_IT; break; 
			  }
			  
			  
			  // global variable
			  uppy = Uppy.Core(uppyProperties);
			  // upload using Ajax
			  uppy.use(Uppy.XHRUpload, {
				  endpoint: LAMS_URL + 'tmpFileUpload',
				  fieldName : 'file',
				  // files are uploaded one by one
				  limit : 1
			  });
			  
			  uppy.use(Uppy.DragDrop, {
				  target: '#image-upload-area',
				  inline: true,
				  height: 120,
				  width: '100%'
				});

			  uppy.use(Uppy.Informer, {
				  target: '#image-upload-area'
			  });
			  
			  uppy.use(Uppy.StatusBar, {
				  target: '#image-upload-area',
				  hideAfterFinish: false,
				  hideUploadButton: true,
				  hideRetryButton: true,
				  hidePauseResumeButton: true,
				  hideCancelButton: true
				});
		}
		
		$(document).ready(function(){
			initFileUpload('<c:out value="${tmpFileUploadId}" />', '<lams:user property="localeLanguage"/>');
		});	
	</script>
</lams:head>

<body class="stripes">

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				<fmt:message key="label.import.xml" />
			</div>
		</div>
			
		<div class="panel-body">
			<c:set var="csrfToken"><csrf:token/></c:set>
			<form action='<c:url value="/xmlQuestions/importQuestionsXml.do"/>?${csrfToken}&collectionUid=<c:out value="${param.collectionUid}" />' method="post" 
				id="importForm">	
				<input type="hidden" name="tmpFileUploadId" value='<c:out value="${tmpFileUploadId}" />' /> 
				
			
				<div id="image-upload-area" class="voffset20"></div>
			
				<div class="pull-right voffset10" id="buttonsDiv">
					<input class="btn btn-sm btn-default" value='<fmt:message key="button.cancel"/>' type="button"
						onClick="javascript:self.parent.tb_remove();" />

					<a href="#nogo" class="btn btn-sm btn-primary" onclick="javascript:submit();">
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
