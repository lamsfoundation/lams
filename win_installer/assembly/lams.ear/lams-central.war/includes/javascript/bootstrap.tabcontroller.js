function selectTab(tabID) {
	var tabFilter = '#page-tabs a[href="#t'+tabID+'"]';
    $(tabFilter).tab('show');
    
    try {
        //trigger the custom event listener onSelectTab()
        onSelectTab(tabID);
    }
    catch (error) {
        //catch reference error when onSelectTab() is not defined
    }
}

function getCurrentTabID() {
	var activeTab = $("ul#page-tabs li.active a");
	var href = activeTab.attr("href") // activated tab, should be "#t<num>"
	var activeTabID = href.substring(2);
	return activeTabID;
}
