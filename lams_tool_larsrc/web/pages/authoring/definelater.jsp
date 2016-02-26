<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="org.lamsfoundation.lams.tool.rsrc.ResourceConstants"%>
<%@ page import="java.util.Set"%>
<%Set tabs = new HashSet();
			tabs.add("label.authoring.heading.basic");
			pageContext.setAttribute("tabs", tabs);

			%>
<lams:html>
<lams:head>
	<title><fmt:message key="label.author.title" /></title>

	<%@ include file="/common/tabbedheader.jsp"%>

	<script>
        
        function init(){
            selectTab(1); //select the default tab;
            
            initEditor("Title");
            initEditor("Instructions");
            initEditor("OnlineInstruction");
            initEditor("OfflineInstruction");
            
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
<div id="page">
	<html:form action="authoring/update" method="post" styleId="authoringForm" enctype="multipart/form-data">
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<html:hidden property="resource.contentId" />
		<html:hidden property="mode" value="teacher"/>
		<html:hidden property="contentFolderID" />
		<html:hidden property="sessionMapID" />
		
		<h1>
			<fmt:message key="label.authoring.heading" />
		</h1>
<div id="header">
		<!-- start tabs -->
		<lams:Tabs collection="${tabs}" useKey="true" control="true" />
</div>
<div id="content">
		<%@ include file="/common/messages.jsp"%>
		
		<lams:help toolSignature="<%= ResourceConstants.TOOL_SIGNATURE %>" module="authoring"/>
		<!-- end tab buttons -->
		<div class="tabbody">
			<!-- tab content 1 (Basic) -->
			<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
			<!-- end of content (Basic) -->


			<!-- Button Row -->
			<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="<%=ResourceConstants.TOOL_SIGNATURE%>" 
				toolContentID="${formBean.resource.contentId}"  accessMode="teacher" defineLater="yes"  
				customiseSessionID="${formBean.sessionMapID}" contentFolderID="${formBean.contentFolderID}"/>
		</div>


	</html:form>
	
</div>
<div id="footer"></div>
</div>
</body>
</lams:html>
