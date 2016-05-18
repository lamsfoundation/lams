<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.commonCartridge.CommonCartridgeConstants"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.author.title" /></title>

	<%@ include file="/common/tabbedheader.jsp"%>
	
	<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
	<script>
        function init(){
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
	<html:form action="authoring/update" method="post" styleId="authoringForm" enctype="multipart/form-data">
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<html:hidden property="commonCartridge.contentId" />
		<html:hidden property="mode" value="teacher"/>
		<html:hidden property="contentFolderID" />
		<html:hidden property="sessionMapID" />
		
		<c:set var="title"><fmt:message key="label.author.title" /></c:set>
		<lams:Page title="${title}" type="navbar">
		
		 	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= CommonCartridgeConstants.TOOL_SIGNATURE %>" helpModule="authoring">
				<lams:Tab id="1" key="label.authoring.heading.basic" />
			</lams:Tabs>
			
			<lams:TabBodyArea>
				<%@ include file="/common/messages.jsp"%>
				
		 		<lams:TabBodys>
					<!-- tab content 1 (Basic) -->
					<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
					<!-- end of content (Basic) -->
				</lams:TabBodys>
		
				<!-- Button Row -->
				<div id="saveCancelButtons">
					<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="<%=CommonCartridgeConstants.TOOL_SIGNATURE%>" 
						toolContentID="${formBean.commonCartridge.contentId}"  accessMode="teacher" defineLater="yes"  
						customiseSessionID="${formBean.sessionMapID}" contentFolderID="${formBean.contentFolderID}"/>
				</div> 
			</lams:TabBodyArea>
			
			<div id="footer"></div>
		
		</lams:Page>
	</html:form>
</body>
</lams:html>
