<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.commonCartridge.CommonCartridgeConstants"%>

<lams:html>
<lams:head>
	<%@ include file="/common/tabbedheader.jsp" %>
		 
	<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
	<script>
		var initialTabId = "${initialTabId}";

		function init() {
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
	        
	    function viewItem(itemUid,sessionMapID) {
			var myUrl = "<c:url value="/reviewItem.do"/>?mode=teacher&itemUid=" + itemUid + "&sessionMapID="+sessionMapID;
			launchPopup(myUrl,"MonitoringReview");
		}
    </script>		 
</lams:head>

<body class="stripes" onLoad="init()">
	<c:set var="title"><fmt:message key="label.author.title" /></c:set>
	<lams:Page type="navbar" title="${title}"> 

		<lams:Tabs control="true" title="${title}" helpToolSignature="<%= CommonCartridgeConstants.TOOL_SIGNATURE %>" helpModule="monitoring" refreshOnClickAction="javascript:location.reload();">
			<lams:Tab id="1" key="monitoring.tab.summary"/>
			<lams:Tab id="2" key="monitoring.tab.edit.activity"/>
			<lams:Tab id="3" key="monitoring.tab.statistics"/>
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
