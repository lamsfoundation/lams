<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.assessment.AssessmentConstants"%>

<c:set var="lams"><lams:LAMSURL /></c:set>

<lams:html>
	<lams:head>
		<%@ include file="/common/tabbedheader.jsp" %>
		<link href="${lams}css/jquery-ui-redmond-theme.css" rel="stylesheet" type="text/css" >
		<link href="${lams}css/jquery-ui.timepicker.css" rel="stylesheet" type="text/css" >
		<link href="${lams}css/thickbox.css" rel="stylesheet" type="text/css" media="screen">
		<link href="${lams}css/jquery.jqGrid.css" rel="stylesheet" type="text/css"/>
		<style type="text/css">
			.ui-jqgrid tr.jqgrow td {
			    white-space: normal !important;
			    height:auto;
			    vertical-align:text-top;
			    padding-top:2px;
			}
			
			.session-container {
				padding-left: 30px;
				width:99%; 
			}
			.session-header {
				padding-bottom: 5px;
				font-size: small;
			}
			div[id^="gbox_userSummary"] {
				margin-top: 5px;
			}
		</style>
 
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script> 
 		<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/thickbox.js"></script>
	 	
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
				<fmt:message key="label.monitoring.heading" />
			</h1>
			<div id="header">
				<lams:Tabs>
					<lams:Tab id="1" key="monitoring.tab.summary" />
					<lams:Tab id="2" key="monitoring.tab.edit.activity" />			
					<lams:Tab id="3" key="monitoring.tab.statistics" />
				</lams:Tabs>
			</div>
			<div id="content">
					<lams:help toolSignature="<%= AssessmentConstants.TOOL_SIGNATURE %>" module="monitoring"/>
			
					<lams:TabBody id="1" titleKey="monitoring.tab.summary" page="summary.jsp" />
					<lams:TabBody id="2" titleKey="monitoring.tab.edit.activity" page="editactivity.jsp" />			
					<lams:TabBody id="3" titleKey="monitoring.tab.statistics" page="statistic.jsp" />
			</div>
			<div id="footer"></div>
		
		</div>
	</body>
</lams:html>
