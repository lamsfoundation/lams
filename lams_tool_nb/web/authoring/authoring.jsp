<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Set" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD hTML 4.01 Transitional//EN">
<html:html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<title><fmt:message key="tool.display.name" /></title>
	
	<lams:css/>
	
	<!-- this is the custom CSS for the tool -->
	<link href="${tool}css/tool_custom.css" rel="stylesheet" type="text/css">
	
    <script>
    
    	var imgRoot="${lams}images/";
	    var themeName="aqua";
        
        function init(){
        
            initTabSize(3);
            
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
        	if(document.getElementById("tab" + tabId).className == "tab tabcentre_inactive")
        		return false;
        	return true;
        }
 
    </script>
    
 	<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>    
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/xmlrequest.js'/>"></script>
    
</head>

<body onLoad="init()">
<html:form action="/authoring" target="_self" enctype="multipart/form-data">
<html:hidden property="currentTab" styleId="currentTab" />

<h1><fmt:message key="activity.title" /></h1>
    
<%@ include file="../errorbox.jsp" %> <!-- show any error messages here -->
    
<% // in define later mode we only show the basic content, so no point showing the tabs. %>
<c:if test="${requestScope.showBasicContent != 'true'}">
	
	<!-- start tabs -->
	<lams:Tabs collection="${tabs}" useKey="true" control="true"/>
	<lams:Tabs control="true">
		<lams:Tab id="1" key="label.authoring.heading.basic"/>
		<lams:Tab id="2" key="label.authoring.heading.advance" inactive="true"/>
		<lams:Tab id="3" key="label.authoring.heading.instructions"/>
	</lams:Tabs>
	<!-- end tab buttons -->
</c:if>

<div class="tabbody">

<!-- tab content 1 (Basic) -->
<lams:TabBody id="1" titleKey="label.authoring.heading.basic" page="basic.jsp"/>
<!-- end of content (Basic) -->
      
<c:if test="${requestScope.showBasicContent != 'true'}">
	<!-- tab content 2 (Advanced) -->
	<lams:TabBody id="2" titleKey="label.authoring.heading.advance" page="advance.jsp" />
	<!-- end of content (Advanced) -->

	<!-- tab content 3 (Instructions) -->
	<lams:TabBody id="3" titleKey="label.authoring.heading.instructions" page="instructions.jsp" />
	<!-- end of content (Instructions) -->
</c:if>

<!-- Button Row -->
<hr>
<p align="right">
<html:submit property="method" styleClass="button"><fmt:message key="button.save" /></html:submit>
<html:button property="method" onclick="window.close()" styleClass="button"><fmt:message key="button.cancel" /></html:button>
</p>
</div>

</html:form>
</body>
</html:html>
