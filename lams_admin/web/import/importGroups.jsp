<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.admin.service.IImportService" %>
<%@ page import="java.util.List" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.OrganisationType" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set> 
<c:set var="language"><lams:user property="localeLanguage"/></c:set>
<c:set var="classTypeId"><%= OrganisationType.CLASS_TYPE %></c:set>
<c:set var="courseTypeId"><%= OrganisationType.COURSE_TYPE %></c:set>
<c:set var="lams"><lams:LAMSURL/></c:set>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.import.groups.title"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link href="<lams:LAMSURL/>css/uppy.min.css" rel="stylesheet" type="text/css" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
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
			initFileUpload('${importForm.tmpFileUploadId}', '${language}');
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

	<c:set var="help"><fmt:message key="Import+Groups"/></c:set>
	<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
	<lams:Page type="admin" title="${title}" titleHelpURL="${help}" formID="importForm">
	
	<p><a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

	<div id="main-page">
	
	<c:if test="${not empty results}">
		<h4><fmt:message key="heading.import.results"/></h4>
		<table cellspacing="5" cellpadding="5">
			<tr>
				<th width="115" align="right"><fmt:message key="table.heading.organisation.id"/></th>
				<th><fmt:message key="admin.organisation.name"/></th>
			</tr>
			<c:set var="results" value="${results}" />
			<c:forEach var="i" begin="0" step="1" end="${results.size()-1}">
				<tr>
				<c:set var="rowResult" value="${results[i]}"/>
				<c:choose>
					<c:when test="${(rowResult != null) && (rowResult.size() >= 4)}">
						<c:if test="${courseTypeId == rowResult[3]}">
							<th> <c:out value="${rowResult[0]}" /> </th>
							<th> <c:out value="${rowResult[1]}" /> </th>
						</c:if>
						<c:if test="${classTypeId == rowResult[3]}">
							<td> <c:out value="${rowResult[0]}" /> </td>
							<td> <c:out value="${rowResult[1]}" /> </td>
						</c:if>
					</c:when>
					<c:otherwise>
						<td colspan="2">
						<c:forEach var="j" begin="0" step="1" end="${results.size()-1}">
							<c:out value="${rowResult[j]}"/> <br />
						</c:forEach>
						</td>
					</c:otherwise>
				</c:choose>
				</tr>
			</c:forEach>
		</table>
		<hr />
	</c:if>
	
	<p><fmt:message key="import.groups.intro"/></p>
	<p>
	<ul>
		<li>
			<fmt:message key="msg.import.1"/>
		</li>
		<li>
			<fmt:message key="import.groups.instructions"/>
		</li>
		<li>
			<fmt:message key="import.groups.download"/>
			<ul><li><p><a href="file/lams_groups_template.xls">lams_groups_template.xls</a></p></li></ul>
		</li>
	</ul>
	</p>
	<p><fmt:message key="msg.import.conclusion"/></p>
	
	<form:form action="importgroups.do" modelAttribute="importForm" id="importForm" method="post">
		<form:hidden path="orgId" />
		<form:hidden path="tmpFileUploadId" />
				
		<div id="file-upload-area" class="voffset20"></div>
		
		<div class="pull-right voffset20">
		<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default"><fmt:message key="admin.cancel"/></a>
		<input type="submit" id="importButton" class="btn btn-primary loffset5" value="<fmt:message key="label.import"/>" /> &nbsp; 	
		</div>
	
	</form:form>
	
	</div>
	</lams:Page>
		
</body>
</lams:html>




