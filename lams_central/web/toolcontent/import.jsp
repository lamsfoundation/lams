<!DOCTYPE html>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>

<lams:html>
	<lams:head>
		<title><fmt:message key="title.import" /></title>
		
		<lams:css />
		
		<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
 		<script type="text/javascript" src="/lams/includes/javascript/upload.js"></script>
 	
		<script type="text/javascript">
			function closeWin(){
				window.close();
			}
			
			function verifyAndSubmit() {
				var fileSelect = document.getElementById('UPLOAD_FILE');
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
				document.getElementById('importForm').submit();
			}
			
		</script>
	</lams:head>

<c:set var="title" scope="request">
	<fmt:message key="title.import" />
</c:set>

	<body class="stripes">
			<lams:Page type="admin" title="${title}">

				<div class="panel">
					<fmt:message key="title.import.instruction" />
				</div>

				<form action="<c:url value="/authoring/importToolContent.do"/>" method="post" enctype="multipart/form-data" id="importForm">
					<div>
						<label for="UPLOAD_FILE"><fmt:message key="label.ld.zip.file" /></label>
						<input type="hidden" name="customCSV" id="customCSV" value="${customCSV}" />
					</div>
					
 					<lams:FileUpload fileFieldname="UPLOAD_FILE" fileInputMessageKey="label.file" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
					<lams:WaitingSpinner id="itemAttachment_Busy"/>

					<div class="pull-right voffset10">
 						<a href="javascript:;" class="btn btn-primary" onclick="javascript:verifyAndSubmit();"><fmt:message key="button.import" /></a>
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