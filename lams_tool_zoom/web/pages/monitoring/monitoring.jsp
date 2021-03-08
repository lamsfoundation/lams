<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.zoom.util.ZoomConstants"%>

<lams:html>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<lams:head>
	<title><fmt:message key="activity.title" /></title>
	
	<lams:css/>
	
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/monitoring.js"></script>
	<script type="text/javascript">
		var initialTabId = "${contentDTO.currentTab}";
	</script>
</lams:head>
<body class="stripes" onload="init();">

	<lams:Page title="pageTitle.monitoring" type="navbar">
		<c:set var="title">
			<fmt:message key="activity.title" />
		</c:set>
		
		<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ZoomConstants.TOOL_SIGNATURE %>" helpModule="monitoring">
			<lams:Tab id="1" key="button.summary" />
			<lams:Tab id="2" key="button.editActivity" />
			<lams:Tab id="3" key="button.statistics" />
		</lams:Tabs>
		
		<lams:TabBodyArea>
			<lams:TabBodys>
				<lams:TabBody id="1" page="summary.jsp" />
				<lams:TabBody id="2" page="editActivity.jsp" />
				<lams:TabBody id="3" page="statistics.jsp" />
			</lams:TabBodys>
		</lams:TabBodyArea>
		
		<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>