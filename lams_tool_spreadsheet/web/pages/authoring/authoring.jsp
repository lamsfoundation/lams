<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants"%>
<c:set var="spreadsheetURL"><lams:WebAppURL/>includes/javascript/simple_spreadsheet/spreadsheet_offline.html?lang=${pageContext.response.locale.language}</c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="label.authoring.title" /></title>

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
        
    	function onSubmitHandler() {
        	var code = window.frames["externalSpreadsheet"].cellsToJS();
 			document.getElementById("spreadsheet-code").value = code;
         	return true;
    	}
    	var spreadsheetCode = "${sessionMap.code}";
        
    </script> 
</lams:head>
<body class="stripes" onLoad="init()">
<form:form action="updateContent.do" method="post" id="spreadsheetForm" modelAttribute="spreadsheetForm" enctype="multipart/form-data" onsubmit="return onSubmitHandler();">
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
	<form:hidden path="spreadsheet.contentId" />
	<form:hidden path="sessionMapID" />
	<form:hidden path="contentFolderID" />
	<form:hidden path="currentTab" id="currentTab" />

	<c:set var="title"><fmt:message key="activity.title"/> ${lang}</c:set>
	<lams:Page title="${title}" type="navbar">

	 	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= SpreadsheetConstants.TOOL_SIGNATURE %>" helpModule="authoring">
			<lams:Tab id="1" key="label.authoring.heading.basic" />
			<lams:Tab id="2" key="label.authoring.heading.advance" />
	    </lams:Tabs>   

		<lams:TabBodyArea>
			<lams:errors/>
               
			<!--  Set up tabs  -->
			<lams:TabBodys>
				<!-- tab content 1 (Basic) -->
				<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
				<!-- end of content (Basic) -->
		
				<!-- tab content 2 (Advanced) -->
				<lams:TabBody id="2" titleKey="label.authoring.heading.advance.desc" page="advance.jsp" />
				<!-- end of content (Advanced) -->
			</lams:TabBodys>

		<!-- Button Row -->
		<lams:AuthoringButton formID="spreadsheetForm" clearSessionActionUrl="/clearsession.do" 
			toolSignature="<%=SpreadsheetConstants.TOOL_SIGNATURE%>" toolContentID="${spreadsheetForm.spreadsheet.contentId}" 
			 customiseSessionID="${spreadsheetForm.sessionMapID}" accessMode="${mode}" defineLater="${mode=='teacher'}"
			 contentFolderID="${spreadsheetForm.contentFolderID}" />
		</lams:TabBodyArea>

		<div id="footer"></div>
	</lams:Page>	
</form:form>
</body>
</lams:html>
