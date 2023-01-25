<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.admin.service.IImportService"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ page import="org.lamsfoundation.lams.util.FileUtil" %> 

<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
<c:set var="mustHaveLowercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE)%></c:set>
<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>

<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set> 
<c:set var="language"><lams:user property="localeLanguage"/></c:set>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.management"/></c:set>
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	<link href="<lams:LAMSURL/>css/uppy.min.css" rel="stylesheet" type="text/css" />
	<link href="<lams:LAMSURL/>css/uppy.custom.css" rel="stylesheet" type="text/css" />
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/uppy/uppy.min.js"></script>
	<c:choose>
		<c:when test="${language eq 'es'}">
			<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/uppy/es_ES.min.js"></script>
		</c:when>
		<c:when test="${language eq 'fr'}">
			<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/uppy/fr_FR.min.js"></script>
		</c:when>
		<c:when test="${language eq 'el'}">
			<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/uppy/el_GR.min.js"></script>
		</c:when>
	</c:choose>
	<script type="text/JavaScript">
		var LAMS_URL = '<lams:LAMSURL/>',
			UPLOAD_FILE_MAX_SIZE = '<c:out value="${UPLOAD_FILE_MAX_SIZE}"/>';
	
			$(document).ready(function() {
				initFileUpload('<c:out value="${importExcelForm.tmpFileUploadId}" />', '${language}');
			});
			
			/**
			 * Initialise Uppy as the file upload widget
			 */
			function initFileUpload(tmpFileUploadId, language) {
				var allowedFileTypes = ['.xls'],
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
					  target: '#file-upload-area',
					  inline: true,
					  height: 150,
					  width: '100%'
					});

				  uppy.use(Uppy.Informer, {
					  target: '#file-upload-area'
				  });
				  
				  uppy.use(Uppy.StatusBar, {
					  target: '#file-upload-area',
					  hideAfterFinish: false,
					  hideUploadButton: true,
					  hideRetryButton: true,
					  hidePauseResumeButton: true,
					  hideCancelButton: true
					});
			}
			

	</script>
</lams:head>
    
<body class="stripes">

	<c:set var="title"><fmt:message key="admin.user.management"/></c:set>
	<c:set var="help"><fmt:message key="Import+Users"/></c:set>
	<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
			<lams:Page type="admin" title="${title}" titleHelpURL="${help}" formID="importExcelForm">
				<p>
					<a href="<lams:LAMSURL/>admin/sysadminstart.do"
						class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a>
				</p>
				
				<p>
					<fmt:message key="msg.import.intro" />
				</p>
				<p>
				<ul>
					<li><fmt:message key="msg.import.1" /></li>
					<li><fmt:message key="msg.import.2" />
						<ul>
							<li><p>
									<a href="file/lams_users_template.xls">lams_users_template.xls</a>
								</p></li>
						</ul></li>
					<li><fmt:message key="msg.import.3" />
						<ul>
							<li><p>
									<a href="file/lams_roles_template.xls">lams_roles_template.xls</a>
								</p></li>
						</ul></li>
				</ul>
				</p>
				<div class ="pull-left">
					<lams:Alert type="info" close="false">
						<strong><fmt:message key='label.password.must.contain' />:</strong>
						<ul style="line-height: 1.2">
							<li><span class="fa fa-check"></span> <fmt:message
									key='label.password.min.length'>
									<fmt:param value='${minNumChars}' />
								</fmt:message></li>
							<c:if test="${mustHaveUppercase}">
								<li><span class="fa fa-check"></span> <fmt:message
										key='label.password.must.ucase' /></li>
							</c:if>
							<c:if test="${mustHaveLowercase}">
								<li><span class="fa fa-check"></span> <fmt:message
										key='label.password.must.lcase' /></li>
							</c:if>
							<c:if test="${mustHaveNumerics}">
								<li><span class="fa fa-check"></span> <fmt:message
										key='label.password.must.number' /></li>
							</c:if>
							<c:if test="${mustHaveSymbols}">
								<li><span class="fa fa-check"></span> <fmt:message
										key='label.password.must.symbol' /></li>
							</c:if>
							<li><span class="fa fa-check"></span>
								<fmt:message key='label.password.user.details' />
							</li>
							<li><span class="fa fa-check"></span>
								<fmt:message key='label.password.common' />
							</li>
						</ul>
					</lams:Alert>
				</div>
				<p>
					<fmt:message key="msg.import.conclusion" />
				</p>
				
				<form:form action="importexcelsave.do" modelAttribute="importExcelForm" id="importExcelForm" method="post">
					<form:hidden path="orgId" />
					<form:hidden path="tmpFileUploadId" />
					
					<div class="checkbox">
						<label>
							<form:checkbox path="sendEmail" /> <fmt:message key='label.import.email' />
						</label>
					</div>
				
					<div id="file-upload-area" class="voffset20"></div>
				
					<div class="pull-right voffset20">
						<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default"><fmt:message key="admin.cancel"/></a>
						<input type="submit" id="importButton" class="btn btn-primary loffset5" value="<fmt:message key="label.import" />" />
						&nbsp;
					</div>
				
				</form:form>
			</lams:Page>
</body>
</lams:html>




