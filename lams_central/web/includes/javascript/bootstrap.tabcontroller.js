var selectedTabID = "0";

function selectTab(tabID) {
    //save tabID as selectedTabID
    selectedTabID = tabID;
    $('#'+tabID).tab('show'); // this line does not work! no errors just no show!
    
    try {
        //trigger the custom event listener onSelectTab()
        onSelectTab(tabID);
    }
    catch (error) {
        //catch reference error when onSelectTab() is not defined
    }
}

