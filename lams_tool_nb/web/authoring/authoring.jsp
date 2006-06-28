<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/includes/taglibs.jsp"%>

<html:html>
<head>
	<lams:headItems />
	<title><fmt:message key="activity.title" /></title>

	<script type="text/javascript">
    
    	var imgRoot="<lams:LAMSURL />images/";
	    var themeName="aqua";
        
        function init(){
			
			initEditor("title");						
			initEditor("content");
			var showBasicContent = "${requestScope.showBasicContent}";
			if (showBasicContent != "true") {
	            initTabSize(3);
	            
       			initEditor("onlineInstructions");						
       			initEditor("offlineInstructions");
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
     </script>
</head>

<body onLoad="init()">

	<div id="page">
		<html:form action="/authoring" styleId="authoringForm" target="_self" enctype="multipart/form-data">
			<html:hidden property="currentTab" styleId="currentTab" />

			<%// in define later mode we only show the basic content, so no point showing the tabs. %>
			<c:choose>
				<c:when test="${requestScope.showBasicContent != 'true'}">
					<h1>
						<fmt:message key="activity.title" />
					</h1>
					<div id="header">
						<!-- start tabs -->
						<lams:Tabs control="true">
							<lams:Tab id="1" key="label.authoring.heading.basic" />
							<lams:Tab id="2" key="label.authoring.heading.advanced" inactive="true" />
							<lams:Tab id="3" key="label.authoring.heading.instructions" />
						</lams:Tabs>
						<!-- end tab buttons -->
					</div>
				</c:when>
				<c:otherwise>
					<h1 class="no-tabs-below">
						<fmt:message key="activity.title" />
					</h1>
					<div id="header-no-tabs"></div>
				</c:otherwise>
			</c:choose>

			<div id="content">
				<!-- show any error messages here -->
				<%@ include file="../errorbox.jsp"%>

				<c:choose>
					<c:when test="${requestScope.showBasicContent != 'true'}">
						<!--  Set up tabs  -->
						<lams:TabBody id="1" titleKey="label.authoring.heading.basic" page="basic.jsp" />
						<lams:TabBody id="2" titleKey="label.authoring.heading.advanced" page="advance.jsp" />
						<lams:TabBody id="3" titleKey="label.authoring.heading.instructions" page="instructions.jsp" />
					</c:when>
					<c:otherwise>
						<!-- just include the basic page -->
						<%@ include file="basic.jsp"%>
					</c:otherwise>
				</c:choose>

				<!-- Button Row -->
				<c:set var="dispactchMethodName">
					<fmt:message key="button.save" />
				</c:set>
				<html:hidden property="method" value="${dispactchMethodName}" />
				<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="lanb11" toolContentID="${toolContentID}" cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save" />
			</div>
			<div id="footer" />
				<lams:HTMLEditor />
		</html:form>
	</div>
</body>
</html:html>
