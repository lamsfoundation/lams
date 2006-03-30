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

<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
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
		
		Set tabsBasic = new LinkedHashSet();
		tabsBasic.add("label.basic");
		pageContext.setAttribute("tabsBasic", tabsBasic);
	%>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

	<!DOCTYPE HTML PUBLIC "-//W3C//DTD hTML 4.01 Transitional//EN">
	<html:html locale="true">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<title> <bean:message key="label.authoring"/> </title>
	
	 <lams:css/>
	<!-- depending on user / site preference this will get changed probably use passed in variable from flash to select which one to use-->

 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
    <script type="text/javascript" src="${lams}fckeditor/fckeditor.js"></script>
    <script type="text/javascript" src="${tool}author_page/js/fckcontroller.js"></script>
    <link href="${tool}author_page/css/fckeditor_style.css" rel="stylesheet" type="text/css">

	<script language="JavaScript" type="text/JavaScript">

		function submitMethod(actionMethod) {
			document.QaAuthoringForm.dispatch.value=actionMethod; 
			document.QaAuthoringForm.submit();
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
            
            initEditor("title");
            initEditor("instructions");
            initEditor("questionContent0");
            initEditor("onlineInstructions");
            initEditor("offlineInstructions");
            
            <c:set var="queIndex" scope="session" value="1"/>
            <c:forEach var="questionEntry" items="${sessionScope.mapQuestionContent}">
                <c:set var="queIndex" scope="session" value="${queIndex +1}"/>
                initEditor("<c:out value="Question${queIndex-1}"/>");
            </c:forEach>

        }     
        
        function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	// end optional tab controller stuff
	    	selectTab(tabId);
        } 
        
        function doSubmit(method) {
        	document.QaAuthoringForm.dispatch.value=method;
        	document.QaAuthoringForm.submit();
        }
	
	</script>
	
	<script type="text/javascript" src="<c:out value="${tool}"/>author_page/js/tabcontroller.js"></script>    
	<script type="text/javascript" src="<c:out value="${lams}"/>includes/javascript/common.js"></script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/xmlrequest.js'/>"></script>
	
</head>
<body onLoad="init();">

	<b> <font size=2> <bean:message key="label.authoring.qa"/> </font></b>
	
	<html:form  action="/authoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">
	<html:hidden property="dispatch"/>
	<html:hidden property="toolContentID"/>
	<html:hidden property="currentTab" styleId="currentTab" />
	
	<c:if test="${sessionScope.activeModule != 'defineLater' }"> 			
		<lams:Tabs collection="${tabs}" useKey="true" control="true"/>
		<!-- end tab buttons -->
		<div class="tabbody">
		
		<!-- tab content 1 (Basic) -->
		<lams:TabBody id="1" titleKey="label.basic" page="BasicContent.jsp"/>
		<!-- end of content (Basic) -->
		      
		<!-- tab content 2 (Advanced) -->
		<lams:TabBody id="2" titleKey="label.advanced" page="AdvancedContent.jsp" />
		<!-- end of content (Advanced) -->
		
		<!-- tab content 3 (Instructions) -->
		<lams:TabBody id="3" titleKey="label.instructions" page="InstructionsContent.jsp" />
		<!-- end of content (Instructions) -->
	</c:if> 			
	
	<c:if test="${ (sessionScope.activeModule == 'defineLater') && (sessionScope.defineLaterInEditMode != 'true') }"> 			
		<lams:Tabs collection="${tabsBasic}" useKey="true" control="true"/>
		<!-- end tab buttons -->
		<div class="tabbody">
		
		<!-- tab content 1 (Basic) -->
		<lams:TabBody id="1" titleKey="label.basic" page="BasicContentViewOnly.jsp"/>
		<!-- end of content (Basic) -->
	</c:if> 			
	
	<c:if test="${ (sessionScope.activeModule == 'defineLater') && (sessionScope.defineLaterInEditMode == 'true') }"> 			
		<lams:Tabs collection="${tabsBasic}" useKey="true" control="true"/>
		<!-- end tab buttons -->
		<div class="tabbody">
		
		<!-- tab content 1 (Basic) -->
		<lams:TabBody id="1" titleKey="label.basic" page="BasicContent.jsp"/>
		<!-- end of content (Basic) -->
	</c:if> 			
	
	
	<lams:HTMLEditor/>	
	</html:form>
</body>
</html:html>
