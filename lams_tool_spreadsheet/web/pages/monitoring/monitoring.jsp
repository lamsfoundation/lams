<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

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
			    var myAjax = new Ajax.Updater(
				    	statisticTargetDiv,
				    	url,
				    	{
				    		method:'get',
				    		parameters:param,
				    		onComplete:messageComplete,
				    		evalScripts:true
				    	}
			    );
			}
		
			function showBusy(targetDiv){
				if($(targetDiv+"_Busy") != null){
					Element.show(targetDiv+"_Busy");
				}
			}
			function hideBusy(targetDiv){
				if($(targetDiv+"_Busy") != null){
					Element.hide(targetDiv+"_Busy");
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
	<div id="page">
		<h1>
			<fmt:message key="label.authoring.heading" />
		</h1>
	<div id="header">
		<lams:Tabs useKey="true" control="true">
			<lams:Tab id="1" key="monitoring.tab.summary" />
			<lams:Tab id="2" key="monitoring.tab.edit.activity" />			
			<lams:Tab id="3" key="monitoring.tab.statistics" />
		</lams:Tabs>
	</div>
	<div id="content">
			<lams:help toolSignature="<%= SpreadsheetConstants.TOOL_SIGNATURE %>" module="monitoring"/>
	
			<lams:TabBody id="1" titleKey="monitoring.tab.summary" page="summary.jsp" />
			<lams:TabBody id="2" titleKey="monitoring.tab.edit.activity" page="editactivity.jsp" />			
			<lams:TabBody id="3" titleKey="monitoring.tab.statistics" page="statistics.jsp" />
	</div>
	<div id="footer"></div>
	
	</div>
	</body>
</lams:html>
