	function init(){
	 	if (currentTab) {
			selectTab(currentTab);
		} else {
			selectTab(1);
		}
	}   
	       
	function doSelectTab(tabId) {
		selectTab(tabId);
		currentTab = tabId;
	}
	
	function refreshPage (url){
		var userDropdown=document.getElementById("userDropdown");
		var selectedValue = userDropdown.options[userDropdown.selectedIndex].value;
		if (selectedValue == "SHOW_ALL"){
	 		document.location.href = url;
	 	}
	 	else if (selectedValue >= 0){
	 		document.location.href = url+"&userUid="+selectedValue;
		}
	}