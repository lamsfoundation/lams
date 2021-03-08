<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants"%>

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

	    		//for statistic page change:
	    		if(tabId == 3) doStatistic();
	       	} 
	        
		    function viewItem(itemUid,sessionMapID){
				var myUrl = "<c:url value="/reviewItem.do"/>?mode=teacher&itemUid=" + itemUid + "&sessionMapID="+sessionMapID;
				launchPopup(myUrl,"MonitoringReview");
			}
			
			var statisticTargetDiv = "statisticArea";
			function doStatistic(){
				var url = "<c:url value="/monitoring/doStatistic.do"/>";
			    var reqIDVar = new Date();
				var param = "toolContentID=" + ${param.toolContentID};
				messageLoading();
				$.ajaxSetup({ cache: true });
				$("#"+statisticTargetDiv).load(
						url,
						param,
						function(data) {
							messageComplete();
						}
				);
			}
							
			function showBusy(targetDiv){
				if($(targetDiv+"_Busy") != null){
					$(targetDiv+"_Busy").show();
				}
			}
			function hideBusy(targetDiv){
				if($(targetDiv+"_Busy") != null){
					$(targetDiv+"_Busy").hide();
				}				
			}
			function messageLoading(){
				showBusy(statisticTargetDiv);
			}
			function messageComplete(){
				hideBusy(statisticTargetDiv);
			}        
			
	    </script>		 
	</lams:head>
	<body class="stripes" onLoad="init()">

	<c:set var="title"><fmt:message key="activity.title"/> ${lang}</c:set>
	<lams:Page title="${title}" type="navbar">

	 	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= SpreadsheetConstants.TOOL_SIGNATURE %>" helpModule="monitoring">
			<lams:Tab id="1" key="monitoring.tab.summary" />
			<lams:Tab id="2" key="monitoring.tab.edit.activity" />			
			<lams:Tab id="3" key="monitoring.tab.statistics" />
	    </lams:Tabs>   

		<lams:TabBodyArea>

			<!--  Set up tabs  -->
			<lams:TabBodys>
			<lams:TabBody id="1" page="summary.jsp" />
			<lams:TabBody id="2" page="editactivity.jsp" />			
			<lams:TabBody id="3" page="statistics.jsp" />
			</lams:TabBodys>
			
		</lams:TabBodyArea>

	<div id="footer"></div>
	</lams:Page>

	</body>
</lams:html>
