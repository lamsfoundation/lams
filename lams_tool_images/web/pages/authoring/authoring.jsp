<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.author.title" /></title>

	<%@ include file="/common/tabbedheader.jsp"%>
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
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
    </script>
 
</lams:head>
<body class="stripes" onLoad="init();">
	<div id="page">
		<h1>
			<fmt:message key="label.authoring.heading" />
		</h1>
		<div id="header">
			<lams:Tabs useKey="true" control="true">
				<lams:Tab id="1" key="label.authoring.heading.basic" />
				<lams:Tab id="2" key="label.authoring.heading.advance" />
			</lams:Tabs>
		</div>
	
		<!-- start tabs -->
		<div id="content">
			<!-- end tab buttons -->
			
			<%@ include file="/common/messages.jsp"%>
	
			<html:form action="authoring/update" method="post" styleId="authoringForm" enctype="multipart/form-data">
				<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
				<html:hidden property="imageGallery.contentId" />
				<html:hidden property="sessionMapID" />
				<html:hidden property="contentFolderID" />
				<html:hidden property="currentTab" styleId="currentTab" />
		
				<lams:help toolSignature="<%= ImageGalleryConstants.TOOL_SIGNATURE %>" module="authoring"/>
		
					<!-- tab content 1 (Basic) -->
					<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
					<!-- end of content (Basic) -->
		
					<!-- tab content 2 (Advanced) -->
					<lams:TabBody id="2" titleKey="label.authoring.heading.advance.desc" page="advance.jsp" />
					<!-- end of content (Advanced) -->
		
					<!-- Button Row -->
					<%--  Default value 
						cancelButtonLabelKey="label.authoring.cancel.button"
						saveButtonLabelKey="label.authoring.save.button"
						cancelConfirmMsgKey="authoring.msg.cancel.save"
						accessMode="author"
					--%>
					<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" 
						toolSignature="<%=ImageGalleryConstants.TOOL_SIGNATURE%>" toolContentID="${formBean.imageGallery.contentId}" 
						 customiseSessionID="${formBean.sessionMapID}"
						 contentFolderID="${formBean.contentFolderID}" />
			</html:form>
	
		</div>
	
		<div id="footer"></div>

	<!-- end page div -->
	</div>

</body>
</lams:html>
