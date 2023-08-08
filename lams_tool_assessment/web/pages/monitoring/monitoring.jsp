<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.assessment.AssessmentConstants"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>
<c:set var="assessment" value="${sessionMap.assessment}"/>

<lams:html>
	<lams:head>
		<title><c:out value="${assessment.title}" /></title>
		
		<%@ include file="/common/tabbedheader.jsp" %>
		<link href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet" type="text/css">
		<link href="${lams}css/jquery-ui.timepicker.css" rel="stylesheet" type="text/css">
		<link href="${lams}css/thickbox.css" rel="stylesheet" rel="stylesheet" type="text/css">
		<link href="${lams}css/free.ui.jqgrid.min.css" rel="stylesheet" type="text/css" >
 		<lams:css suffix="chart"/>
 		<link href="${lams}css/jquery.jqGrid.confidence-level-formattter.css" rel="stylesheet" type="text/css">
 		<c:if test="${not empty codeStyles}">
			<link rel="stylesheet" type="text/css" href="${lams}css/codemirror.css" />
		</c:if>
	
 		<style>
	 		.ui-jqdialog.ui-jqgrid-bootstrap .ui-jqdialog-titlebar, .ui-jqgrid.ui-jqgrid-bootstrap .ui-jqgrid-caption {
				background-color: #f5f5f5;
	 		}
	  		
	  		#completion-charts-container > div {
	  			padding: 5rem 2rem;
	  		}
	  		
	  		pre {
				background-color: initial;
				border: none;
			}
			
			.requires-grading {
				background-color: rgba(255, 195, 55, .6);
			}
 		</style>
 		
		<lams:JSImport src="includes/javascript/portrait.js" />
		<script>
	        // avoid name clash between bootstrap and jQuery UI
	        $.fn.bootstrapTooltip = $.fn.tooltip.noConflict();
        
			var LAMS_URL    = '<lams:LAMSURL />',
				WEB_APP_URL = '<lams:WebAppURL />',
				LABELS = {
					ACTIVITY_COMPLETION_CHART_TITLE : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.completion"/></spring:escapeBody>',
					ACTIVITY_COMPLETION_CHART_POSSIBLE_LEARNERS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.completion.possible"/></spring:escapeBody>',
					ACTIVITY_COMPLETION_CHART_STARTED_LEARNERS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.completion.started"/></spring:escapeBody>',
					ACTIVITY_COMPLETION_CHART_COMPLETED_LEARNERS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.completion.completed"/></spring:escapeBody>',
					ANSWERED_QUESTIONS_CHART_TITLE : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.answered.questions"/></spring:escapeBody>',
					ANSWERED_QUESTIONS_CHART_TITLE_GROUPS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.answered.questions.groups"/></spring:escapeBody>',
					ANSWERED_QUESTIONS_CHART_X_AXIS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.answered.questions.x.axis"/></spring:escapeBody>',
					ANSWERED_QUESTIONS_CHART_Y_AXIS_STUDENTS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.answered.questions.y.axis.students"/></spring:escapeBody>',
					ANSWERED_QUESTIONS_CHART_Y_AXIS_GROUPS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.answered.questions.y.axis.groups"/></spring:escapeBody>'
				},
				// pass settings to monitorToolSummaryAdvanced.js
			    submissionDeadlineSettings = {
					lams: '<lams:LAMSURL />',
					submissionDeadline: '${submissionDeadline}',
					submissionDateString: '${submissionDateString}',
					setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>?<csrf:token/>',
					toolContentID: '<c:out value="${param.toolContentID}" />',
					messageNotification: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.notification" /></spring:escapeBody>',
					messageRestrictionSet: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.date.restriction.set" /></spring:escapeBody>',
					messageRestrictionRemoved: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.date.restriction.removed" /></spring:escapeBody>'
				},
				confidenceLevelsSettings = {
					type: "${assessment.confidenceLevelsType}",
					LABEL_NOT_CONFIDENT: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.not.confident" /></spring:escapeBody>',
					LABEL_CONFIDENT: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.confident" /></spring:escapeBody>',
					LABEL_VERY_CONFIDENT: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.very.confident" /></spring:escapeBody>',
					LABEL_NOT_SURE: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.not.sure" /></spring:escapeBody>',
					LABEL_SURE: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.sure" /></spring:escapeBody>',
					LABEL_VERY_SURE: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.very.sure" /></spring:escapeBody>'
				};
		</script>
		<lams:JSImport src="includes/javascript/common.js" />
 		<script type="text/javascript" src="${lams}includes/javascript/thickbox.js"></script>
 		<lams:JSImport src="includes/javascript/monitorToolSummaryAdvanced.js" />
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script> 
 		<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
   		<script type="text/javascript" src="${lams}includes/javascript/d3.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/chart.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/chart.bundle.min.js"></script>
 		<lams:JSImport src="includes/javascript/chart.js" relative="true" />
 		<script type="text/javascript" src="${lams}includes/javascript/jquery.cookie.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
 		<script type="text/javascript" src="${lams}includes/javascript/download.js"></script>
 		<lams:JSImport src="includes/javascript/portrait.js" />
 		<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.confidence-level-formattter.js"></script>
 		
		<c:if test="${not empty codeStyles}">
			<script type="text/javascript" src="${lams}includes/javascript/codemirror/addon/runmode/runmode-standalone.js"></script>
			<script type="text/javascript" src="${lams}includes/javascript/codemirror/addon/runmode/colorize.js"></script>
		</c:if>
		<%-- codeStyles is a set, so each code style will be listed only once --%>
		<c:forEach items="${codeStyles}" var="codeStyle">
			<c:choose>
				<c:when test="${codeStyle == 1}">
					<script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/python.js"></script>
				</c:when>
				<c:when test="${codeStyle == 2}">
					<script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/javascript.js"></script>
				</c:when>
				<c:when test="${codeStyle >= 3}">
					<script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/clike.js"></script>
				</c:when>
			</c:choose>
		</c:forEach>
		
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
						toolContentID: <c:out value="${param.toolContentID}" />, 
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
					<lams:TabBody id="1" page="summary.jsp" />
					<lams:TabBody id="2" page="editactivity.jsp" />			
					<lams:TabBody id="3" page="statistic.jsp" />
				</lams:TabBodys> 
			</lams:TabBodyArea> 
	
			<div id="footer"></div>
		</lams:Page>
	</body>
</lams:html>