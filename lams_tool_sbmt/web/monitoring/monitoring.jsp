<!DOCTYPE html>

<%@include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.sbmt.SbmtConstants"%>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<lams:css/>
	<link type="text/css" href="${lams}/css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
	<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">
	<link type="text/css" href="${lams}/css/jquery.tablesorter.theme.bootstrap5.css" rel="stylesheet">
	<link type="text/css" href="${lams}/css/jquery.tablesorter.pager5.css" rel="stylesheet">

	<lams:JSImport src="includes/javascript/common.js" />
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>

	<script type="text/javascript">
		function doSelectTab(tabId) {
	    	if (tabId == 3){
				doStatistic();
	    	}
    		selectTab(tabId);
        } 
		
		function doStatistic(){
			var url = "<c:url value="/monitoring.do"/>";
			
			$("#statisticArea_Busy").show();
			$.ajaxSetup({ cache: true });
			$("#statisticArea").load(
				url,
				{
					method: "doStatistic",
					toolContentID: '<c:out value="${param.toolContentID}" />', 
					reqID: (new Date()).getTime()
				},
				function() {
					$("#statisticArea_Busy").hide();
				}
			);
		}
	</script>
</lams:head>

<body class="stripes">

<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:Page title="${title}" type="navbar">

	<lams:Tabs title="${title}" control="true" helpToolSignature="<%= SbmtConstants.TOOL_SIGNATURE %>" helpModule="monitoring">
		<lams:Tab id="1" key="label.monitoring.heading.userlist" />
		<lams:Tab id="2" key="label.monitoring.heading.edit.activity" />
		<lams:Tab id="3" key="label.monitoring.heading.stats" />
	</lams:Tabs>

	<lams:TabBodyArea>
		<lams:TabBodys>
			<lams:TabBody id="1" page="parts/summary.jsp" />
			<lams:TabBody id="2" page="parts/activity.jsp" />
			<lams:TabBody id="3" page="parts/statisticpart.jsp" />
		</lams:TabBodys>
	</lams:TabBodyArea>
		
	<div id="footer"></div>

</lams:Page>
</body>
</lams:html>