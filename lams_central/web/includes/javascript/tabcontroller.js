var selectedTabID = 0;
/* Initialise the number of tabs in the page */
function initTabSize(size) {
	alert("The funtion initTabSize is deprecated, please remove"); 
}

function selectTab(tabID) {
    if (selectedTabID == tabID) {
        return;
    }
    if (selectedTabID == 0) {
        selectedTabID = tabID;
    }
	
	// change the old tabs class
    document.getElementById("tab-left-" + selectedTabID).className = "tab-left";
    document.getElementById("tab-middle-" + selectedTabID).className = "tab-middle";
    document.getElementById("tab-right-" + selectedTabID).className = "tab-right";
    document.getElementById("tab-middle-link-" + selectedTabID).className = "tab-middle-link";

	// change the new tabs class
    document.getElementById("tab-left-" + tabID).className = "tab-left-selected";
    document.getElementById("tab-middle-" + tabID).className = "tab-middle-selected";
    document.getElementById("tab-right-" + tabID).className = "tab-right-selected";
    document.getElementById("tab-middle-link-" + tabID).className = "tab-middle-link-selected";
    
    //save tabID as selectedTabID
    selectedTabID = tabID;
    
    //switch the the selected tab on
    var i = 1;
	var tabbody = null;
	while(true) {
		var tabbody = document.getElementById("tabbody" + i);
		
		if (tabbody != null) {
			tabbody.style.display = (i == tabID) ? 'block':'none';
			i++;
		} else {
			break;
		}
	}

    try {
        //trigger the custom event listener onSelectTab()
        onSelectTab(tabID);
    }
    catch (error) {
        //catch reference error when onSelectTab() is not defined
    }
}

