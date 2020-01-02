<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.assessment.AssessmentConstants"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />

<lams:html>
<lams:head>
	<title><fmt:message key="label.author.title" /></title>
	<%@ include file="/common/tabbedheader.jsp"%>
	<link href="<lams:LAMSURL/>css/thickbox.css" rel="stylesheet" type="text/css" media="screen">
	 
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/thickbox.js"></script>
	<script>
        function init() {
            var tag = document.getElementById("currentTab");
		    	if (tag.value != ""){
		    		selectTab(tag.value);
		    	} else {
	        		selectTab(1); //select the default tab;
		    	}
        }     
        
        function doSelectTab(tabId) {
	        	// start optional tab controller stuff
	        	var tag = document.getElementById("currentTab");
		    	tag.value = tabId;
		    	// end optional tab controller stuff
		    	selectTab(tabId);
        }

        function validateQuestionBankIsNotEmpty() {
            //check with a teacher whether he forgot to add questions to the question bank
            var referenceCount = $("#referencesTable tr").length - 1;
			if ((referenceCount == 0) && !confirm("<fmt:message key="label.no.questions.in.question.bank"/>")) {
				return false;
			}

			//serialize overallFeedbackForm
	        	$("#overallFeedbackList").val($('#advancedInputArea').contents().find('#overallFeedbackForm').serialize(true));
	        	
	        	//enable checkbox to allow its value been submitted
	        	$("#display-summary").removeAttr("disabled", "disabled");

        		return true;
        }
    </script>
</lams:head>
<body class="stripes" onLoad="init()">
	<form:form action="updateContent.do" method="post" modelAttribute="assessmentForm" id="authoringForm" 
			onsubmit="return validateQuestionBankIsNotEmpty();">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
	
		<c:set var="title"><fmt:message key="label.author.title" /></c:set>
		<lams:Page title="${title}" type="navbar">
		
			<form:hidden path="assessment.contentId" />
			<input type="hidden" name="mode" value="${mode}">
			<form:hidden path="sessionMapID" />
			<form:hidden path="contentFolderID" />
			<form:hidden path="currentTab" id="currentTab" />
	
			<lams:Tabs control="true" title="${title}" helpToolSignature="<%= AssessmentConstants.TOOL_SIGNATURE %>" helpModule="authoring">
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
					<lams:AuthoringButton formID="authoringForm"
						clearSessionActionUrl="/clearsession.do" toolSignature="<%=AssessmentConstants.TOOL_SIGNATURE%>"
						toolContentID="${assessmentForm.assessment.contentId}"
						accessMode="${mode}" defineLater="${mode=='teacher'}"
						contentFolderID="${assessmentForm.contentFolderID}" />
				</div>
			</lams:TabBodyArea>
	
			<div id="footer"></div>
	
		<!-- end page div -->
		</lams:Page>
	
	</form:form>
</body>
</lams:html>
