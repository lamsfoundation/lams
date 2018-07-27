<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants"%>

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>

	<%@ include file="/common/tabbedheader.jsp"%>
	<link href="${lams}css/jquery-ui-smoothness-theme.css" rel="stylesheet" type="text/css" >
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script>
        function init(){
            var tag = document.getElementById("currentTab");
	    	if(tag.value != "") {
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
    </script>
</lams:head>
<body class="stripes" onLoad="init()">
<html:form action="authoring/update" method="post" styleId="authoringForm" enctype="multipart/form-data">
	<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" scope="request"/>		
	<html:hidden property="peerreview.contentId" />
	<html:hidden property="mode" value="${mode}"/>
	<html:hidden property="sessionMapID" />
	<html:hidden property="contentFolderID" />
	<html:hidden property="currentTab" styleId="currentTab" />

	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	<lams:Page title="${title}" type="navbar">

		<lams:Tabs control="true" title="${title}" helpToolSignature="<%= PeerreviewConstants.TOOL_SIGNATURE %>" helpModule="authoring">
			<lams:Tab id="1" key="label.authoring.heading.basic" />
 			<lams:Tab id="2" key="label.authoring.heading.advance" />
 		</lams:Tabs>
		
		<lams:TabBodyArea>
		
			<%@ include file="/common/messages.jsp"%>
		
			<lams:TabBodys>
				<!-- tab content 1 (Basic) -->
				<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
				<!-- end of content (Basic) -->
	
				<!-- tab content 2 (Advanced) -->
 				<lams:TabBody id="2" titleKey="label.authoring.heading.advance.desc" page="advance.jsp" />
 				<!-- end of content (Advanced) -->
			</lams:TabBodys>

			<!-- Button Row -->
			<%--  Default value 
				cancelButtonLabelKey="label.authoring.cancel.button"
				saveButtonLabelKey="label.authoring.save.button"
				cancelConfirmMsgKey="authoring.msg.cancel.save"
				accessMode="author"
			--%>
			<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" 
				toolSignature="<%=PeerreviewConstants.TOOL_SIGNATURE%>" toolContentID="${formBean.peerreview.contentId}" 
				 customiseSessionID="${formBean.sessionMapID}" accessMode="${mode}" defineLater="${mode=='teacher'}"
				 contentFolderID="${formBean.contentFolderID}" />
		</lams:TabBodyArea>

		<div id="footer"></div>
		
	</lams:Page>
	
</html:form>
</body>
</lams:html>
