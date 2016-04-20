<!DOCTYPE html>
            

<%@ include file="/includes/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<lams:html>
<lams:head>
  	<link href="${lams}css/jquery-ui-redmond-theme.css" rel="stylesheet" type="text/css" />
 	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>

	<lams:headItems />
	<title><fmt:message key="activity.title" />
	</title>
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

<html:form action="/authoring" styleId="authoringForm" target="_self" enctype="multipart/form-data">
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:Page title="${title}" type="navbar">

			<html:hidden property="toolContentID" />
			<html:hidden property="contentFolderID" />
			<html:hidden property="currentTab" styleId="currentTab" />
			<html:hidden property="defineLater" />
		
		 	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= NoticeboardConstants.TOOL_SIGNATURE %>" helpModule="authoring">
				<lams:Tab id="1" key="label.authoring.heading.basic" />
				<lams:Tab id="2" key="label.authoring.heading.advanced" />
			</lams:Tabs>	
		
		 	<lams:TabBodyArea>
		 		<logic:messagesPresent>
				<lams:Alert id="errorMessages" type="danger" close="false">
			        <html:messages id="error">
        			    <c:out value="${error}" escapeXml="false"/><BR/>
   				     </html:messages>
 			    </lams:Alert>
				</logic:messagesPresent>
		 		
				<!--  Set up tabs  -->
		 		<lams:TabBodys>
   					<lams:TabBody id="1" titleKey="label.authoring.heading.basic" page="basic.jsp" />
 					<lams:TabBody id="2" titleKey="label.authoring.heading.advanced" page="advance.jsp" />
  				</lams:TabBodys>
		
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
			</lams:TabBodyArea>

<div id="footer"></div>
<!-- end page div -->
</lams:Page>

</html:form>

</body>
</lams:html>
