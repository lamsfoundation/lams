<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.assessment.AssessmentConstants"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/tabbedheader.jsp" %>
		<link href="<lams:LAMSURL/>css/thickbox.css" rel="stylesheet" type="text/css" media="screen">
		<link rel="stylesheet" type="text/css" href="<html:rewrite page='/includes/css/jqGrid.grid.css'/>" />
 
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery-1.2.6.pack.js'/>"></script>
				 
		<script type="text/javascript"> 
			var pathToImageFolder = "<lams:LAMSURL/>images/";
			var pathToJsFolder = "<html:rewrite page='/includes/javascript/'/>"; 
		</script>
 		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/thickbox.js'/>"></script>
	 	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.jqGrid.js'/>"></script>
	 	
		<script>
			var initialTabId = "${initialTabId}";
	
			function init(){
				if (initialTabId) {
					selectTab(initialTabId);
				} else {
					selectTab(1);
				}
		    }     
		        
		    function doSelectTab(tabId) {
			   	// end optional tab controller stuff
			   	selectTab(tabId);
		    } 
		</script>	  
	</lams:head>
	
	<body class="stripes" onLoad="init()">
		<div id="page">
			<h1>
				<fmt:message key="label.authoring.heading" />
			</h1>
			<div id="header">
				<lams:Tabs>
					<lams:Tab id="1" key="monitoring.tab.summary" />
					<lams:Tab id="2" key="monitoring.tab.instructions" />
					<lams:Tab id="3" key="monitoring.tab.edit.activity" />			
					<lams:Tab id="4" key="monitoring.tab.statistics" />
				</lams:Tabs>
			</div>
			<div id="content">
					<lams:help toolSignature="<%= AssessmentConstants.TOOL_SIGNATURE %>" module="monitoring"/>
			
					<lams:TabBody id="1" titleKey="monitoring.tab.summary" page="summary.jsp" />
					<lams:TabBody id="2" titleKey="monitoring.tab.instructions" page="instructions.jsp"/>
					<lams:TabBody id="3" titleKey="monitoring.tab.edit.activity" page="editactivity.jsp" />			
					<lams:TabBody id="4" titleKey="monitoring.tab.statistics" page="statistic.jsp" />
			</div>
			<div id="footer"></div>
		
		</div>
	</body>
</lams:html>
