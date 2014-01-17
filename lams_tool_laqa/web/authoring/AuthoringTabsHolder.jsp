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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

  http://www.gnu.org/licenses/gpl.txt
--%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<%@ page import="java.util.LinkedHashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.lamsfoundation.lams.tool.qa.QaAppConstants"%>

    <% 
		Set tabs = new LinkedHashSet();
		tabs.add("label.basic");
		tabs.add("label.advanced");
		tabs.add("label.conditions");
		pageContext.setAttribute("tabs", tabs);
		
		Set tabsBasic = new LinkedHashSet();
		tabsBasic.add("label.basic");
		pageContext.setAttribute("tabsBasic", tabsBasic);
	%>

<lams:html>		
	<lams:head>
	<title><fmt:message key="activity.title" /></title>

	<%@ include file="/common/tabbedheader.jsp"%>
	<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
	
 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
	<script language="JavaScript" type="text/JavaScript">

		function submitMethod(actionMethod) {
			document.QaAuthoringForm.dispatch.value=actionMethod; 
			document.QaAuthoringForm.submit();
		}
		
		function submitModifyAuthoringQuestion(questionIndexValue, actionMethod) 
		{
			document.QaAuthoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}
        
        function init(){
			if (document.QaAuthoringForm.activeModule.value != 'defineLater')
			{
	            var tag = document.getElementById("currentTab");
		    	if(tag.value != "")
		    		selectTab(tag.value);
	            else
	                selectTab(1); //select the default tab;
	            
			}
			else
			{
	            var tag = document.getElementById("currentTab");
		    	if(tag.value != "")
		    		selectTab(tag.value);
	            else
	                selectTab(1); //select the default tab;
			}
	       
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
</lams:head>


<body class="stripes" onLoad="init();">

<div id="page">
	<h1>  <fmt:message key="label.authoring.qa"/> </h1>
	
	<div id="header">
		<c:if test="${qaGeneralAuthoringDTO.activeModule != 'defineLater' }"> 			
			<lams:Tabs collection="${tabs}" useKey="true" control="true" />
		</c:if> 			
		<c:if test="${(qaGeneralAuthoringDTO.activeModule == 'defineLater') && (qaGeneralAuthoringDTO.defineLaterInEditMode != 'true') }"> 			
			<lams:Tabs collection="${tabsBasic}" useKey="true" control="true"/>
		</c:if> 					
		<c:if test="${(qaGeneralAuthoringDTO.activeModule == 'defineLater') && (qaGeneralAuthoringDTO.defineLaterInEditMode == 'true') }"> 					
			<lams:Tabs collection="${tabsBasic}" useKey="true" control="true"/>		
		</c:if> 						
	</div>

	<div id="content">	
		<html:form  action="/authoring?validate=false" styleId="authoringForm" enctype="multipart/form-data" method="POST" target="_self">
		<html:hidden property="dispatch" value="submitAllContent"/>
		<html:hidden property="toolContentID"/>
		<html:hidden property="currentTab" styleId="currentTab" />
		<html:hidden property="activeModule"/>
		<html:hidden property="httpSessionID"/>								
		<html:hidden property="defaultContentIdStr"/>								
		<html:hidden property="defineLaterInEditMode"/>										
		<html:hidden property="contentFolderID"/>												
		
		<%@ include file="/common/messages.jsp"%>
		
		<lams:help toolSignature="<%= QaAppConstants.MY_SIGNATURE %>" module="authoring"/>
		
		<c:if test="${qaGeneralAuthoringDTO.activeModule != 'defineLater' }"> 			
			<!-- tab content 1 (Basic) -->
			<lams:TabBody id="1" titleKey="label.basic" page="BasicContent.jsp"/>
			<!-- end of content (Basic) -->
			      
			<!-- tab content 2 (Advanced) -->
			<lams:TabBody id="2" titleKey="label.advanced" page="AdvancedContent.jsp" />
			<!-- end of content (Advanced) -->

			<!-- tab content 3 (Conditions) -->
			<lams:TabBody id="3" titleKey="label.conditions" page="conditions.jsp" />
			<!-- end of content (Conditions) -->
			
			<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
			<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="laqa11" 
			cancelButtonLabelKey="label.cancel" saveButtonLabelKey="label.save" toolContentID="${formBean.toolContentID}" 
			contentFolderID="${formBean.contentFolderID}" />
		</c:if> 			

		<c:if test="${ (qaGeneralAuthoringDTO.activeModule == 'defineLater') && (qaGeneralAuthoringDTO.defineLaterInEditMode != 'true') }"> 			
			<!-- tab content 1 (Basic) -->
			<lams:TabBody id="1" titleKey="label.basic" page="BasicContentViewOnly.jsp"/>
			<!-- end of content (Basic) -->
		</c:if> 			

		<c:if test="${ (qaGeneralAuthoringDTO.activeModule == 'defineLater') && (qaGeneralAuthoringDTO.defineLaterInEditMode == 'true') }"> 			
			<!-- tab content 1 (Basic) -->
			<lams:TabBody id="1" titleKey="label.basic" page="BasicContent.jsp"/>
			<!-- end of content (Basic) -->
		</c:if> 			
		</html:form>		
	</div>

	<div id="footer"></div>

</div>


</body>
</lams:html>



