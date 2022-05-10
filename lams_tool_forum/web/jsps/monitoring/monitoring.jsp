<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.forum.ForumConstants"%>
<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>
		<%@ include file="/common/tabbedheader.jsp"%>
		<c:set var="tool"><lams:WebAppURL /></c:set>
		<c:set var="lams"><lams:LAMSURL /></c:set>
		
				<script type="text/javascript">
		    var initialTabId = "${initialTabId}";
		     
			function init(){
				if (initialTabId) {
					selectTab(initialTabId);
				} else {
					selectTab(1);
				}
			}  
		        
			function doSelectTab(tabId) {
				var tag = document.getElementById("currentTab");
				tag.value = tabId;
				selectTab(tabId);
				
		    	//for statistic page change:
		    	if(tabId == 3) {
		    		doStatistic();
		    	}
			}
			
			function doStatistic(){
				var url = "<c:url value="/monitoring/statistic.do?sessionMapID=${sessionMapID}"/>";
				
				$("#statisticArea_Busy").show();
				$.ajaxSetup({ cache: true });
				$("#statisticArea").load(
					url,
					{
						toolContentID: ${param.toolContentID}, 
						reqID: (new Date()).getTime()
					},
					function() {
						$("#statisticArea_Busy").hide();
					}
				);
				
			}       
		</script>
	</lams:head>

	<body class="stripes" onLoad="init()">
	
		<input type="hidden" name="currentTab" id="currentTab" />

		<c:set var="title"><fmt:message key="activity.title" /></c:set>
		<lams:Page title="${title}" type="navbar">
		
			<lams:Tabs title="${title}" control="true" helpToolSignature="<%= ForumConstants.TOOL_SIGNATURE %>" helpModule="monitoring">
				<lams:Tab id="1" key="monitoring.tab.summary" />
				<lams:Tab id="2" key="monitoring.tab.edit.activity" />
				<lams:Tab id="3" key="monitoring.tab.statistics" />
			</lams:Tabs>
			
			<lams:TabBodyArea>
			<lams:TabBodys>
			<lams:TabBody id="1" page="summary.jsp" />
			<lams:TabBody id="2" page="editactivity.jsp" />
			<lams:TabBody id="3" page="statistic.jsp" />
			</lams:TabBodys>
			</lams:TabBodyArea>
			
			<div id="footer" />
			
		</lams:Page>
	</body>
</lams:html>

