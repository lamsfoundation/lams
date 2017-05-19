<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.mc.McAppConstants"%>

<lams:html>
<lams:head>

	<title><fmt:message key="label.monitoring"/></title>
	
	<%@ include file="/common/monitoringheader.jsp"%>
	<script type="text/javascript">
	
        function init(){
            var tag = "${mcGeneralMonitoringDTO.currentTab}";
	    	if(tag != "")
	    		selectTab(tag);
            else
                selectTab(1); //select the default tab;
        }     
        
        function doSelectTab(tabId) {
	    	// end optional tab controller stuff
	    	selectTab(tabId);
        }

		function downloadMarks() {
			var url = "<c:url value='/monitoring.do'/>";
		    var reqIDVar = new Date();
			var param = "?validate=false&dispatch=downloadMarks&toolContentID=${mcGeneralMonitoringDTO.toolContentID}&reqID="+reqIDVar.getTime();
			url = url + param;

			return downloadFile(url, 'message-area-busy', '<fmt:message key="label.summary.downloaded"/>', 'message-area', 'btn-disable-on-submit');
		}
		
		function turnOnDisplayAnswers() {
			window.open("<c:url value='/monitoring.do'/>?dispatch=displayAnswers&toolContentID=${mcGeneralMonitoringDTO.toolContentID}&contentFolderID=${contentFolderID}","_self")
		}
	
	</script>
	
</lams:head>
<body onLoad="init();" class="stripes">

<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:Page title="${title}" type="navbar">

	<lams:Tabs title="${title}" control="true" helpToolSignature="<%= McAppConstants.MY_SIGNATURE %>" helpModule="monitoring">
		<lams:Tab id="1" key="label.summary" />
		<lams:Tab id="2" key="label.editActivity" />
		<lams:Tab id="3" key="label.stats" />
	</lams:Tabs>
	
	<lams:TabBodyArea>
		<lams:TabBodys>
			<lams:TabBody id="1" titleKey="label.summary" page="SummaryContent.jsp"/>
			<lams:TabBody id="2" titleKey="label.editActivity" page="Edit.jsp" />
			<lams:TabBody id="3" titleKey="label.stats" page="Stats.jsp" />
		</lams:TabBodys>
	</lams:TabBodyArea>
	
	<div id="footer" />
	
</lams:Page>

</body>
</lams:html>
