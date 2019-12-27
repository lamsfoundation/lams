<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.pixlr.util.PixlrConstants"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="UPLOAD_FILE_LARGE_MAX_SIZE" scope="request"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING" scope="request"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set>

<lams:html>

	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:headItems />
		<c:set var="lams">
			<lams:LAMSURL />
		</c:set>
		<c:set var="tool">
			<lams:WebAppURL />
		</c:set>
		<script type="text/javascript" src="${tool}includes/javascript/authoring.js"></script>
		<script type="text/javascript" src="${tool}includes/javascript/common.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/upload.js"></script>
	</lams:head>

	<body class="stripes" onload="init();">
	
		
		<lams:Page title='<fmt:message key="pageTitle.authoring"/>' type="navbar">
				
			<script type="text/javascript">
				function validate() {
					//in case image is already uploaded - skip validation
					if ("${imageExists}" == "true") {
						return true;
					}
					
					// Get the selected files from the input.
					var files = document.getElementById('file').files;
					if (files.length == 0) {
						clearFileError();
						showFileError('<fmt:message key="error.resource.item.file.blank"/>');
						return false;
					}
					var file = files[0];
					if ( validateShowErrorImageType(file, '<fmt:message key="error.resource.image.not.alowed.format"/>', false) 
								&& validateShowErrorFileSize(file, ${UPLOAD_FILE_LARGE_MAX_SIZE}, '<fmt:message key="errors.maxfilesize"><param>{0}</param></fmt:message>', false) ) {
						document.getElementById("imageAttachmentDiv_Busy").style.display = '';
						return true;
					} else {
						return false;
					}		
				}
			</script>	
		
			<form:form action="/lams/tool/lapixl10/authoring/updateContent.do" modelAttribute="authoringForm" id="authoringForm" method="post" enctype="multipart/form-data"  onsubmit="return validate();">
				<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />
				
				<form:hidden path="currentTab" id="currentTab" />
				<form:hidden path="sessionMapID" />
				<form:hidden path="toolContentID" />
				<form:hidden path="mode" value=""/>
				<form:hidden path="contentFolderID" />
				<form:hidden path="existingImageFileName" />
				
				<c:set var="title"><fmt:message key="activity.title" /></c:set>
				<lams:Tabs control="true" title="${title}" helpToolSignature="<%= PixlrConstants.TOOL_SIGNATURE %>" helpModule="authoring">
					<lams:Tab id="1" key="button.basic" />
					<lams:Tab id="2" key="button.advanced" />
				</lams:Tabs>
			
				<lams:TabBodyArea>
					<lams:errors/>
					 		
					<!--  Set up tabs  -->
					<lams:TabBodys>
			   			<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
			 			<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
			  		</lams:TabBodys>
					
					<!-- Button Row -->
					<div id="saveCancelButtons">
						<lams:AuthoringButton formID="authoringForm"
							clearSessionActionUrl="/clearsession.do" toolSignature="<%=PixlrConstants.TOOL_SIGNATURE%>"
							cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save"
							toolContentID="${sessionMap.toolContentID}"
							accessMode="${sessionMap.mode}" defineLater="${sessionMap.mode == 'teacher'}"
							contentFolderID="${sessionMap.contentFolderID}" />
					</div>
				</lams:TabBodyArea>
			
			</form:form>
		
			<div id="footer"></div>

		</lams:Page>

	</body>
</lams:html>

<%@ include file="/common/taglibs.jsp"%>

