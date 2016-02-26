<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants"%>


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
        	document.getElementById("spreadsheet.code").value = code;
        	return true;
    	}
        
    </script>
	<!-- ******************** END CK Editor related javascript & HTML ********************** -->

 
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
		</lams:Tabs></div>
		<!-- start tabs -->
<div id="content">
	<!-- end tab buttons -->
		
	<%@ include file="/common/messages.jsp"%>

	<html:form action="authoring/update" method="post" styleId="authoringForm" enctype="multipart/form-data" onsubmit="return onSubmitHandler();">
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<html:hidden property="spreadsheet.contentId" />
		<html:hidden property="sessionMapID" />
		<html:hidden property="contentFolderID" />
		<html:hidden property="currentTab" styleId="currentTab" />

		<lams:help toolSignature="<%= SpreadsheetConstants.TOOL_SIGNATURE %>" module="authoring"/>

		<!-- tab content 1 (Basic) -->
		<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
		<!-- end of content (Basic) -->

		<!-- tab content 2 (Advanced) -->
		<lams:TabBody id="2" titleKey="label.authoring.heading.advance.desc" page="advance.jsp" />
		<!-- end of content (Advanced) -->

		<!-- Button Row -->
		<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" 
			toolSignature="<%=SpreadsheetConstants.TOOL_SIGNATURE%>" toolContentID="${formBean.spreadsheet.contentId}" 
			 customiseSessionID="${formBean.sessionMapID}"
			 contentFolderID="${formBean.contentFolderID}" />
	</html:form>

</div>

<div id="footer"></div>
<!-- end page div -->
</div>

</body>
</lams:html>
