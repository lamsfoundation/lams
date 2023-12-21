function selectTab(tabID) {
    const triggerFirstTabEl = document.querySelector('#tabs button#tab-' + tabID)
	bootstrap.Tab.getInstance(triggerFirstTabEl).show()
    
    try {
        //trigger the custom event listener onSelectTab()
        onSelectTab(tabID);
    }
    catch (error) {
        //catch reference error when onSelectTab() is not defined
    }
}

function getCurrentTabID() {
	var activeTab = $("#tabs-content div.tab-pane.active");
	var id = activeTab.attr("id") // activated tab, should be "#t<num>"
	var activeTabID = id.substring(4,5);
	return activeTabID;
}
