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
		tabs.add("label.summary");
		tabs.add("label.instructions");
		tabs.add("label.editActivity");
		tabs.add("label.stats");
		pageContext.setAttribute("tabs", tabs);
		
	%>

	<html:html locale="true">
	<head>
	<title><fmt:message key="activity.title" /></title>

	<%@ include file="/common/header.jsp"%>
	<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/common.js"></script>

 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
	<script language="JavaScript" type="text/JavaScript">

		function submitMonitoringMethod(actionMethod) 
		{
			document.QaMonitoringForm.dispatch.value=actionMethod; 
			document.QaMonitoringForm.submit();
		}
		
		function submitAuthoringMethod(actionMethod) {
			document.QaAuthoringForm.dispatch.value=actionMethod; 
			document.QaAuthoringForm.submit();
		}
		
		
		function submitModifyQuestion(questionIndexValue, actionMethod) 
		{
			document.QaMonitoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}
		
		function submitEditResponse(responseId, actionMethod) 
		{
			document.QaMonitoringForm.responseId.value=responseId; 
			submitMethod(actionMethod);
		}
		
		function submitEditGroupResponse(sessionId, responseId, actionMethod) 
		{
			document.QaMonitoringForm.sessionId.value=sessionId; 
			document.QaMonitoringForm.responseId.value=responseId; 
			submitMethod(actionMethod);
		}
		
		
		function submitMethod(actionMethod) 
		{
			submitMonitoringMethod(actionMethod);
		}
		
		function deleteOption(deletableOptionIndex, actionMethod) {
			document.QaMonitoringForm.deletableOptionIndex.value=deletableOptionIndex; 
			submitMethod(actionMethod);
		}
		
		function submitSession(selectedToolSessionId, actionMethod) {
			document.QaMonitoringForm.selectedToolSessionId.value=selectedToolSessionId; 
			submitMonitoringMethod(actionMethod);
		}

		function submitResponse(currentUid, actionMethod)
		{
			document.QaMonitoringForm.currentUid.value=currentUid;
	        submitMethod(actionMethod);
		}

		function submitGroupResponse(sessionId, currentUid, actionMethod)
		{
			document.QaMonitoringForm.sessionId.value=sessionId;
			document.QaMonitoringForm.currentUid.value=currentUid;
	        submitMethod(actionMethod);
		}
		

		function submitModifyMonitoringQuestion(questionIndexValue, actionMethod) 
		{
			document.QaMonitoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}



    	var imgRoot="${lams}images/";
	    var themeName="aqua";
        
        function init(){
        
            initTabSize(4);
            
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
	    	selectTab(tabId);
        } 
        
        function doSubmit(method) {
        	document.QaMonitoringForm.dispatch.value=method;
        	document.QaMonitoringForm.submit();
        }
	
	</script>
	
</head>
<body onLoad="init();">

<div id="page">
	<h1> <bean:message key="label.monitoring"/> </h1>

	<div id="header">
			<lams:Tabs collection="${tabs}" useKey="true" control="true"/>	
	</div>	
	
	<div id="content">		
	    <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		<html:hidden property="dispatch"/>
		<html:hidden property="currentUid"/>
		<html:hidden property="toolContentID"/>
		<html:hidden property="activeModule"/>
		<html:hidden property="currentTab" styleId="currentTab" />
		<html:hidden property="editResponse"/>		
		<html:hidden property="httpSessionID"/>		
		<html:hidden property="defaultContentIdStr"/>						
		<html:hidden property="contentFolderID"/>						

		<lams:TabBody id="1" titleKey="label.summary" page="SummaryContent.jsp"/>
		<lams:TabBody id="2" titleKey="label.instructions" page="Instructions.jsp" />
		<lams:TabBody id="3" titleKey="label.editActivity" page="Edit.jsp" />
		<lams:TabBody id="4" titleKey="label.stats" page="Stats.jsp" />
		</html:form>
	</div>	
	
	<div id="footer"></div>

	</div>
	
</body>
</html:html>
