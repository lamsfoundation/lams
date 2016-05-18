function doSelectTab(tabId) {
	if (tabId == 3) {
		$("#statisticArea").load(statisticsURL + (new Date()).getTime());
	}
	selectTab(tabId);
}