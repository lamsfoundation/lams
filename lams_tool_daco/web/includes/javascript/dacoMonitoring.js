	function init(){
	 	if (currentTab) {
			selectTab(currentTab);
		} else {
			selectTab(1);
		}
	}   
	       
	function doSelectTab(tabId) {
		selectTab(tabId);
		currentTab = tabId;

    	//for statistic page change:
    	if(tabId == 3) {
    		doStatistic();
    	}
	}
	
	function doStatistic(){
		var url = gStatisticsUrl+"&reqID="+(new Date()).getTime();
		$("#statisticArea").load(url);
	} 
	
	function refreshPage() {
		var tabID = getCurrentTabID();
		if ( ! tabID ) {
			tabID = currentTab;
		}
		var param = {"reqID":((new Date()).getTime())};
		$( "#body" ).load( 
				gRefreshUrl, 
				param, 
				function() {
					doSelectTab(tabID);
					currentTab = tabID;
				});			
	}
