<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.survey.SurveyConstants"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.author.title" /></title>

	<%@ include file="/common/tabbedheader.jsp"%>

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
<body class="stripes" onLoad="init()">
	<form:form action="update.do" method="post" modelAttribute="authoringForm" id="authoringForm">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<form:hidden path="survey.contentId" />
		<input type="hidden" name="mode" value="${mode}"/>
		<form:hidden path="sessionMapID" />
		<form:hidden path="contentFolderID" />
		<form:hidden path="currentTab" id="currentTab" />
	
		<c:set var="title"><fmt:message key="activity.title" /></c:set>
		<lams:Page title="${title}" type="navbar" formID="authoringForm">
		
			<lams:Tabs control="true" title="${title}" helpToolSignature="<%= SurveyConstants.TOOL_SIGNATURE %>" helpModule="authoring">
				<lams:Tab id="1" key="label.authoring.heading.basic" />
				<lams:Tab id="2" key="label.authoring.heading.advance" />
				<lams:Tab id="3" key="label.authoring.heading.conditions" />
			</lams:Tabs>
	
			<lams:TabBodyArea>
			
				<lams:errors/>
	
				<lams:TabBodys>
					<!-- tab content 1 (Basic) -->
					<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
					<!-- end of content (Basic) -->
		
					<!-- tab content 2 (Advanced) -->
					<lams:TabBody id="2" titleKey="label.authoring.heading.advance.desc" page="advance.jsp" />
					<!-- end of content (Advanced) -->
					
					<!-- tab content 4 (Conditions) -->
					<lams:TabBody id="3" titleKey="label.authoring.heading.conditions.desc" page="conditions.jsp" />
					<!-- end of content (Conditions) -->
				</lams:TabBodys>
				
				<!-- Button Row -->
				<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" 
					toolSignature="<%=SurveyConstants.TOOL_SIGNATURE%>" toolContentID="${authoringForm.survey.contentId}" 
					 customiseSessionID="${authoringForm.sessionMapID}" accessMode="${mode}" defineLater="${mode=='teacher'}"
					 contentFolderID="${authoringForm.contentFolderID}" />
			</lams:TabBodyArea>
			
			<div id="footer"></div>
			
		</lams:Page>
	</form:form>

</body>
</lams:html>
