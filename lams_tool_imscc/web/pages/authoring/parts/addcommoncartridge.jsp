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
	<%@ include file="/common/header.jsp"%>
	<script type="text/javascript" src="${lams}includes/javascript/upload.js"></script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/commonCartridgeItem.js'/>"></script>
	
	<script type="text/javascript">
		function verifyAndSubmit() {
			var fileSelect = document.getElementById('file');
			var files = fileSelect.files;
			if (files.length == 0) {
				clearFileError();
				showFileError('<fmt:message key="error.resource.item.file.blank"/>');
				return false;
			} else {
				var file = files[0];
				if ( ! validateShowErrorNotExecutable(file, '<fmt:message key="error.attachment.executable"/>', false, '${EXE_FILE_TYPES}')
						 || ! validateShowErrorFileSize(file, '${UPLOAD_FILE_MAX_SIZE}', '<fmt:message key="errors.maxfilesize"/>') ) {
					return false;
				}
			}
			return true;
		}
	</script>
	
</lams:head>
	
<body class="stripes">
	<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="commonCartridgeItemForm" enctype="multipart/form-data" onsubmit="return verifyAndSubmit()">
		<c:set var="itemAttachment" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	
		<c:set var="title"><fmt:message key="label.authoring.basic.upload.common.cartridge" /></c:set>
		<lams:Page title="${title}" type="learner">

			<%@ include file="/common/messages.jsp"%>
			<html:hidden property="sessionMapID" />
			<input type="hidden" name="itemType" id="itemType" value="2" />
			<html:hidden property="itemIndex" />

			<div class="form-group">
				<div id="itemAttachmentArea">
					<%@ include file="/pages/authoring/parts/itemattachment.jsp"%>
				</div>
			</div>		
		
			<div class="voffset10">
				<a href="#nogo" onclick="javascript:self.parent.tb_remove();" onmousedown="self.focus();" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.cancel" /> 
				</a>
				<a href="#nogo" onclick="javascript:submitCommonCartridgeItem();" onmousedown="self.focus();" class="btn btn-sm btn-primary">
					<i class="fa fa-upload"></i> <fmt:message key="button.upload" />
				</a>
			</div>
			
		</lams:Page>

	</html:form>
</body>
</lams:html>
