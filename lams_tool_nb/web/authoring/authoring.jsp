<!DOCTYPE html>
           
<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<lams:html>
<lams:head>
  	<link href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet" type="text/css" />
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
<form:form modelAttribute="nbAuthoringForm" action="save.do" id="nbAuthoringForm" enctype="multipart/form-data">
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>

<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:Page title="${title}" type="navbar" formID="nbAuthoringForm">

			<form:hidden path="toolContentID" />
			<form:hidden path="contentFolderID" />
			<form:hidden path="currentTab" id="currentTab" />
			<form:hidden path="defineLater" />
		 	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= NoticeboardConstants.TOOL_SIGNATURE %>" helpModule="authoring">
				<lams:Tab id="1" key="label.authoring.heading.basic" />
				<lams:Tab id="2" key="label.authoring.heading.advanced" />
			</lams:Tabs>	
		
		 	<lams:TabBodyArea>
		 		<c:set var="hasErrors">
					<form:errors path='*'/>
				</c:set>
				<c:if test="${not empty hasErrors}">
					<lams:Alert id="error" type="danger" close="false">
						<form:errors path="*"/>
					</lams:Alert>
				</c:if>
		 		
				<!--  Set up tabs  -->
		 		<lams:TabBodys>
   					<lams:TabBody id="1" titleKey="label.authoring.heading.basic" page="basic.jsp" />
 					<lams:TabBody id="2" titleKey="label.authoring.heading.advanced" page="advance.jsp" />
  				</lams:TabBodys>
		
				<!-- Button Row -->
				<c:set var="accessMode">
					<c:choose>
						<c:when test="${nbAuthoringForm.defineLater == 'true'}">
							teacher
						</c:when>
						<c:otherwise>
							author
						</c:otherwise>
					</c:choose>
				</c:set>
				<lams:AuthoringButton formID="nbAuthoringForm"
					clearSessionActionUrl="/clearsession.do" toolSignature="lanb11"
					toolContentID="${nbAuthoringForm.toolContentID}"
					cancelButtonLabelKey="button.cancel"
					saveButtonLabelKey="button.save"
					accessMode="${accessMode}"
					defineLater="${nbAuthoringForm.defineLater}"
					contentFolderID="${nbAuthoringForm.contentFolderID}" />
			</lams:TabBodyArea>

<div id="footer"></div>
<!-- end page div -->
</lams:Page>

</form:form>

</body>
</lams:html>
