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
    document.getElementById("tab" + selectedTabID).className = "tab tabcentre";
    //swap images of side parts
	var tl = document.getElementById("tableft_"+selectedTabID);
	tl.src= imgRoot + themeName + "_tab_left.gif";
	tl.height = 22;
	var tr = document.getElementById("tabright_"+selectedTabID);
	tr.src= imgRoot + themeName + "_tab_right.gif";
	tr.height = 22;
    
    //change the new tab's class
    document.getElementById("tab" + tabID).className = "tab tabcentre_selected";
    var tl = document.getElementById("tableft_"+tabID);
	tl.src= imgRoot + themeName + "_tab_s_left.gif";
	tl.height = 25;
	var tr = document.getElementById("tabright_"+tabID);
	tr.src= imgRoot + themeName + "_tab_s_right.gif";
	tr.height = 25;
    
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