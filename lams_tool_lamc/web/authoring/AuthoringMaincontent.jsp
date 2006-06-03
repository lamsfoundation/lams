<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
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
		
		Set tabsBasic = new LinkedHashSet();
		tabsBasic.add("label.basic");
		pageContext.setAttribute("tabsBasic", tabsBasic);
	%>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

	<!DOCTYPE HTML PUBLIC "-//W3C//DTD hTML 4.01 Transitional//EN">
	<html:html locale="true">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<lams:headItems/>
		<title><fmt:message key="activity.title" /></title>
	
 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
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
            initEditor("richTextReportTitle");            
            initEditor("richTextEndLearningMsg");                        
            initEditor("richTextOfflineInstructions");                                    
            initEditor("richTextOnlineInstructions");                                                
            initEditor("richTextIncorrectFeedback");                                                
            initEditor("richTextCorrectFeedback");                                                            
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
	
	<html:form  action="/authoring?validate=false" styleId="authoringForm" enctype="multipart/form-data" method="POST" target="_self">
	<html:hidden property="dispatch" value="submitQuestions"/>
	<html:hidden property="toolContentID"/>
	<html:hidden property="currentTab" styleId="currentTab" />
	
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
	
	<c:if test="${sessionScope.activeModule == 'defineLater' }"> 			
		<lams:Tabs collection="${tabsBasic}" useKey="true" control="true"/>
		<!-- end tab buttons -->
		<div class="tabbody">
		
		<!-- tab content 1 (Basic) -->
		<lams:TabBody id="1" titleKey="label.basic" page="Basic.jsp"/>
		<!-- end of content (Basic) -->
	</c:if> 			

	</html:form>
	<lams:HTMLEditor/>		
</body>
</html:html>
