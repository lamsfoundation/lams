<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.mc.McAppConstants"%>

<lams:html>
<lams:head>

	<title><fmt:message key="label.monitoring"/></title>
	
	<%@ include file="/common/monitoringheader.jsp"%>
	<lams:css suffix="chart"/>

	<script type="text/javascript">
	
        function init(){
			selectTab(1); //select the default tab;
        }     
        
        function doSelectTab(tabId) {
		    	// end optional tab controller stuff
		    	if ( tabId == 3 ) {
		    		doStatistic();
		    	}
		    	selectTab(tabId);
        }

		function doStatistic(){
			var url = '<c:url value="/monitoring/statistic.do"/>';
			$.ajaxSetup({ cache: true });
			$("#statisticArea").load(
				url,
				{
					toolContentID: ${mcGeneralMonitoringDTO.toolContentID}, 
					reqID: (new Date()).getTime()
				}
			);
		}       

		function downloadMarks() {
			var url = "<c:url value='/monitoring/downloadMarks.do'/>";
		    var reqIDVar = new Date();
			var param = "?<csrf:token/>&toolContentID=${mcGeneralMonitoringDTO.toolContentID}&reqID="+reqIDVar.getTime();
			url = url + param;

			return downloadFile(url, 'message-area-busy', '<fmt:message key="label.summary.downloaded"/>', 'message-area', 'btn-disable-on-submit');
		}
		
		function turnOnDisplayAnswers() {
			window.open("<c:url value='/monitoring/displayAnswers.do'/>?toolContentID=${mcGeneralMonitoringDTO.toolContentID}&contentFolderID=${contentFolderID}","_self")
		}

		function turnOnDisplayFeedbackOnly() {
			window.open("<c:url value='/monitoring/displayFeedbackOnly.do'/>?toolContentID=${mcGeneralMonitoringDTO.toolContentID}&contentFolderID=${contentFolderID}","_self")
		}

	</script>
	
</lams:head>
<body onLoad="init();" class="stripes">

<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:Page title="${title}" type="navbar">

	<lams:Tabs title="${title}" control="true" helpToolSignature="<%= McAppConstants.TOOL_SIGNATURE %>" helpModule="monitoring" refreshOnClickAction="javascript:location.reload();">
		<lams:Tab id="1" key="label.summary" />
		<lams:Tab id="2" key="label.editActivity" />
		<lams:Tab id="3" key="label.stats" />
	</lams:Tabs>
	
	<lams:TabBodyArea>
		<lams:TabBodys>
			<lams:TabBody id="1" page="SummaryContent.jsp"/>
			<lams:TabBody id="2" page="Edit.jsp" />
			<lams:TabBody id="3" page="Stats.jsp" />
		</lams:TabBodys>
	</lams:TabBodyArea>
	
	<div id="footer" />
	
</lams:Page>

</body>
</lams:html>
