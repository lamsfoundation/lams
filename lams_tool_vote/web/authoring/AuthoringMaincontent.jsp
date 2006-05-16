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
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<title> <bean:message key="activity.title"/>  </title>
	
	 <lams:css/>
	<!-- depending on user / site preference this will get changed probably use passed in variable from flash to select which one to use-->

 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
    <script type="text/javascript" src="${lams}fckeditor/fckeditor.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/fckcontroller.js"></script>
    <link href="${lams}css/fckeditor_style.css" rel="stylesheet" type="text/css">

	<script language="JavaScript" type="text/JavaScript">

		function submitModifyOption(optionIndexValue, actionMethod) 
		{
			document.VoteAuthoringForm.optIndex.value=optionIndexValue; 
			submitMethod(actionMethod);
		}
		
		function submitMethod(actionMethod) 
		{
			document.VoteAuthoringForm.dispatch.value=actionMethod; 
			document.VoteAuthoringForm.submit();
		}
		
		function deleteOption(deletableOptionIndex, actionMethod) {
			document.VoteAuthoringForm.deletableOptionIndex.value=deletableOptionIndex; 
			submitMethod(actionMethod);
		}
		
		function submitDeleteFile(uuid, actionMethod) 
		{
			document.VoteAuthoringForm.uuid.value=uuid; 
			submitMethod(actionMethod);
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
            

            initEditor("richTextOfflineInstructions");                                    
	        initEditor("richTextOnlineInstructions");                                    

            
            initEditor("title");
            initEditor("instructions");
            
            initEditor("optionContent0");
            <c:set var="optIndex" scope="session" value="1"/>
            <c:forEach var="questionEntry" items="${sessionScope.mapOptionsContent}">
                <c:set var="optIndex" scope="session" value="${optIndex +1}"/>
                initEditor("<c:out value="optionContent${optIndex-1}"/>");
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
        	document.VoteAuthoringForm.dispatch.value=method;
        	document.VoteAuthoringForm.submit();
        }
	
	</script>
	
	<script type="text/javascript" src="<c:out value="${lams}"/>includes/javascript/tabcontroller.js"></script>    
	<script type="text/javascript" src="<c:out value="${lams}"/>includes/javascript/common.js"></script>
	
</head>
<body onLoad="init();">

	<b> <font size=2> <bean:message key="label.authoring.vote"/> </font></b>
	
	<html:form  styleId="authoringForm" action="/authoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">
	<html:hidden property="dispatch" value="submitAllContent"/>
	<html:hidden property="toolContentID"/>
	<html:hidden property="currentTab" styleId="currentTab" />
	<html:hidden property="activeModule"/>
	
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
		<!-- Button Row -->
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession" toolSignature="lavote11" 
			cancelButtonLabelKey="label.cancel" saveButtonLabelKey="label.save" toolContentID="${formBean.toolContentID}" />		
		
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
		<!-- Button Row -->
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession" toolSignature="lavote11" 
			cancelButtonLabelKey="label.cancel" saveButtonLabelKey="label.save" toolContentID="${formBean.toolContentID}" />			
	</c:if> 			

	
	<lams:HTMLEditor/>	
	</html:form>
</body>
</html:html>
