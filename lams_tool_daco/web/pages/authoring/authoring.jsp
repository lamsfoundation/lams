<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.daco.DacoConstants"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.common.heading" /></title>

	<%@ include file="/common/tabbedheader.jsp"%>

	<script type="text/javascript">
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
<html:form action="authoring/update" method="post" styleId="authoringForm" enctype="multipart/form-data">
<c:set var="formBean"  value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

	<c:set var="title"><fmt:message key="label.common.heading" /></c:set>
	<lams:Page title="${title}" type="navbar">
	
	 	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= DacoConstants.TOOL_SIGNATURE %>" helpModule="authoring">
			<lams:Tab id="1" key="label.authoring.heading.basic" />
			<lams:Tab id="2" key="label.authoring.heading.advanced" />
		</lams:Tabs>	
	
	 	<lams:TabBodyArea>
			<%@ include file="/common/messages.jsp"%>
			<html:hidden property="mode" value="${mode}"/>
			<html:hidden property="daco.contentId" />
			<html:hidden property="sessionMapID" />
			<html:hidden property="contentFolderID" />
			<html:hidden property="currentTab" styleId="currentTab" />
	
	 		<lams:TabBodys>
				<!-- tab content 1 (Basic) -->
				<lams:TabBody id="1" titleKey="label.authoring.heading.basic.description" page="basic.jsp" />
				<!-- end of content (Basic) -->
	
				<!-- tab content 2 (Advanced) -->
				<lams:TabBody id="2" titleKey="label.authoring.heading.advanced.description" page="advanced.jsp" />
				<!-- end of content (Advanced) -->
			</lams:TabBodys>
	
			<!-- Button Row -->
			<div id="saveCancelButtons">
			<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" 
				toolSignature="<%=DacoConstants.TOOL_SIGNATURE%>" toolContentID="${formBean.daco.contentId}" 
				 customiseSessionID="${formBean.sessionMapID}" accessMode="${mode}" defineLater="${mode=='teacher'}"
				 contentFolderID="${formBean.contentFolderID}"
				 cancelConfirmMsgKey="message.authoring.cancel.save" />
			</div>
		</lams:TabBodyArea>
	 
	
	<div id="footer"></div>
	<!-- end page div -->
	</lams:Page>

</html:form>

</body>
</lams:html>