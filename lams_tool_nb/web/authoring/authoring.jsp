<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Set" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD hTML 4.01 Transitional//EN">
<html:html>
<head>
	<lams:headItems/>
	<title><fmt:message key="activity.title" /></title>
	
    <script>
    
    	var imgRoot="<lams:LAMSURL />images/";
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
    
</head>

<body onLoad="init()">
<html:form action="/authoring" styleId="authoringForm" target="_self" enctype="multipart/form-data">
<html:hidden property="currentTab" styleId="currentTab" />

<h1><fmt:message key="activity.title" /></h1>
    
<%@ include file="../errorbox.jsp" %> <!-- show any error messages here -->
    
<% // in define later mode we only show the basic content, so no point showing the tabs. %>
<c:if test="${requestScope.showBasicContent != 'true'}">
	
	<!-- start tabs -->
	<lams:Tabs collection="${tabs}" useKey="true" control="true"/>
	<lams:Tabs control="true">
		<lams:Tab id="1" key="label.authoring.heading.basic"/>
		<lams:Tab id="2" key="label.authoring.heading.advanced" inactive="true"/>
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
	<lams:TabBody id="2" titleKey="label.authoring.heading.advanced" page="advance.jsp" />
	<!-- end of content (Advanced) -->

	<!-- tab content 3 (Instructions) -->
	<lams:TabBody id="3" titleKey="label.authoring.heading.instructions" page="instructions.jsp" />
	<!-- end of content (Instructions) -->
</c:if>

<!-- Button Row -->
<hr>
<%--  Default value
	cancelButtonLabelKey="label.authoring.cancel.button"
	saveButtonLabelKey="label.authoring.save.button"
	cancelConfirmMsgKey="authoring.msg.cancel.save"
	accessMode="author"
--%>
<c:set var="dispactchMethodName">
	<fmt:message key="button.save"/>
</c:set>
<html:hidden property="method" value="${dispactchMethodName}"/>
<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="lanb11" toolContentID="${toolContentID}" cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save"/>

</div>

</html:form>
</body>
</html:html>
