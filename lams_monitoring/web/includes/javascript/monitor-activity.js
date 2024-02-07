function init() {
	if (INITIAL_TAB_ID) {
		selectTab(INITIAL_TAB_ID);
	} else {
		//selectTab(1); //select the default tab;
	}
}

function onSelectTab(tabId) {
	var tag = document.getElementById("currentTab");
	tag.value = tabId;

	//for statistic page change:
	if (tabId == 3) {
		doStatistic();
	}
}

function doStatistic() {
	$("#statisticArea_Busy").show();
	$.ajaxSetup({ cache: true });
	$("#statisticArea").load(
		MONITORING_STATISTIC_URL,
		{
			toolContentID: TOOL_CONTENT_ID,
			reqID: (new Date()).getTime()
		},
		function() {
			$("#statisticArea_Busy").hide();
		}
	);
}

function selectTab(tabID) {
	const triggerEl = document.querySelector('#tabs button#tab-' + tabID)
	bootstrap.Tab.getOrCreateInstance(triggerEl).show()
}
