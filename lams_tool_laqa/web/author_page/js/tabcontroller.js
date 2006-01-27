var tabSize = 0;
var selectedTabID = 0;

/* Initialise the number of tabs in the page */
function initTabSize(size){
    tabSize = size;
}

function selectTab(tabID) {
        
    if(selectedTabID == tabID)
        return;

    if(selectedTabID == 0)
        selectedTabID = tabID;
        
    //change the old tab's class
    document.getElementById("tab" + selectedTabID).className = "tabitem";
    
    //change the new tab's class
    document.getElementById("tab" + tabID).className = "tabitem_selected";    
    
    //save tabID as selectedTabID
    selectedTabID = tabID;
    
    //switch the the selected tab on
    for(i = 1; i <= tabSize; i++) {
      document.getElementById("tabbody" + i).style.display = (i == tabID) ? 'block':'none';
    }
    
    
    try{
        //trigger the custom event listener onSelectTab()
        onSelectTab(tabID);
    }
    catch (error){
        //catch reference error when onSelectTab() is not defined
    }
}