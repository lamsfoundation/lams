<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.survey.SurveyConstants"%>

<c:set var="lams"><lams:LAMSURL/></c:set>

<lams:html>
	<lams:head>

	<lams:css/>
	
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/d3.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/chart.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>
	
	<!--  File Download -->
 	<script type="text/javascript" src="${lams}includes/javascript/jquery.cookie.js"></script>
 	<script type="text/javascript" src="${lams}includes/javascript/download.js"></script>
	
	<script type="text/javascript">
	//pass settings to monitorToolSummaryAdvanced.js
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${sessionMap.submissionDeadline}',
		submissionDateString: '${sessionMap.submissionDateString}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>?<csrf:token/>',
		toolContentID: '<c:out value="${param.toolContentID}" />',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};	
	</script>
	<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

	<link type="text/css" href="${lams}/css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
	<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet"> 
	<link type="text/css" href="${lams}/css/defaultHTML_chart.css" rel="stylesheet" />
		
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
	        
		    function viewItem(itemUid,sessionMapID){
				var myUrl = "<c:url value="/reviewItem.do"/>?mode=teacher&itemUid=" + itemUid + "&sessionMapID="+sessionMapID;
				launchPopup(myUrl,"MonitoringReview");
			}
	    </script>		 
	</lams:head>
	<body class="stripes" onLoad="init()">
	
	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	<lams:Page title="${title}" type="navbar">
	
		<lams:Tabs title="${title}" control="true" helpToolSignature="<%= SurveyConstants.TOOL_SIGNATURE %>" helpModule="monitoring">
			<lams:Tab id="1" key="monitoring.tab.summary" />
			<lams:Tab id="2" key="monitoring.tab.edit.activity" />			
			<lams:Tab id="3" key="monitoring.tab.statistics" />
		</lams:Tabs>
		
		<lams:TabBodyArea>
		<lams:TabBodys>
			<lams:TabBody id="1" page="summary.jsp" />
			<lams:TabBody id="2" page="editactivity.jsp" />			
			<lams:TabBody id="3" page="statistic.jsp" />
		</lams:TabBodys>
		</lams:TabBodyArea>
		
		<div id="footer" />
		
	</lams:Page>

	</body>
</lams:html>
