<!DOCTYPE html>
        
<c:set var="title"><fmt:message key="activity.title" /></c:set>

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
		<html:hidden property="resource.contentId" />
		<html:hidden property="mode" value="teacher"/>
		<html:hidden property="contentFolderID" />
		<html:hidden property="sessionMapID" />
		
	<lams:Page title="${title}" type="navbar">

		<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ResourceConstants.TOOL_SIGNATURE %>" helpModule="authoring">
			<lams:Tab id="1" key="authoring.tab.basic" />
		</lams:Tabs>    
	
		<lams:TabBodyArea>
			<%@ include file="/common/messages.jsp"%>
		   
		    <!--  Set up tabs  -->
		     <lams:TabBodys>
				<lams:TabBody id="1" titleKey="authoring.tab.basic" page="basic.jsp" />
		    </lams:TabBodys>
	
			<!-- Button Row -->
			<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="<%=ResourceConstants.TOOL_SIGNATURE%>" 
				toolContentID="${formBean.resource.contentId}"  accessMode="teacher" defineLater="yes"  
				customiseSessionID="${formBean.sessionMapID}" contentFolderID="${formBean.contentFolderID}"/>

		</lams:TabBodyArea>

		<div id="footer"></div>

	</lams:Page>

	</html:form>
</body>
</lams:html>
