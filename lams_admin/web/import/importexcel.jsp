<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.admin.service.IImportService"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %> 

<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
<c:set var="mustHaveLowercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE)%></c:set>
<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>

<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set> 
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set> 
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set> 

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.management"/></c:set>
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/upload.js"></script>
	<script type="text/JavaScript">
		
		function goToStatus() {
			if ( "undefined" === typeof bCancel || ! bCancel ) {
				var fileSelect = document.getElementById('file');
				var files = fileSelect.files;
					if (files.length == 0) {
					clearFileError();
					showFileError('<fmt:message key="button.select.importfile"/>');
					return false;
				} else {
					var file = files[0];
					if ( ! validateShowErrorSpreadsheetType(file, '<fmt:message key="error.attachment.not.xls"/>', false)
							 || ! validateShowErrorFileSize(file, '${UPLOAD_FILE_MAX_SIZE}', '<fmt:message key="errors.maxfilesize"/>') ) {
						return false;
					}
				}
				document.getElementById('fileUpload_Busy').style.display = '';
			}
			document.location = '<lams:LAMSURL/>/admin/import/status.jsp';
		}
	 </script>
</lams:head>
    
<body class="stripes">

	<c:set var="title"><fmt:message key="admin.user.management"/></c:set>
	<c:set var="help"><fmt:message key="Import+Users"/></c:set>
	<c:set var="help"><lams:help style="small" page="${help}" /></c:set>

	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbTop"><lams:LAMSURL/>admin/sysadminstart.do | <fmt:message key="sysadmin.maintain" /></c:set>
	<c:set var="breadcrumbActive">. | <fmt:message key="admin.user.management"/></c:set>
	<c:set var="breadcrumbItems" value="${breadcrumbTop}, ${breadcrumbActive}"/>	
	
	<lams:Page type="admin" title="${title}" titleHelpURL="${help}" formID="importExcelForm" breadcrumbItems="${breadcrumbItems}">

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
			<c:set var="alertTitle"><fmt:message key='label.password.must.contain' /></c:set>
			<lams:Alert type="info" close="false" title="${alertTitle}:">
				<ul style="line-height: 1.2em; list-style: none;">
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
				</ul>
			</lams:Alert>
			<p>
				<fmt:message key="msg.import.conclusion" />
			</p>
			
			<form:form action="importexcelsave.do" modelAttribute="importExcelForm" id="importExcelForm" method="post"
				enctype="multipart/form-data" onsubmit="return goToStatus();">
				<form:hidden path="orgId" />
			
				<lams:FileUpload fileFieldname="file" fileInputMessageKey="label.excel.spreadsheet" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/> 
				<lams:WaitingSpinner id="fileUpload_Busy"/> 
			
				<div class="pull-right">
					<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-outline-secondary"><fmt:message key="admin.cancel"/></a>
					<input type="submit" id="importButton" class="btn btn-primary loffset5" value="<fmt:message key="label.import" />" />
					&nbsp;
				</div>
			
			</form:form>
	</lams:Page>
</body>
</lams:html>




