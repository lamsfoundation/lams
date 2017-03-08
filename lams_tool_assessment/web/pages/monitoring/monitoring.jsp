<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.assessment.AssessmentConstants"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/tabbedheader.jsp" %>
		<link href="${lams}css/jquery-ui-smoothness-theme.css" rel="stylesheet" type="text/css" >
		<link href="${lams}css/jquery-ui.timepicker.css" rel="stylesheet" type="text/css" >
		<link href="${lams}css/thickbox.css" rel="stylesheet" rel="stylesheet" type="text/css">
		<link href="${lams}css/jquery.jqGrid.css" rel="stylesheet" type="text/css"/>
		<style type="text/css">
			.ui-jqgrid tr.jqgrow td {
			    white-space: normal !important;
			    height:auto;
			    vertical-align:text-top;
			    padding-top:2px;
			}
		</style>

		<script>
			// pass settings to monitorToolSummaryAdvanced.js
			var submissionDeadlineSettings = {
				lams: '<lams:LAMSURL />',
				submissionDeadline: '${submissionDeadline}',
				submissionDateString: '${submissionDateString}',
				setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>',
				toolContentID: '${param.toolContentID}',
				messageNotification: '<fmt:message key="monitor.summary.notification" />',
				messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
				messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
			};
		</script>
 		<script type="text/javascript" src="${lams}includes/javascript/thickbox.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/monitorToolSummaryAdvanced.js" ></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script> 
 		<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.js"></script>
 		
 		<script type="text/javascript" src="${lams}includes/javascript/jquery.cookie.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/download.js"></script>
 		
		<script>        
		    function doSelectTab(tabId) {
			   	// end optional tab controller stuff
			   	selectTab(tabId);
		    } 
		</script>	  
	</lams:head>
	
	<body class="stripes">		
		<c:set var="title"><fmt:message key="label.author.title" /></c:set>
	 	<lams:Page type="navbar" title="${title}"> 
	
			<lams:Tabs control="true" title="${title}" helpToolSignature="<%= AssessmentConstants.TOOL_SIGNATURE %>" helpModule="monitoring" refreshOnClickAction="javascript:location.reload();">
				<lams:Tab id="1" key="monitoring.tab.summary"/>
				<lams:Tab id="2" key="monitoring.tab.edit.activity"/>
				<lams:Tab id="3" key="monitoring.tab.statistics"/>
			</lams:Tabs>
	
	  		<lams:TabBodyArea>
				<lams:TabBodys>
					<lams:TabBody id="1" titleKey="monitoring.tab.summary" page="summary.jsp" />
					<lams:TabBody id="2" titleKey="monitoring.tab.edit.activity" page="editactivity.jsp" />			
					<lams:TabBody id="3" titleKey="monitoring.tab.statistics" page="statistic.jsp" />
				</lams:TabBodys> 
			</lams:TabBodyArea> 
	
			<div id="footer"></div>
		</lams:Page>
	</body>
</lams:html>
