<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>

<%@ page import="java.util.LinkedHashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.lamsfoundation.lams.tool.mc.McAppConstants"%>

    <% 
		Set tabs = new LinkedHashSet();
		tabs.add("label.summary");
		tabs.add("label.editActivity");
		tabs.add("label.stats");
		pageContext.setAttribute("tabs", tabs);
	%>

<lams:html>
<lams:head>

	<title><fmt:message key="label.monitoring"/></title>
	
	<link href="<lams:LAMSURL/>css/thickbox.css" rel="stylesheet" type="text/css" media="screen">

	<%@ include file="/common/header.jsp"%>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/thickbox.js"></script>
	<script type="text/javascript">
	
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
        	document.McMonitoringForm.dispatch.value=method;
        	document.McMonitoringForm.submit();
        }

		function submitMonitoringMethod(actionMethod) {
			document.McMonitoringForm.dispatch.value=actionMethod; 
			document.McMonitoringForm.submit();
		}
		
		function submitMethod(actionMethod) {
			submitMonitoringMethod(actionMethod);
		}
		
		function submitMonitoringMethod(actionMethod) {
			document.McMonitoringForm.dispatch.value=actionMethod; 
			document.McMonitoringForm.submit();
		}
		
		function submitChangeDisplayAnswers(displayAnswers, actionMethod) {
			document.McMonitoringForm.displayAnswers.value=displayAnswers; 
			submitMonitoringMethod(actionMethod);
		}
	
	</script>
	
</lams:head>
<body onLoad="init();" class="stripes">

	<div id="page">
		<h1> <fmt:message key="label.monitoring"/> </h1>

		<div id="header">
			<lams:Tabs collection="${tabs}" useKey="true" control="true"/>
		</div>	
	
		<div id="content">						
		    <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self" styleId="monitoringForm">		
				<html:hidden property="dispatch"/>
				<html:hidden property="toolContentID"/>
				<html:hidden property="httpSessionID"/>
				<html:hidden property="currentTab" styleId="currentTab" />
				<html:hidden property="contentFolderID"/>
				<html:hidden property="responseId"/>
			
				<lams:help toolSignature="<%= McAppConstants.MY_SIGNATURE %>" module="monitoring"/>
			
				<lams:TabBody id="1" titleKey="label.summary" page="SummaryContent.jsp"/>
				<lams:TabBody id="2" titleKey="label.editActivity" page="Edit.jsp" />
				<lams:TabBody id="3" titleKey="label.stats" page="Stats.jsp" />
			</html:form>
		</div>	
	
		<div id="footer"></div>

	</div>
	
</body>
</lams:html>
