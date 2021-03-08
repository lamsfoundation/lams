<!DOCTYPE html>
        
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<lams:html>
	<lams:head>
		<%@ include file="/common/tabbedheader.jsp" %>
		 
		<script>
			var initialTabId = "${initialTabId}";
	
			function init(){
				if (initialTabId) {
					selectTab(initialTabId);
				} else {
					selectTab(1);
				}
		    }     
		        
		    function doSelectTab(tabId) {
			   if ( tabId == 3) {
				   doStatistic();
			   }
			   selectTab(tabId);
		    } 
		        
			function doStatistic(){
				var url = "<c:url value="/monitoring/statistic.do?sessionMapID=${sessionMapID}"/>";
				$("#statisticArea_Busy").show();
				$.ajaxSetup({ cache: true });
				$("#statisticArea").load(
					url,
					{
						toolContentID: ${sessionMap.toolContentID}, 
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
		<c:set var="title"><fmt:message key="activity.title" /></c:set>	
        <lams:Page title="${title}" type="navbar">

        	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= PeerreviewConstants.TOOL_SIGNATURE %>" helpModule="monitoring">		
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
			
			<div id="footer"></div>
	
		</lams:Page>
	</body>
</lams:html>
