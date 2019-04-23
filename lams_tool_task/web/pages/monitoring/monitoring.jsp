<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.taskList.TaskListConstants"%>

<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		
<lams:html>
	<lams:head>
	
		<script type="text/javascript">
			//pass settings to monitorToolSummaryAdvanced.js
			var submissionDeadlineSettings = {
				lams: '${lams}',
				submissionDeadline: '${sessionMap.submissionDeadline}',
				submissionDateString: '${sessionMap.submissionDateString}',
				setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>',
				toolContentID: '${toolContentID}',
				messageNotification: '<fmt:message key="monitor.summary.notification" />',
				messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
				messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
			};
		</script>
		<%@ include file="/common/monitorheader.jsp" %>
		
		<script>
	        function init(){
	        	var initialTabId = "${initialTabId}";
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

	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	<lams:Page title="${title}" type="navbar">
	
	
		<lams:Tabs title="${title}" control="true" helpToolSignature="<%= TaskListConstants.TOOL_SIGNATURE %>" helpModule="monitoring">
			<lams:Tab id="1" key="monitoring.tab.summary" />
			<lams:Tab id="2" key="monitoring.tab.edit.activity" />			
			<lams:Tab id="3" key="monitoring.tab.statistics" />
		</lams:Tabs>
		
		<lams:TabBodyArea>
		<lams:TabBodys>
			<lams:TabBody id="1" titleKey="monitoring.tab.summary" page="summary.jsp" />
			<lams:TabBody id="2" titleKey="monitoring.tab.edit.activity" page="editactivity.jsp" />			
			<lams:TabBody id="3" titleKey="monitoring.tab.statistics" page="statistic.jsp" />
		</lams:TabBodys>
		</lams:TabBodyArea>
		
		<div id="footer" />
		
	</lams:Page>

	</body>
</lams:html>
