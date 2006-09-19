<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
            "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/includes/taglibs.jsp"%>

<lams:html>
<head>
	<lams:headItems />
	<title><fmt:message key="activity.title" /></title>

	<script type="text/javascript">
		function init(){
			var showBasicContent = "${requestScope.showBasicContent}";
			if (showBasicContent != "true") {
	            initTabSize(3);
	        }
	        
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
	    	if(active(tabId))
	    		selectTab(tabId);
        } 
        
        function active(tabId) {
        	//if(document.getElementById("tab" + tabId).className == "tab tabcentre_inactive")
        	//	return false;
        	return true;
        }
        
		function deleteAttachment(uuid) {
			document.NbAuthoringForm.method.value = '<fmt:message key="link.delete"/>';
			document.NbAuthoringForm.deleteFileUuid.value = uuid;
			document.NbAuthoringForm.submit();
		}        
		
		function doUpload() {
			document.NbAuthoringForm.method.value = '<fmt:message key="button.upload" />';
			document.NbAuthoringForm.submit();
		}        
     </script>  
</head>

<body onLoad="init()">

	<div id="page">
		<html:form action="/authoring" styleId="authoringForm" target="_self"
			enctype="multipart/form-data">
			<html:hidden property="toolContentID" />
			<html:hidden property="contentFolderID" />
			<html:hidden property="currentTab" styleId="currentTab" />
			<html:hidden property="sessionMapID" />

			<c:set var="formBean"
				value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<c:set var="sessionMap"
				value="${sessionScope[formBean.sessionMapID]}" scope="request" />

			<%-- in define later mode we edit only the basic content. But if we don't have the instructions tab, then the instructions are lost when saving. --%>
			<h1>
				<fmt:message key="activity.title" />
			</h1>
			<div id="header">
				<!-- start tabs -->
				<lams:Tabs control="true">
					<lams:Tab id="1" key="label.authoring.heading.basic" />
					<lams:Tab id="2" key="label.authoring.heading.advanced"
						inactive="true" />
					<c:choose>
						<c:when test="${formBean.defineLater != 'true'}">
							<lams:Tab id="3" key="label.authoring.heading.instructions" />
						</c:when>
						<c:otherwise>
							<lams:Tab id="3" key="label.authoring.heading.instructions"
								inactive="true" />
						</c:otherwise>
					</c:choose>
				</lams:Tabs>
				<!-- end tab buttons -->
			</div>

			<div id="content">
				<!-- show any error messages here -->
				<%@ include file="../errorbox.jsp"%>

				<!--  Set up tabs  -->
				<lams:TabBody id="1" titleKey="label.authoring.heading.basic"
					page="basic.jsp" />
				<lams:TabBody id="2" titleKey="label.authoring.heading.advanced"
					page="advance.jsp" />
				<lams:TabBody id="3" titleKey="label.authoring.heading.instructions"
					page="instructions.jsp" />

				<!-- Button Row -->
				<c:set var="dispatchMethodName">
					<fmt:message key="button.save" />
				</c:set>
				<html:hidden property="method" value="${dispatchMethodName}" />
				<lams:AuthoringButton formID="authoringForm"
					clearSessionActionUrl="/clearsession.do" toolSignature="lanb11"
					toolContentID="${formBean.toolContentID}"
					cancelButtonLabelKey="button.cancel"
					saveButtonLabelKey="button.save"
					customiseSessionID="${sessionMap.sessionID}"
					contentFolderID="${NbAuthoringForm.contentFolderID}" />
			</div>
			<div id="footer" />
		</html:form>
	</div>
</body>
</lams:html>