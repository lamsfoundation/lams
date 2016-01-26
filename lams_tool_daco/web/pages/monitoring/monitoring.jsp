<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.daco.DacoConstants"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/jqueryheader.jsp" %>
		
		<script>
			var currentTab = "${monitoringCurrentTab}";
		</script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/dacoMonitoring.js'/>"></script> 
		
		<title><fmt:message key="title.monitoring" /></title>
		
	</lams:head>
	<body id="body" class="stripes" onLoad="init()">
	<div id="page">
		<h1>
			<fmt:message key="label.common.heading" />
		</h1>
	<div id="header">
		<lams:Tabs control="true">
			<lams:Tab id="1" key="label.common.summary" />
			<lams:Tab id="2" key="tab.monitoring.edit.activity" />			
			<lams:Tab id="3" key="tab.monitoring.statistics" />
		</lams:Tabs>
	</div>
	<div id="content">
			<lams:help toolSignature="<%= DacoConstants.TOOL_SIGNATURE %>" module="monitoring"/>
			
			<lams:TabBody id="1" titleKey="label.common.summary" page="summary.jsp" />
			<lams:TabBody id="2" titleKey="tab.monitoring.edit.activity" page="editactivity.jsp" />			
			<lams:TabBody id="3" titleKey="tab.monitoring.statistics" page="statistics.jsp" />
	</div>
	<div id="footer"></div>
	
	</div>
</div>
</body>
</lams:html>
