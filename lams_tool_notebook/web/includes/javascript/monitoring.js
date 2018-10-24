function doSelectTab(tabId) {
	if (tabId == 3) {
		 $.ajaxSetup({ cache: true });
		$("#statisticArea").load(statisticsURL + (new Date()).getTime());
	}
	selectTab(tabId);
}