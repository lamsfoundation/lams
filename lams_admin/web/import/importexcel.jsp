<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.admin.service.IImportService"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %> 

<c:set var="lams"><lams:LAMSURL/></c:set>

<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
<c:set var="mustHaveLowercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE)%></c:set>
<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>

<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set> 
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set> 
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set> 


<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/upload.js"></script>
 
<script language="javascript" type="text/JavaScript">
	
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

<p>
	<a href="<lams:LAMSURL/>/admin/sysadminstart.do"
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
		</ul>
	</lams:Alert>
</div>
<p>
	<fmt:message key="msg.import.conclusion" />
</p>

<html:form action="/importexcelsave.do" method="post"
	enctype="multipart/form-data" onsubmit="return goToStatus();">
	<html:hidden property="orgId" />

	<lams:FileUpload fileFieldname="file" fileInputMessageKey="label.excel.spreadsheet" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/> 
	<lams:WaitingSpinner id="fileUpload_Busy"/> 

	<div class="pull-right">
		<html:cancel styleId="cancelButton" styleClass="btn btn-default">
			<fmt:message key="admin.cancel" />
		</html:cancel>
		<html:submit styleId="importButton"
			styleClass="btn btn-primary loffset5">
			<fmt:message key="label.import" />
		</html:submit>
		&nbsp;
	</div>

</html:form>