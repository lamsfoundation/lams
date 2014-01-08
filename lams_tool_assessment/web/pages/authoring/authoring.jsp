<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.assessment.AssessmentConstants"%>


<lams:html>
<lams:head>
	<title><fmt:message key="label.author.title" /></title>
	<%@ include file="/common/tabbedheader.jsp"%>
	<link href="<lams:LAMSURL/>css/thickbox.css" rel="stylesheet" type="text/css" media="screen">
	 
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/thickbox.js"></script>
	
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

        function doUploadOnline() {
        	var myForm = $("#authoringForm");
        	myForm.attr("action", "<c:url value='/authoring/uploadOnlineFile.do'/>");
        	myForm.submit();
        }
        
        function doUploadOffline() {
        	var myForm = $("#authoringForm");
        	myForm.attr("action", "<c:url value='/authoring/uploadOfflineFile.do'/>");
        	myForm.submit();
        }
        
        function serializeOverallFeedbackForm() {
        	$("#overallFeedbackList").val($('#advancedInputArea').contents().find('#overallFeedbackForm').serialize(true));
        	
        	//enable checkbox to allow its value been submitted
        	$("#display-summary").removeAttr("disabled", "disabled");
        	return true;
        }
    </script>
</lams:head>
<body class="stripes" onLoad="init()">
<div id="page">
		<h1>
			<fmt:message key="label.authoring.heading" />
		</h1>
<div id="header">
		<lams:Tabs useKey="true" control="true">
			<lams:Tab id="1" key="label.authoring.heading.basic" />
			<lams:Tab id="2" key="label.authoring.heading.advance" />
			<lams:Tab id="3" key="label.authoring.heading.instructions" />
		</lams:Tabs>
</div>
	<!-- start tabs -->
<div id="content">
	<!-- end tab buttons -->
		
	<%@ include file="/common/messages.jsp"%>

	<html:form action="authoring/update" method="post" styleId="authoringForm" enctype="multipart/form-data" onsubmit="return serializeOverallFeedbackForm();">
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<html:hidden property="assessment.contentId" />
		<html:hidden property="sessionMapID" />
		<html:hidden property="contentFolderID" />
		<html:hidden property="currentTab" styleId="currentTab" />

		<lams:help toolSignature="<%= AssessmentConstants.TOOL_SIGNATURE %>" module="authoring"/>

			<!-- tab content 1 (Basic) -->
			<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
			<!-- end of content (Basic) -->

			<!-- tab content 2 (Advanced) -->
			<lams:TabBody id="2" titleKey="label.authoring.heading.advance.desc" page="advance.jsp" />
			<!-- end of content (Advanced) -->

			<!-- tab content 3 (Instructions) -->
			<lams:TabBody id="3" titleKey="label.authoring.heading.instructions.desc" page="instructions.jsp" />
			<!-- end of content (Instructions) -->


			<!-- Button Row -->
			<%--  Default value 
				cancelButtonLabelKey="label.authoring.cancel.button"
				saveButtonLabelKey="label.authoring.save.button"
				cancelConfirmMsgKey="authoring.msg.cancel.save"
				accessMode="author"
			--%>
			<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" 
				toolSignature="<%=AssessmentConstants.TOOL_SIGNATURE%>" toolContentID="${formBean.assessment.contentId}" 
				 customiseSessionID="${formBean.sessionMapID}" contentFolderID="${formBean.contentFolderID}" />
	</html:form>

</div>

<div id="footer"></div>
<!-- end page div -->
</div>

</body>
</lams:html>
