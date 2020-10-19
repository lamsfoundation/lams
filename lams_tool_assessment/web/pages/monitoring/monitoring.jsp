<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.assessment.AssessmentConstants"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>
<c:set var="assessment" value="${sessionMap.assessment}"/>

<lams:html>
	<lams:head>
		<%@ include file="/common/tabbedheader.jsp" %>
		<link href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet" type="text/css">
		<link href="${lams}css/jquery-ui.timepicker.css" rel="stylesheet" type="text/css">
		<link href="${lams}css/thickbox.css" rel="stylesheet" rel="stylesheet" type="text/css">
		<link href="${lams}css/free.ui.jqgrid.min.css" rel="stylesheet" type="text/css" >
 		<lams:css suffix="chart"/>
 		<link href="${lams}css/jquery.jqGrid.confidence-level-formattter.css" rel="stylesheet" type="text/css">
 		
 		<style>
	 		.ui-jqdialog.ui-jqgrid-bootstrap .ui-jqdialog-titlebar, .ui-jqgrid.ui-jqgrid-bootstrap .ui-jqgrid-caption {
				background-color: #f5f5f5;
	 		}
	 		
	 		.countdown-timeout {
	  			color: #FF3333 !important;
	  		}
	  		
	  		#time-limit-table th {
	  			vertical-align: middle;
	  		}
	  		
	  		#time-limit-table td.centered {
	  			text-align: center;
	  		}
	  		
	  		#completion-chart-container {
	  			width: 400px;
	  			margin: auto;
	  		}
 		</style>
 		
		<script type="text/javascript" src="${lams}includes/javascript/portrait.js"></script>
		<script>
			// pass settings to monitorToolSummaryAdvanced.js
			var submissionDeadlineSettings = {
				lams: '<lams:LAMSURL />',
				submissionDeadline: '${submissionDeadline}',
				submissionDateString: '${submissionDateString}',
				setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>?<csrf:token/>',
				toolContentID: '${param.toolContentID}',
				messageNotification: '<fmt:message key="monitor.summary.notification" />',
				messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
				messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
			},
			confidenceLevelsSettings = {
				type: "${assessment.confidenceLevelsType}",
				LABEL_NOT_CONFIDENT : '<fmt:message key="label.not.confident" />',
				LABEL_CONFIDENT : '<fmt:message key="label.confident" />',
				LABEL_VERY_CONFIDENT : '<fmt:message key="label.very.confident" />',
				LABEL_NOT_SURE : '<fmt:message key="label.not.sure" />',
				LABEL_SURE : '<fmt:message key="label.sure" />',
				LABEL_VERY_SURE : '<fmt:message key="label.very.sure" />'
			};
		</script>
 		<script type="text/javascript" src="${lams}includes/javascript/thickbox.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/monitorToolSummaryAdvanced.js" ></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script> 
 		<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
   		<script type="text/javascript" src="${lams}includes/javascript/d3.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/chart.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/chart.bundle.min.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/jquery.cookie.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/download.js"></script>
 		<script type="text/javascript" src="${lams}/includes/javascript/portrait.js" ></script>
 		<script type="text/javascript" src="${lams}/includes/javascript/jquery.jqGrid.confidence-level-formattter.js"></script>
 		
		<script>        
		    function doSelectTab(tabId) {
		    	if ( tabId == 3 )
		    		doStatistic();
			   	selectTab(tabId);
		    }
		    
			function doStatistic(){
				var url = '<c:url value="/monitoring/statistic.do?sessionMapID=${sessionMapID}"/>';
				$.ajaxSetup({ cache: true });
				$("#statisticArea").load(
					url,
					{
						toolContentID: ${param.toolContentID}, 
						reqID: (new Date()).getTime()
					}
				);
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
