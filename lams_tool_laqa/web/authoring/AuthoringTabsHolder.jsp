
<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

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
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
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

	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
	<html:html locale="true">
	<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<lams:headItems/>
	<title><fmt:message key="activity.title" /></title>

 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
	<script language="JavaScript" type="text/JavaScript">

		function submitMethod(actionMethod) {
			document.QaAuthoringForm.dispatch.value=actionMethod; 
			document.QaAuthoringForm.submit();
		}

    	var imgRoot="${lams}images/";
	    var themeName="aqua";
        
        function init(){
			if (document.QaAuthoringForm.activeModule.value != 'defineLater')
			{
	            initTabSize(3);
	            
	            var tag = document.getElementById("currentTab");
		    	if(tag.value != "")
		    		selectTab(tag.value);
	            else
	                selectTab(1); //select the default tab;
	            
	            initEditor("onlineInstructions");                                    
		        initEditor("offlineInstructions");                                    
			}
			else
			{
	            initTabSize(1);
	            
	            var tag = document.getElementById("currentTab");
		    	if(tag.value != "")
		    		selectTab(tag.value);
	            else
	                selectTab(1); //select the default tab;
			}
			
	            initEditor("title");
	            initEditor("instructions");
	            
	            initEditor("questionContent0");
	            <c:set var="queIndex" scope="session" value="1"/>
	            <c:forEach var="questionEntry" items="${sessionScope.mapQuestionContent}">
	                <c:set var="queIndex" scope="session" value="${queIndex+1}"/>
	                initEditor("<c:out value="questionContent${queIndex-1}"/>");
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
	
</head>
<body onLoad="init();">

	<b> <font size=2> <bean:message key="label.authoring.qa"/> </font></b>

	<html:form  action="/authoring?validate=false" styleId="authoringForm" enctype="multipart/form-data" method="POST" target="_self">
	<html:hidden property="dispatch" value="submitAllContent"/>
	<html:hidden property="toolContentID"/>
	<html:hidden property="currentTab" styleId="currentTab" />
	<html:hidden property="activeModule"/>
	
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
		<!-- Button Row -->

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
	
	</html:form>
	<lams:HTMLEditor/>	
</body>
</html:html>
