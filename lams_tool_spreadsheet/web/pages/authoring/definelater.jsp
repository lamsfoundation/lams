<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants"%>
<%@ page import="java.util.Set"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.authoring.title" /></title>

	<%@ include file="/common/tabbedheader.jsp"%>

	<script>
        
        function init(){
            selectTab(1); //select the default tab;
            
            initEditor("Title"); // what is this?
            initEditor("Instructions");
        }     
        
        function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	// end optional tab controller stuff
	    	selectTab(tabId);
        } 
        
       
    </script>
	<!-- ******************** END CK Editor related javascript & HTML ********************** -->


</lams:head>
<body class="stripes" onLoad="init()">

	<html:form action="authoring/update" method="post" styleId="authoringForm" enctype="multipart/form-data">
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<html:hidden property="spreadsheet.contentId" />
		<html:hidden property="mode" value="teacher"/>
		<html:hidden property="contentFolderID" />
		<html:hidden property="sessionMapID" />

	<c:set var="title"><fmt:message key="activity.title"/></c:set>
	<lams:Page title="${title}" type="navbar">

	 	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= SpreadsheetConstants.TOOL_SIGNATURE %>" helpModule="authoring">
			<lams:Tab id="1" key="label.authoring.heading.basic" />
	    </lams:Tabs>   

		<lams:TabBodyArea>
			<%@ include file="/common/messages.jsp"%>
               
			<!--  Set up tabs  -->
			<lams:TabBodys>
				<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
			</lams:TabBodys>

			<!-- Button Row -->
			<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="<%=SpreadsheetConstants.TOOL_SIGNATURE%>" 
				toolContentID="${formBean.spreadsheet.contentId}"  accessMode="teacher" defineLater="yes"  
				customiseSessionID="${formBean.sessionMapID}" contentFolderID="${formBean.contentFolderID}"/>

		</lams:TabBodyArea>

		<div id="footer"></div>
	</lams:Page>	
			
	</html:form>
	
</body>
</lams:html>
