<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.daco.DacoConstants"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.common.heading" /></title>

	<%@ include file="/common/tabbedheader.jsp"%>

	<script type="text/javascript">
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
	<!-- ******************** END FCK Editor related javascript & HTML ********************** -->
			
</lams:head>
<body class="stripes" onLoad="init()">

	<html:form action="authoring/update" method="post" styleId="authoringForm" enctype="multipart/form-data">
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<html:hidden property="daco.contentId" />
		<html:hidden property="mode" value="teacher"/>
		<html:hidden property="contentFolderID" />
		<html:hidden property="sessionMapID" />
		
	<c:set var="title"><fmt:message key="label.common.heading" /></c:set>
	<lams:Page title="${title}" type="navbar">

 	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= DacoConstants.TOOL_SIGNATURE %>" helpModule="authoring">
			<lams:Tab id="1" key="label.authoring.heading.basic" />
	</lams:Tabs>	

 	<lams:TabBodyArea>
		<%@ include file="/common/messages.jsp"%>
 		<lams:TabBodys>
			<!-- tab content 1 (Basic) -->
			<lams:TabBody id="1" titleKey="label.authoring.heading.basic.description" page="basic.jsp" />
			<!-- end of content (Basic) -->
		</lams:TabBodys>

		<!-- Button Row -->
		<div id="saveCancelButtons">
			<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="<%=DacoConstants.TOOL_SIGNATURE%>" 
				toolContentID="${formBean.daco.contentId}"  accessMode="teacher" defineLater="yes"  
				customiseSessionID="${formBean.sessionMapID}" contentFolderID="${formBean.contentFolderID}"/>
		</div> 
	</lams:TabBodyArea>

	<div id="footer"></div>
	<!-- end page div -->
	</lams:Page>

</html:form>

</body>
</lams:html>