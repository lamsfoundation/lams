<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.notebook.util.NotebookConstants"%>
<lams:html>
	<lams:head>
		<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
		<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>
		<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
		
		<lams:css/>
		
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
		
		<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jinplace-1.0.1.js"></script>
		
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>
		
		<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/jquery.timeago.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
		
		<script type="text/javascript" src="${lams}includes/javascript/portrait.js"></script>
		
		<script type="text/javascript" src="${tool}includes/javascript/monitoring.js"></script>
	</lams:head>

	<body class="stripes">
		<script type="text/javascript">
		var statisticsURL = "<c:url value='/monitoring/getStatistics.do' />?toolContentID="+${notebookDTO.toolContentId}+"&reqID=";
		</script>

		<c:set var="title"><fmt:message key="activity.title" /></c:set>
		<lams:Page title="${title}" type="navbar">

			<lams:Tabs title="${title}" control="true" helpToolSignature="<%= NotebookConstants.TOOL_SIGNATURE %>" helpModule="monitoring">
				<lams:Tab id="1" key="button.summary" />
				<lams:Tab id="2" key="button.editActivity" />
				<lams:Tab id="3" key="button.statistics" />
			</lams:Tabs>
			<lams:TabBodyArea>
				<lams:TabBodys>
					<lams:TabBody id="1" titleKey="button.summary" page="summary.jsp" />
					<lams:TabBody id="2" titleKey="button.editActivity" page="editActivity.jsp" />
					<lams:TabBody id="3" titleKey="button.statistics" page="statistics.jsp" />
				</lams:TabBodys>
			</lams:TabBodyArea>
			
			<div id="footer"></div>

		</lams:Page>
		<div class="footer"></div>					
	</body>
</lams:html>
