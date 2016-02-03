<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.notebook.util.NotebookConstants"%>

<div id="header">
	<lams:Tabs control="true">
		<lams:Tab id="1" key="button.summary" />
		<lams:Tab id="2" key="button.editActivity" />
		<lams:Tab id="3" key="button.statistics" />
	</lams:Tabs>
	
	<script type="text/javascript">
		var initialTabId = "${notebookDTO.currentTab}";
		
		function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
    		selectTab(tabId);
		    		
	    	//for statistic page change:
	    	if(tabId == 3)
	    		doStatistic();	  
        } 
	        
		function doStatistic(){
			var fudge = (new Date()).getTime();
			var url = "<c:url value="/monitoring.do"/>?dispatch=getStatistics&toolContentID=${notebookDTO.toolContentId}&reqID="+fudge;
			$("#statisticArea").load(url);
		}
			
	</script>
</div>

<div id="content">
	<lams:help toolSignature="<%= NotebookConstants.TOOL_SIGNATURE %>" module="monitoring"/>
	<input type="hidden" id="currentTab"/>
	
	<lams:TabBody id="1" titleKey="button.summary" page="summary.jsp" />
	<lams:TabBody id="2" titleKey="button.editActivity" page="editActivity.jsp" />
	<lams:TabBody id="3" titleKey="button.statistics" page="statistics.jsp" />
</div>

<div id="footer"></div>
