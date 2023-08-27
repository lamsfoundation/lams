<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="ANTIVIRUS_ENABLE"><%=Configuration.get(ConfigurationKeys.ANTIVIRUS_ENABLE)%></c:set>
<c:set var="language"><lams:user property="localeLanguage"/></c:set>

<lams:html>
	<lams:head>
		<title><fmt:message key="title.import" /></title>
		
		<lams:css />
		<link href="/lams/css/uppy.min.css" rel="stylesheet" type="text/css" />
		<link href="/lams/css/uppy.custom.css" rel="stylesheet" type="text/css" />
		
		<%@ include file="/common/uppylang.jsp"%>
		
		<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="/lams/includes/javascript/bootstrap.min.js"></script>
 	
		<script type="text/javascript">
			var LAMS_URL = '<lams:LAMSURL/>',
		 		UPLOAD_FILE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_MAX_SIZE}"/>';
				
		       
			function closeWin(){
				window.close();
			}
			
			/**
			 * Initialised Uppy as the file upload widget
			 */
			function initFileUpload(tmpFileUploadId, language) {
				  var allowedFileTypes = ['.zip'],
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
							  'largeFilesAllowed' : false
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
					  height: 150,
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
				initFileUpload('${tmpFileUploadId}', '<lams:user property="localeLanguage"/>');
			});		
		</script>
	</lams:head>

<c:set var="title" scope="request">
	<fmt:message key="title.import" />
</c:set>

	<body class="stripes">
			<lams:Page type="admin" title="${title}">

				<div class="panel">
					<fmt:message key="title.import.instruction" />
					<c:if test="${ANTIVIRUS_ENABLE == 'true'}">
						<br />
						<fmt:message key="title.import.instruction.antivirus" />
					</c:if>
				</div>

				<form action="<c:url value="/authoring/importToolContent.do"/>" method="post" enctype="multipart/form-data" id="importForm">
					<div>
						<label for="image-upload-area"><fmt:message key="label.ld.zip.file" /></label>
						<input type="hidden" name="customCSV" id="customCSV" value="${customCSV}" />
					</div>
					
					<input type="hidden" id="tmpFileUploadId" name="tmpFileUploadId"
						   value="${tmpFileUploadId}" />
					<div id="image-upload-area" class="voffset20"></div>
		
					<div class="pull-right voffset10">
 						<button id="importButton" class="btn btn-primary" type="submit">
 							<fmt:message key="button.import" />
 						</button>
 					</div>
				</form>

			</div>
			<!--closes content-->


			<div id="footer">
			</div>
			<!--closes footer-->
</lams:Page>			
	</body>
</lams:html>