<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="title"><fmt:message key="activity.title" /></c:set>
<%@ page import="java.util.HashSet"%>
<%@ page import="org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.author.title" /></title>
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
</lams:head>
<body class="stripes" onLoad="init()">

	<html:form action="authoring/update" method="post" styleId="authoringForm" enctype="multipart/form-data">
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<html:hidden property="dokumaran.contentId" />
		<html:hidden property="mode" value="teacher"/>
		<html:hidden property="contentFolderID" />
		<html:hidden property="sessionMapID" />
		
		<lams:Page title="${title}" type="navbar">
		
			<lams:Tabs control="true" title="${title}" helpToolSignature="<%= DokumaranConstants.TOOL_SIGNATURE %>" helpModule="authoring">
				<lams:Tab id="1" key="label.authoring.heading.basic" />
			</lams:Tabs>    
	
			<lams:TabBodyArea>
			<%@ include file="/common/messages.jsp"%>
		   
			    <!--  Set up tabs  -->
			    <lams:TabBodys>
					<!-- tab content 1 (Basic) -->
					<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
					<!-- end of content (Basic) -->
				</lams:TabBodys>
	
				<!-- Button Row -->
				<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="<%=DokumaranConstants.TOOL_SIGNATURE%>" 
					toolContentID="${formBean.dokumaran.contentId}"  accessMode="teacher" defineLater="yes"  
					customiseSessionID="${formBean.sessionMapID}" contentFolderID="${formBean.contentFolderID}"/>

			</lams:TabBodyArea>

			<div id="footer"></div>
		</lams:Page>
	</html:form>
	
</body>
</lams:html>
