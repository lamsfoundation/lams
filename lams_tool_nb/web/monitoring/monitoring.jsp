<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/includes/taglibs.jsp"%>

<%@ page import="java.util.HashMap"%>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.web.NbMonitoringAction"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<html:html>
<head>
	<lams:headItems />
	<title><fmt:message key="activity.title" /></title>

	<script type="text/javascript">

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

		// this is used when the page is initialised/reloaded, to show the correct tab
		function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	// end optional tab controller stuff
        } 
        
        // The following methods control the user switching tabs.
        // Needed as it goes back to the server to get the data (so the page is reloaded)
        function doSwitchSummary(tabId) {
        	doSwitchTab(tabId,"summary");
        }
        function doSwitchInstructions(tabId) {
        	doSwitchTab(tabId,"instructions");
        }
        function doSwitchEditActivity(tabId) {
        	doSwitchTab(tabId,"editActivity");
        }
        function doSwitchStatistics(tabId) {
        	doSwitchTab(tabId,"statistics");
        }
        
		function doSwitchTab(tabId, method) {
        	if(tabId != null) {
        		document.NbMonitoringForm.currentTab.value=tabId;
        	}
        	document.NbMonitoringForm.method.value=method;
        	document.NbMonitoringForm.submit();
        }

	</script>
</head>

<body onLoad='init()'>

	<div id="page">
		<html:form action="/monitoring" target="_self">
			<html:hidden property="method" />
			<html:hidden property="currentTab" styleId="currentTab" />

			<c:set var="monitoringURL">
				<html:rewrite page="/monitoring.do" />
			</c:set>

			<h1>
				<fmt:message key="activity.title" />
			</h1>
			<div id="header">

				<lams:Tabs control="true">
					<lams:Tab id="<%=NbMonitoringAction.SUMMARY_TABID%>" key="titleHeading.summary" methodCall="doSwitchSummary" />
					<lams:Tab id="<%=NbMonitoringAction.INSTRUCTIONS_TABID%>" key="titleHeading.instructions" methodCall="doSwitchInstructions" />
					<lams:Tab id="<%=NbMonitoringAction.EDITACTIVITY_TABID%>" key="titleHeading.editActivity" methodCall="doSwitchEditActivity" />
					<lams:Tab id="<%=NbMonitoringAction.STATISTICS_TABID%>" key="titleHeading.statistics" methodCall="doSwitchStatistics" />
				</lams:Tabs>

			</div>

			<div id="content">
				<lams:TabBody id="1" titleKey="titleHeading.summary" page="m_Summary.jsp" />
				<lams:TabBody id="2" titleKey="titleHeading.instructions" page="m_Instructions.jsp" />
				<lams:TabBody id="3" titleKey="titleHeading.editActivity" page="m_EditActivity.jsp" />
				<lams:TabBody id="4" titleKey="titleHeading.statistics" page="m_Statistics.jsp" />

			</div>
			<div id="footer" />
				<lams:HTMLEditor />
		</html:form>
	</div>

</body>


</html:html>

