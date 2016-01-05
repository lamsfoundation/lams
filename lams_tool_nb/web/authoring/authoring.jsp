<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
            "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/includes/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<lams:html>
<lams:head>
	<lams:headItems />
	<title><fmt:message key="activity.title" />
	</title>
	<link href="${lams}css/jquery-ui-redmond-theme.css" rel="stylesheet" type="text/css" >
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>

	<script type="text/javascript">
		function init(){
			var showBasicContent = "${requestScope.showBasicContent}";
			if (showBasicContent != "true") {
				// TODO fix this
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
        
		function doUpload() {
			document.NbAuthoringForm.method.value = 'upload';
			document.NbAuthoringForm.submit();
		}        
     </script>
</lams:head>

<body class="stripes" onLoad="init()">

	<div id="page">
		<html:form action="/authoring" styleId="authoringForm" target="_self"
			enctype="multipart/form-data">
			<html:hidden property="toolContentID" />
			<html:hidden property="contentFolderID" />
			<html:hidden property="currentTab" styleId="currentTab" />
			<html:hidden property="defineLater" />

			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			
			<h1>
				<fmt:message key="activity.title" />
			</h1>
			<div id="header">
				<!-- start tabs -->
				<lams:Tabs control="true">
					<lams:Tab id="1" key="label.authoring.heading.basic" />
					<c:if test="${formBean.defineLater != 'true'}">
						<lams:Tab id="2" key="label.authoring.heading.advanced" />
					</c:if>
				</lams:Tabs>
				<!-- end tab buttons -->
			</div>

			<div id="content">
				<!-- show any error messages here -->
				<logic:messagesPresent>
					<p class="warning">
					        <html:messages id="error">
					            <c:out value="${error}" escapeXml="false"/><br/>
					        </html:messages>
					</p>
				</logic:messagesPresent>
								
				<lams:help toolSignature="<%=NoticeboardConstants.TOOL_SIGNATURE%>"
					module="authoring" />
				<!--  Set up tabs  -->
				<lams:TabBody id="1" titleKey="label.authoring.heading.basic"
					page="basic.jsp" />
				<c:if test="${formBean.defineLater != 'true'}">
					<lams:TabBody id="2" titleKey="label.authoring.heading.advanced"
						page="advance.jsp" />
				</c:if>

				<!-- Button Row -->
				<html:hidden property="method" value="save" />
				<c:set var="accessMode">
					<c:choose>
						<c:when test="${formBean.defineLater == 'true'}">
							teacher
						</c:when>
						<c:otherwise>
							author
						</c:otherwise>
					</c:choose>
				</c:set>

				<lams:AuthoringButton formID="authoringForm"
					clearSessionActionUrl="/clearsession.do" toolSignature="lanb11"
					toolContentID="${formBean.toolContentID}"
					cancelButtonLabelKey="button.cancel"
					saveButtonLabelKey="button.save"
					accessMode="${accessMode}"
					defineLater="${formBean.defineLater}"
					contentFolderID="${NbAuthoringForm.contentFolderID}" />
			</div>
			<div id="footer" />
		</html:form>
	</div>
</body>
</lams:html>
