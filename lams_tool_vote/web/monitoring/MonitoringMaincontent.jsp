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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<%@ page import="java.util.LinkedHashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.lamsfoundation.lams.tool.vote.VoteAppConstants"%>

    <% 
		Set tabs = new LinkedHashSet();
		tabs.add("label.summary");
		tabs.add("label.editActivity");
		tabs.add("label.stats");
		pageContext.setAttribute("tabs", tabs);
	%>

	<lams:html>
	<lams:head>

	<title> <fmt:message key="label.monitoring"/> </title>

	<%@ include file="/common/tabbedheader.jsp"%>
	<script type="text/javascript">
	
	   	var imgRoot="${lams}images/";
	    var themeName="aqua";
	
        function init(){
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
        	document.VoteMonitoringForm.dispatch.value=method;
        	document.VoteMonitoringForm.submit();
        }

		function submitMonitoringMethod(actionMethod) 
		{
			document.VoteMonitoringForm.dispatch.value=actionMethod; 
			document.VoteMonitoringForm.submit();
		}
		
		function submitAuthoringMethod(actionMethod) {
			document.VoteAuthoringForm.dispatch.value=actionMethod; 
			document.VoteAuthoringForm.submit();
		}
		
		function submitModifyQuestion(questionIndexValue, actionMethod) 
		{
			document.VoteMonitoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}
		
		function submitModifyMonitoringNomination(questionIndexValue, actionMethod) 
		{
			document.VoteMonitoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}
		
		
		function submitEditResponse(responseId, actionMethod) 
		{
			document.VoteMonitoringForm.responseId.value=responseId; 
			submitMethod(actionMethod);
		}
		
		function submitMethod(actionMethod) 
		{
			submitMonitoringMethod(actionMethod);
		}
		
		function deleteOption(optIndex, actionMethod) {
			document.VoteMonitoringForm.optIndex.value=optIndex; 
			submitMethod(actionMethod);
		}
		
		function submitSession(selectedToolSessionId, actionMethod) {
			document.VoteMonitoringForm.selectedToolSessionId.value=selectedToolSessionId; 
			submitMonitoringMethod(actionMethod);
		}
		
		
		function submitModifyOption(optionIndexValue, actionMethod) 
		{
			document.VoteMonitoringForm.optIndex.value=optionIndexValue; 
			submitMethod(actionMethod);
		}

		function submitModifyNomination(optionIndexValue, actionMethod) 
		{
			document.VoteMonitoringForm.optIndex.value=optionIndexValue; 
			submitMethod(actionMethod);
		}
		
		function submitOpenVote(currentUid, actionMethod)
		{
			document.VoteMonitoringForm.currentUid.value=currentUid;
	        submitMethod(actionMethod);
		}
	
	</script>
	
</lams:head>
<body class="stripes" onLoad="init();">


	<div id="page">
			<h1> <fmt:message key="label.monitoring"/> </h1>

	<div id="header">
		<lams:Tabs collection="${tabs}" useKey="true" control="true"/>
	</div>	

	<div id="content">						
	    <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		<html:hidden property="dispatch"/>
		<html:hidden property="toolContentID"/>
		<html:hidden property="httpSessionID"/>		
		<html:hidden property="currentTab" styleId="currentTab" />
		<html:hidden property="contentFolderID"/>						
		<html:hidden property="activeModule"/>
		<html:hidden property="defineLaterInEditMode"/>
		<html:hidden property="responseId"/>	 
		<html:hidden property="currentUid"/>
		<html:hidden property="selectedToolSessionId"/>							
		<input type="hidden" name="isToolSessionChanged"/>	
		
		<lams:help toolSignature="<%= VoteAppConstants.MY_SIGNATURE %>" module="monitoring"/>
		
			<lams:TabBody id="1" titleKey="label.summary" page="SummaryContent.jsp"/>
			<lams:TabBody id="2" titleKey="label.editActivity" page="Edit.jsp" />
			<lams:TabBody id="3" titleKey="label.stats" page="Stats.jsp" />
		</html:form>
	</div>	

	<div id="footer"></div>

	</div>
	
</body>
</lams:html>
