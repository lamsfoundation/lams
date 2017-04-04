<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.assessment.AssessmentConstants"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.author.title" /></title>
	<%@ include file="/common/tabbedheader.jsp"%>
	<link href="<lams:LAMSURL/>css/thickbox.css" rel="stylesheet" type="text/css" media="screen">
	 
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/thickbox.js"></script>
	<script>
        function init() {
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
        
        function serializeOverallFeedbackForm() {
        	$("#overallFeedbackList").val($('#advancedInputArea').contents().find('#overallFeedbackForm').serialize(true));
        	
        	//enable checkbox to allow its value been submitted
        	$("#display-summary").removeAttr("disabled", "disabled");
        	return true;
        }
    </script>
</lams:head>
<body class="stripes" onLoad="init()">
	<html:form action="authoring/update" method="post" styleId="authoringForm" enctype="multipart/form-data"
			onsubmit="return serializeOverallFeedbackForm();">
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	
		<c:set var="title"><fmt:message key="label.author.title" /></c:set>
		<lams:Page title="${title}" type="navbar">
		
			<html:hidden property="assessment.contentId" />
			<html:hidden property="mode" value="${mode}"/>
			<html:hidden property="sessionMapID" />
			<html:hidden property="contentFolderID" />
			<html:hidden property="currentTab" styleId="currentTab" />
	
			<lams:Tabs control="true" title="${title}" helpToolSignature="<%= AssessmentConstants.TOOL_SIGNATURE %>" helpModule="authoring">
				<lams:Tab id="1" key="label.authoring.heading.basic" />
				<lams:Tab id="2" key="label.authoring.heading.advance" />
			</lams:Tabs>	
		
		 	<lams:TabBodyArea>
		 		<%@ include file="/common/messages.jsp"%>
		 		
				<!--  Set up tabs  -->
		 		<lams:TabBodys>
   					<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
 					<lams:TabBody id="2" titleKey="label.authoring.heading.advance.desc" page="advance.jsp" />
  				</lams:TabBodys>
		
				<!-- Button Row -->
				<div id="saveCancelButtons">
					<lams:AuthoringButton formID="authoringForm"
						clearSessionActionUrl="/clearsession.do" toolSignature="<%=AssessmentConstants.TOOL_SIGNATURE%>"
						toolContentID="${formBean.assessment.contentId}"
						accessMode="${mode}" defineLater="${mode=='teacher'}"
						contentFolderID="${formBean.contentFolderID}" />
				</div>
			</lams:TabBodyArea>
	
			<div id="footer"></div>
	
		<!-- end page div -->
		</lams:Page>
	
	</html:form>
</body>
</lams:html>
