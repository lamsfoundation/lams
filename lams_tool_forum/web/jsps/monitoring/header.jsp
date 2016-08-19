<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

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