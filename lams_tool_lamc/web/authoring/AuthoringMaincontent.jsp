<%--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
--%>

<%@ include file="../sharing/share.jsp" %>
<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<%@ page import="java.util.LinkedHashSet" %>
<%@ page import="java.util.Set" %>

    <% 
		Set tabs = new LinkedHashSet();
		tabs.add("label.basic");
		tabs.add("label.advanced");
		tabs.add("label.instructions");
		pageContext.setAttribute("tabs", tabs);
		
	%>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

	<!DOCTYPE HTML PUBLIC "-//W3C//DTD hTML 4.01 Transitional//EN">
	<html:html locale="true">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<title> <bean:message key="label.authoring"/> </title>
    <link href="<c:out value="${tool}"/>author_page/css/aqua.css" rel="stylesheet" type="text/css">		
	<script lang="javascript">
	var imgRoot="<c:out value="${lams}"/>images/";
	var themeName="aqua";
	</script>
	<script type="text/javascript" src="<c:out value="${tool}"/>author_page/js/tabcontroller.js"></script>    
	<script type="text/javascript" src="<c:out value="${lams}"/>includes/javascript/common.js"></script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/xmlrequest.js'/>"></script>
	
	<!-- this is the custom CSS for the tool -->
	<link href="<c:out value="${tool}"/>css/tool_custom.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${lams}fckeditor/fckeditor.js"></script>
    <script type="text/javascript" src="${tool}author_page/js/fckcontroller.js"></script>
    <link href="${tool}author_page/css/fckeditor_style.css" rel="stylesheet" type="text/css">
	
	<script language="JavaScript" type="text/JavaScript">

		function submitModifyQuestion(questionIndexValue, actionMethod) 
		{
			document.McAuthoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}
		
		function submitMethod(actionMethod) 
		{
			document.McAuthoringForm.dispatch.value=actionMethod; 
			document.McAuthoringForm.submit();
		}
		
		function deleteOption(deletableOptionIndex, actionMethod) {
			document.McAuthoringForm.deletableOptionIndex.value=deletableOptionIndex; 
			submitMethod(actionMethod);
		}
		
		function submitDeleteFile(uuid, actionMethod) 
		{
			document.McAuthoringForm.uuid.value=uuid; 
			submitMethod(actionMethod);
		}
		
		function MM_reloadPage(init) {  //reloads the window if Nav4 resized
		  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
		    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
		  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
		}

	    	var imgRoot="${lams}images/";
	    var themeName="aqua";
        
        function init(){
        
            initTabSize(3);
            
            var tag = document.getElementById("currentTab");
	    	if(tag.value != "")
	    		selectTab(tag.value);
            else
                selectTab(1); //select the default tab;
            
            initEditor("richTextTitle");
            initEditor("richTextInstructions");
            initEditor("richTextOnlineInstructions");
            initEditor("richTextOfflineInstructions");
            
        }     
        
        function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	// end optional tab controller stuff
	    	selectTab(tabId);
        } 
        
        function doSubmit(method) {
        	document.McAuthoringForm.dispatch.value=method;
        	document.McAuthoringForm.submit();
        }
	
	</script>
</head>
<body onLoad="init();">

	<b> <font size=2> <bean:message key="label.authoring.mc"/> </font></b>
	
	<html:form  action="/authoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">
	<html:hidden property="dispatch"/>
	<html:hidden property="toolContentID"/>
	<html:hidden property="currentTab" styleId="currentTab" />
	

	<c:if test="${sessionScope.activeModule == 'defineLater'}"> 			
		<c:if test="${sessionScope.editOptionsMode == 0}"> 			
			<jsp:include page="/authoring/BasicContent.jsp" />
		</c:if> 				
		<c:if test="${sessionScope.editOptionsMode == 1}"> 			
			<jsp:include page="/authoring/OptionsContent.jsp" />
		</c:if> 				
	</c:if> 			
	<c:if test="${sessionScope.activeModule != 'defineLater' }"> 			
		<lams:Tabs collection="${tabs}" useKey="true" control="true"/>
		<!-- end tab buttons -->
		<div class="tabbody">
		
		<!-- tab content 1 (Basic) -->
		<lams:TabBody id="1" titleKey="label.basic" page="Basic.jsp"/>
		<!-- end of content (Basic) -->
		      
		<!-- tab content 2 (Advanced) -->
		<lams:TabBody id="2" titleKey="label.advanced" page="AdvancedContent.jsp" />
		<!-- end of content (Advanced) -->
		
		<!-- tab content 3 (Instructions) -->
		<lams:TabBody id="3" titleKey="label.instructions" page="InstructionsContent.jsp" />
		<!-- end of content (Instructions) -->
	</c:if> 			

	
	<lams:HTMLEditor/>	
	</html:form>
</body>
</html:html>
