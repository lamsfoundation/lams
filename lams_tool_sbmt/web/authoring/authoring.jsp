<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<%@ page import="org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants"%>
<html:html>
<head>
	
	<lams:headItems />
	<title><fmt:message key="activity.title" /></title>
	<!-- ********************  CSS ********************** -->
	<link href="<html:rewrite page='/includes/css/tool_custom.css'/>" rel="stylesheet" type="text/css">	
	<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/common.js"></script>
	
	<script type="text/javascript">
    
    	var imgRoot="${lams}images/";
	    var themeName="aqua";
        
        function init(){
        
            initTabSize(3);
            
            var tag = document.getElementById("currentTab");
	    	if(tag.value != "")
	    		selectTab(tag.value);
            else
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
        
        function doSubmit(method) {
        	document.getElementById("authoringForm").dispatch.value=method;
        	document.getElementById("authoringForm").submit();
        }
        
    </script>

</head>
<body onLoad="init()">
	<div id="page">


		<h1>
			<fmt:message key="label.authoring.heading" />
		</h1>
		<div id="header">
			<lams:Tabs control="true">
				<lams:Tab id="1" key="label.authoring.heading.basic" />
				<lams:Tab id="2" key="label.authoring.heading.advance" />
				<lams:Tab id="3" key="label.authoring.heading.instructions" />
			</lams:Tabs>
		</div>

		<div id="content">
			<table>
			<tr><td>
			<%@ include file="/common/messages.jsp"%>
			</td></tr>
			</table>
			
			<html:form action="authoring" method="post" styleId="authoringForm" enctype="multipart/form-data">
				<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
			
				<input type="hidden" name="mode" value="author">
				<html:hidden property="sessionMapID" />
				<html:hidden property="toolContentID" />
				<html:hidden property="currentTab" styleId="currentTab" />
				<html:hidden property="dispatch" value="updateContent" />

				<!-- tab content 1 (Basic) -->
				<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
				<!-- end of content (Basic) -->

				<!-- tab content 2 (Advanced) -->
				<lams:TabBody id="2" titleKey="label.authoring.heading.advance.desc" page="advance.jsp" />
				<!-- end of content (Advanced) -->

				<!-- tab content 3 (Instructions) -->
				<lams:TabBody id="3" titleKey="label.authoring.heading.instructions.desc" page="instructions.jsp" />
				<!-- end of content (Instructions) -->

				<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" 
					toolSignature="<%=SbmtConstants.TOOL_SIGNATURE%>" toolContentID="${formBean.toolContentID}" 
					customiseSessionID="${formBean.sessionMapID}" />

				<lams:HTMLEditor />
			</html:form>
		</div>
		<div id="footer"></div>
	</div>
	<!--closes page-->
</body>
</html:html>
