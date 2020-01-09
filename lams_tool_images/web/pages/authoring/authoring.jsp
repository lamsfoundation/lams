<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %>
<c:set var="UPLOAD_FILE_LARGE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="label.author.title" /></title>

	<%@ include file="/common/tabbedheader.jsp"%>
	<link href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet" type="text/css" />

	<script type="text/javascript">
		<%-- used for  imageGalleryitem.js --%>
		var removeItemAttachmentUrl = "<c:url value='/authoring/removeImageFile.do'/>";
		var saveMultipleImagesUrl = "<c:url value='/authoring/saveMultipleImages.do'/>";
		var UPLOAD_FILE_LARGE_MAX_SIZE = "${UPLOAD_FILE_LARGE_MAX_SIZE}";
		var LABEL_ITEM_BLANK = '<fmt:message key="error.resource.item.file.blank"/>';
		var LABEL_MAX_FILE_SIZE = '<fmt:message key="errors.maxfilesize"/>';
		var LABEL_NOT_ALLOWED_FORMAT = '<fmt:message key="error.resource.image.not.alowed.format"/>';
	</script>
	<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/imageGalleryitem.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/upload.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script>
        function init(){
            var tag = document.getElementById("currentTab");
	    	if(tag.value != "")
	    		selectTab(tag.value);
            else
                selectTab(1); //select the default tab;
        }
        
        function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	// end optional tab controller stuff
	    	selectTab(tabId);
        }
        
        //callback method that is invoked after image has been successfully uploaded to the server
        function imageUploadedCallback(data) {
        	$('#new-image-input-area').html(data);
        }
    </script>
 
</lams:head>

<body class="stripes" onLoad="init();">
	<form:form action="update.do" method="post" modelAttribute="imageGalleryForm" id="imageGalleryForm">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<c:set var="title"><fmt:message key="label.learning.heading" /></c:set>
		<lams:Page title="${title}" type="navbar" formID="imageGalleryForm">
			<form:hidden path="imageGallery.contentId"/>
			<form:hidden path="sessionMapID" />
			<form:hidden path="contentFolderID" />
			<form:hidden path="currentTab" id="currentTab" />
	
			<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ImageGalleryConstants.TOOL_SIGNATURE %>" helpModule="authoring">
				<lams:Tab id="1" key="label.authoring.heading.basic" />
				<lams:Tab id="2" key="label.authoring.heading.advance" />
			</lams:Tabs>	
		
		 	<lams:TabBodyArea>
		 		<lams:errors/>
		 		
				<!--  Set up tabs  -->
		 		<lams:TabBodys>
   					<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
 					<lams:TabBody id="2" titleKey="label.authoring.heading.advance.desc" page="advance.jsp" />
  				</lams:TabBodys>
		
				<!-- Button Row -->
				<div id="saveCancelButtons">
					<lams:AuthoringButton formID="imageGalleryForm"
						clearSessionActionUrl="/clearsession.do" toolSignature="<%=ImageGalleryConstants.TOOL_SIGNATURE%>"
						toolContentID="${imageGalleryForm.imageGallery.contentId}" accessMode="${mode}" defineLater="${mode=='teacher'}"
						contentFolderID="${imageGalleryForm.contentFolderID}" />
				</div>
			</lams:TabBodyArea>
	
			<div id="footer"></div>
	
		<!-- end page div -->
		</lams:Page>
	
	</form:form>
</body>
</lams:html>
