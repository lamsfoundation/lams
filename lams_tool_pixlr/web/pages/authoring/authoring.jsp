<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.pixlr.util.PixlrConstants"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="UPLOAD_FILE_LARGE_MAX_SIZE" scope="request"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING" scope="request"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set>

<script type="text/javascript">
	function validate() {
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

<html:form action="/authoring" styleId="authoringForm" method="post" enctype="multipart/form-data" onsubmit="return validate();">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
	<c:set var="defineLater" value="no" />
	<c:if test="${sessionMap.mode == 'teacher'}">
		<c:set var="defineLater" value="yes" />
	</c:if>
	
	<html:hidden property="currentTab" styleId="currentTab" />
	<html:hidden property="dispatch"  styleId="dispatch" value="updateContent" />
	<html:hidden property="sessionMapID" />
	<html:hidden property="toolContentID" />
	<html:hidden property="mode" value=""/>
	<html:hidden property="contentFolderID" />
	<html:hidden property="existingImageFileName" />
	
	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= PixlrConstants.TOOL_SIGNATURE %>" helpModule="authoring">
		<lams:Tab id="1" key="button.basic" />
		<c:if test="${sessionMap.mode == 'author'}">
			<lams:Tab id="2" key="button.advanced" />
		</c:if>
	</lams:Tabs>

	<lams:TabBodyArea>
		<%@ include file="/common/messages.jsp"%>
		 		
		<!--  Set up tabs  -->
		<lams:TabBodys>
   			<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
   			<c:if test="${sessionMap.mode == 'author'}">
 				<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
 			</c:if>
  		</lams:TabBodys>
		
		<!-- Button Row -->
		<div id="saveCancelButtons">
			<lams:AuthoringButton formID="authoringForm"
				clearSessionActionUrl="/clearsession.do" toolSignature="<%=PixlrConstants.TOOL_SIGNATURE%>"
				cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save"
				toolContentID="${sessionMap.toolContentID}"
				accessMode="${sessionMap.mode}" defineLater="${defineLater}"
				contentFolderID="${sessionMap.contentFolderID}" />
		</div>
	</lams:TabBodyArea>

</html:form>

<div id="footer"></div>

