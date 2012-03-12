
function init() {
	if (initialTabId) {
		selectTab(initialTabId);
	} else {
		selectTab(1);
	}
}
function doSelectTab(tabId) {
	selectTab(tabId);
}