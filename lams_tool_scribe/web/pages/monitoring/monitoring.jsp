<!DOCTYPE html>
            
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.scribe.util.ScribeConstants"%>

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
		
		<script type="text/javascript"
			src="${tool}includes/javascript/monitoring.js">
		</script>
	</lams:head>
	<body class="stripes" onload="init();">
		<c:set var="title"><fmt:message key="activity.title" /></c:set>
		<lams:Page title="${title}" type="navbar">
			<lams:Tabs title="${title}" control="true" helpToolSignature="<%= ScribeConstants.TOOL_SIGNATURE %>" helpModule="monitoring">
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


