<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.pixlr.util.PixlrConstants"%>
<lams:html>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>

	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:headItems />
		<c:set var="tool">
			<lams:WebAppURL />
		</c:set>
		<script type="text/javascript" src="${tool}includes/javascript/monitoring.js"></script>
		<script type="text/javascript" src="${tool}includes/javascript/common.js"></script>
	</lams:head>

	<body class="stripes" onload="init();">
	
		<lams:Page title='<fmt:message key="pageTitle.monitoring" />' type="navbar">
			<script type="text/javascript">
				var initialTabId = "${pixlrDTO.currentTab}";
			</script>
			
			<c:set var="title"><fmt:message key="activity.title" /></c:set>
			<lams:Tabs control="true" title="${title}" helpToolSignature="<%= PixlrConstants.TOOL_SIGNATURE %>" helpModule="monitoring" refreshOnClickAction="javascript:location.reload();">
				<lams:Tab id="1" key="button.summary"/>
				<lams:Tab id="2" key="button.editActivity"/>
				<lams:Tab id="3" key="button.statistics"/>
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


