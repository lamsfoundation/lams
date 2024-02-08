<!DOCTYPE html>
<%@ page import="org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionConstants"%>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>

<lams:html>
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:headItems />
		
		<link type="text/css" href="${lams}css/thickbox.css" rel="stylesheet"  media="screen">
		<link type="text/css" href="${lams}/css/jquery-ui-bootstrap-theme5.css" rel="stylesheet">
		<style media="screen,projection" type="text/css">
			.bottom-buttons {margin: 20px 20px 0px; padding-bottom: 10px;}
			table {padding: 10px;}
		</style>
		
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/thickbox.js"></script>
		<lams:JSImport src="includes/javascript/portrait5.js" />
		<script type="text/javascript" src="${tool}includes/javascript/monitoring.js"></script>
	</lams:head>

	<body class="stripes" onload="init();">
		<c:set var="title"><fmt:message key="activity.title" /></c:set>
		<lams:Page title="${title}" type="navbar">
	
		<lams:Tabs title="${title}" control="true" helpToolSignature="<%= LeaderselectionConstants.TOOL_SIGNATURE %>" helpModule="monitoring">
			<lams:Tab id="1" key="button.summary" />
			<lams:Tab id="2" key="button.editActivity" />
			<lams:Tab id="3" key="button.statistics" />
		</lams:Tabs>

		<script type="text/javascript">
			var initialTabId = "${leaderselectionDTO.currentTab}";
		</script>
	
		<lams:TabBodyArea>
			<lams:TabBodys>
				<lams:TabBody id="1" page="summary.jsp" />
				<lams:TabBody id="2" page="editActivity.jsp" />
				<lams:TabBody id="3" page="statistics.jsp" />
			</lams:TabBodys>
		</lams:TabBodyArea>
		
		<div id="footer" />
	
		</lams:Page>
	</body>
</lams:html>