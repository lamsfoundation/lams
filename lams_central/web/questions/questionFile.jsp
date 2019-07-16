<!DOCTYPE html>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 
<%@ taglib uri="tags-core" prefix="c"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="title.lams" /> :: <fmt:message key="label.questions.file.title" /></title>

	<lams:css />
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
	<script type="text/javascript" src="/lams/includes/javascript/upload.js"></script>

	<script type="text/javascript">
		function verifyAndSubmit() {
			var fileSelect = document.getElementById('file');
			var files = fileSelect.files;
				if (files.length == 0) {
				clearFileError();
				showFileError('<fmt:message key="button.select.importfile"/>');
				return false;
			} else {
				var file = files[0];
				if ( ! validateShowErrorNotExecutable(file, '<fmt:message key="error.attachment.executable"/>', false, '${EXE_FILE_TYPES}')
						 || ! validateShowErrorFileSize(file, '${UPLOAD_FILE_MAX_SIZE}', '<fmt:message key="errors.maxfilesize"/>') ) {
					return false;
				}
			}

			document.getElementById('itemAttachment_Busy').style.display = '';
			return true;
		}
	</script>
</lams:head>

<body class="stripes">

	<c:set var="title" scope="request">
		<fmt:message key="label.questions.file.title" />
	</c:set>
	<lams:Page type="admin" title="${title}">
			
		<lams:errors/>				

			<form id="questionForm" action="<lams:LAMSURL/>questions.do" enctype="multipart/form-data" method="post" onsubmit="return verifyAndSubmit();">
				<input type="hidden" name="returnURL" value="${empty param.returnURL ? returnURL : param.returnURL}" /> 
				<input type="hidden" name="callerID" value="${empty param.callerID ? callerID : param.callerID}" /> 
				<input type="hidden" name="limitType" value="${empty param.limitType ? limitType : param.limitType}" /> 
				<lams:FileUpload fileFieldname="file" fileInputMessageKey="label.file" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
				<lams:WaitingSpinner id="itemAttachment_Busy"/>
				<div class="pull-right voffset5" id="buttonsDiv">
					<input class="btn btn-sm btn-default" value='<fmt:message key="button.cancel"/>' type="button"
						onClick="javascript:window.close()" />
					<button class="btn btn-sm btn-primary" value='<fmt:message key="label.upload"/>' type="submit">
						<fmt:message key="button.import" />
					</button>
				</div>
			</form>
			<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>