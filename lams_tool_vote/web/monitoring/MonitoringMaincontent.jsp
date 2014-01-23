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
