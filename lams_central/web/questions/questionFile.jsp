<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileUtil" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="tmpFileUploadId"><%=FileUtil.generateTmpFileUploadId()%></c:set>
<c:set var="title" scope="request">
	<c:choose>
		<c:when test="${param.importType == 'word'}">
			<fmt:message key="label.import.word" />
		</c:when>
		<c:otherwise>
			<fmt:message key="label.questions.file.title" />	
		</c:otherwise>
	</c:choose>
</c:set>

<lams:html>
<lams:head>
    <title><c:out value="${title}"/></title>

	<lams:css />
	<link href="/lams/css/uppy.min.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
		.button {
			float: right;
			margin-left: 10px;
		}
		
		div#errorArea {
			display: none;
		}
	</style>

	<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
	
	<script type="text/javascript" src="/lams/includes/javascript/uppy/uppy.min.js"></script>
	<c:choose>
		<c:when test="${language eq 'es'}">
			<script type="text/javascript" src="/lams/includes/javascript/uppy/es_ES.min.js"></script>
		</c:when>
		<c:when test="${language eq 'fr'}">
			<script type="text/javascript" src="/lams/includes/javascript/uppy/fr_FR.min.js"></script>
		</c:when>
		<c:when test="${language eq 'el'}">
			<script type="text/javascript" src="/lams/includes/javascript/uppy/el_GR.min.js"></script>
		</c:when>
	</c:choose>
		

	<script type="text/javascript">
		/**
		 * Initialise Uppy as the file upload widget
		 */
		var LAMS_URL = '<lams:LAMSURL/>',
	 		UPLOAD_FILE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_MAX_SIZE}"/>';
			
		function initFileUpload(tmpFileUploadId, language) {
			var allowedFileTypes = ['.zip', '.xml', '.docx'],
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
				  },
				  onBeforeFileAdded: function(currentFile, files) {
					  var name = currentFile.data.name,
					  	  extensionIndex = name.lastIndexOf('.'),
					  	  valid = allowedFileTypes.includes(name.substring(extensionIndex).trim());
					  if (!valid) {
						  uppy.info('<fmt:message key="label.questions.file.missing" />', 'error', 10000);
					  }
					  
					  return valid;
			    }
			  };
			  
			  switch(language) {
			  	case 'es' : uppyProperties.locale = Uppy.locales.es_ES; break; 
				case 'fr' : uppyProperties.locale = Uppy.locales.fr_FR; break; 
				case 'el' : uppyProperties.locale = Uppy.locales.el_GR; break; 
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
			initFileUpload('${tmpFileUploadId}', '<lams:user property="localeLanguage"/>');
		});	
	</script>
</lams:head>

<body class="stripes">
	<lams:Page type="admin" title="${title}">
		<lams:errors/>				

		<c:if test="${param.importType == 'word'}">
		<div>
			<p><fmt:message key="label.choose.word.document"/></p>
		</div>
		</c:if>
		<form id="questionForm" action="<lams:LAMSURL/>questions.do" enctype="multipart/form-data" method="post">
			<input type="hidden" name="tmpFileUploadId" value="${tmpFileUploadId}" /> 
			<input type="hidden" name="returnURL" value="${empty param.returnURL ? returnURL : param.returnURL}" /> 
			<input type="hidden" name="importType" value="${empty param.importType ? importType : param.importType}" /> 
			<input type="hidden" name="callerID" value="${empty param.callerID ? callerID : param.callerID}" /> 
			<input type="hidden" name="limitType" value="${empty param.limitType ? limitType : param.limitType}" /> 
			<input type="hidden" name="collectionChoice" value="${empty param.collectionChoice ? collectionChoice : param.collectionChoice}" /> 
			
			<div id="image-upload-area" class="voffset20"></div>

        <c:if test="${param.importType == 'word'}">
        <div class="voffset5">
			<a href="/lams/www/public/MSWord-question-import.docx"><fmt:message key="label.download.word.template"/></a>.
        </div>
        </c:if>
			
			<div class="pull-right voffset20" id="buttonsDiv">
				<input class="btn btn-sm btn-default" value='<fmt:message key="button.cancel"/>' type="button"
					onClick="javascript:window.close()" />
				<button id="importButton" class="btn btn-sm btn-primary" value='<fmt:message key="label.upload"/>' type="submit">
					<fmt:message key="button.import" />
				</button>
			</div>
		</form>
		<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>
