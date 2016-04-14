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
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/dacoMonitoring.js'/>"></script> 
		
		<title><fmt:message key="title.monitoring" /></title>

	</lams:head>
	<body id="body" class="stripes" onLoad="init()">
	<c:set var="title"><fmt:message key="label.common.heading" /></c:set>
 	<lams:Page type="navbar" title="${title}"> 

		<lams:HybridTabHeader control="true" title="${title}" helpToolSignature="<%= DacoConstants.TOOL_SIGNATURE %>" helpModule="monitoring" refreshOnClickAction="javascript:refreshPage()">
			<lams:HybridTab id="1" key="label.common.summary"/>
			<lams:HybridTab id="2" key="tab.monitoring.edit.activity"/>
			<lams:HybridTab id="3" key="tab.monitoring.statistics"/>
		</lams:HybridTabHeader>	

<%-- 			<div class="panel panel-default panel-monitor-page">
			<div class="panel-heading navbar-heading">
			
 			 	<nav class="navbar navbar-default navbar-heading">
				<div class="container-fluid">
			    	<!-- Brand and toggle get grouped for better mobile display -->
					<div class="navbar-header">
				      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse-1" aria-expanded="false">
				        <span class="sr-only">Toggle navigation</span>
				        <span class="icon-bar"></span>
				        <span class="icon-bar"></span>
				        <span class="icon-bar"></span>
				      </button>
				      <span class="navbar-brand">${title}</span>
					</div>
					<div class="collapse navbar-collapse" id="navbar-collapse-1" role="navigation">
			         <ul class="nav navbar-nav" id="page-tabs">
	 			         <li role="separator" class="divider"></li>
						 <li class="active"><a href="#t1"  data-toggle="tab"><i class="fa fa-th-list"></i> <fmt:message key="label.common.summary" /></a></li>
			             <li><a href="#t2"  data-toggle="tab"><i class="fa fa-pencil"></i> <fmt:message key="tab.monitoring.edit.activity" /></a></li>
			             <li><a href="#t3"  data-toggle="tab"><i class="fa fa-area-chart"></i> <fmt:message key="tab.monitoring.statistics" /></a></li>
			         </ul>
			         <ul class="nav navbar-nav navbar-right" id="page-controls">
	 			         <li role="separator" class="divider"></li>
			             <li class="navbar-text" ><span onclick="javascript:refreshPage()"><i class="fa fa-refresh"></i> 	<fmt:message key="label.common.summary.refresh" /></span></li>
 			             <li class="navbar-text" ><lams:help toolSignature="<%= DacoConstants.TOOL_SIGNATURE %>" module="monitoring" /></li>
			         </ul>
			 	    </div>
				 </div>
			     </nav>
			     <!-- /top nav -->
			</div>
			
 --%>
 
  		<lams:HybridTabBody>
			<lams:TabBodys>			
					<lams:TabBody id="1" titleKey="label.common.summary" page="summary.jsp"/>
					<lams:TabBody id="2" titleKey="tab.monitoring.edit.activity" page="editactivity.jsp"/>	
					<lams:TabBody id="3" titleKey="tab.monitoring.statistics" page="statistics.jsp"/>
			</lams:TabBodys> 
		</lams:HybridTabBody> 
				
<!-- 			<div class="panel-body panel-monitor-body">
 -->
<%-- 	<lams:Tabs control="true">
		<lams:Tab id="1" key="label.common.summary" />
		<lams:Tab id="2" key="tab.monitoring.edit.activity" />			
		<lams:Tab id="3" key="tab.monitoring.statistics" />
	</lams:Tabs> --%>

	<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>
