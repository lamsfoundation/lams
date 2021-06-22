<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.daco.DacoConstants"%>

<c:url var="refreshSummaryUrl" value="/monitoring/summary.do?sessionMapID=${sessionMapID}"/>
<c:url var="statisticsUrl" value="/monitoring/statistic.do?sessionMapID=${sessionMapID}"/>

<lams:html>
	<lams:head>
		<%@ include file="/common/jqueryheader.jsp" %>
		
		<script>
			var currentTab = "${monitoringCurrentTab}";
			var gStatisticsUrl = "${statisticsUrl}"; // used by the tab functions
			var gRefreshUrl = "${refreshSummaryUrl}"; // used by the tab functions
		</script>
		<lams:JSImport src="includes/javascript/dacoMonitoring.js" relative="true" /> 
		
		<title><fmt:message key="title.monitoring" /></title>

	</lams:head>
	<body id="body" class="stripes" onLoad="init()">
	<c:set var="title"><fmt:message key="label.common.heading" /></c:set>
 	<lams:Page type="navbar" title="${title}"> 

		<lams:Tabs control="true" title="${title}" helpToolSignature="<%= DacoConstants.TOOL_SIGNATURE %>" helpModule="monitoring" refreshOnClickAction="javascript:refreshPage()">
			<lams:Tab id="1" key="label.common.summary"/>
			<lams:Tab id="2" key="tab.monitoring.edit.activity"/>
			<lams:Tab id="3" key="tab.monitoring.statistics"/>
		</lams:Tabs>	

  		<lams:TabBodyArea>
			<lams:TabBodys>			
					<lams:TabBody id="1" page="summary.jsp"/>
					<lams:TabBody id="2" page="editactivity.jsp"/>	
					<lams:TabBody id="3" page="statistics.jsp"/>
			</lams:TabBodys> 
		</lams:TabBodyArea> 

	<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>
