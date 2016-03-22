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
	<!-- ******************** END FCK Editor related javascript & HTML ********************** -->

 
</lams:head>
<body class="stripes" onLoad="init()">
<div id="page">
		<h1>
			<fmt:message key="label.author.title" />
		</h1>
<div id="header">
		<lams:Tabs useKey="true" control="true">
			<lams:Tab id="1" key="label.authoring.heading.basic" />
			<lams:Tab id="2" key="label.authoring.heading.advanced" />
		</lams:Tabs></div>
		<!-- start tabs -->
<div id="content">
		<!-- end tab buttons -->
		
		<%@ include file="/common/messages.jsp"%>

		<html:form action="authoring/update" method="post" styleId="authoringForm" enctype="multipart/form-data">
		<c:set var="formBean"  value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<html:hidden property="daco.contentId" />
		<html:hidden property="sessionMapID" />
		<html:hidden property="contentFolderID" />
		<html:hidden property="currentTab" styleId="currentTab" />

		<span class="pull-right voffset5">
		<lams:help toolSignature="<%= DacoConstants.TOOL_SIGNATURE %>" module="authoring"/>
		</span>
		
		<lams:TabBodys>
			<!-- tab content 1 (Basic) -->
			<lams:TabBody id="1" titleKey="label.authoring.heading.basic.description" page="basic.jsp" />
			<!-- end of content (Basic) -->

			<!-- tab content 2 (Advanced) -->
			<lams:TabBody id="2" titleKey="label.authoring.heading.advanced.description" page="advanced.jsp" />
			<!-- end of content (Advanced) -->
		</lams:TabBodys>

			<!-- Button Row -->
			<%--  Default value 
				cancelButtonLabelKey="label.common.cancel"
				saveButtonLabelKey="label.authoring.save.button"
				cancelConfirmMsgKey="message.authoring.cancel.save"
				accessMode="author"
			--%>
			<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" 
				toolSignature="<%=DacoConstants.TOOL_SIGNATURE%>" toolContentID="${formBean.daco.contentId}" 
				 customiseSessionID="${formBean.sessionMapID}"
				 contentFolderID="${formBean.contentFolderID}"
				 cancelConfirmMsgKey="message.authoring.cancel.save" />
	</html:form>

</div>

<div id="footer"></div>
<!-- end page div -->
</div>
</body>
</lams:html>