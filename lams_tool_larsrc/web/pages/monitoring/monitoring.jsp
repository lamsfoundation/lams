<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.rsrc.ResourceConstants"%>

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
			   // end optional tab controller stuff
			   selectTab(tabId);
		    } 
		        
			function viewItem(itemUid){
				var myUrl = "<c:url value="/reviewItem.do"/>?mode=teacher&itemUid=" + itemUid + "&sessionMapID=${sessionMapID}";
				launchPopup(myUrl,"MonitoringReview");
			}
			
			function viewComments(itemUid, toolSessionID){
				var myUrl = "<c:url value='/monitoring/viewComments.do'/>?mode=teacher&itemUid=" + itemUid + "&toolSessionID=" + toolSessionID + "&sessionMapID=${sessionMapID}";
				launchPopup(myUrl,"MonitoringReview");
			}
		</script>		 
	</lams:head>
	<body class="stripes" onLoad="init()">

	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	<lams:Page title="${title}" type="navbar">
	
		<lams:Tabs title="${title}" control="true" helpToolSignature="<%= ResourceConstants.TOOL_SIGNATURE %>" helpModule="monitoring">
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
