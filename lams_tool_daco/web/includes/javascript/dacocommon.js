   
   // Finds a corresponding image and shows it
   function showBusy(targetDiv){
		if($(targetDiv+"_Busy") != null){
			$(targetDiv+"_Busy").show();
		}
	}
	
	// Finds a corresponding image and hides it
	function hideBusy(targetDiv){
		if($(targetDiv+"_Busy") != null){
			$(targetDiv+"_Busy").hide();
		}				
	}
	
	// Creates an hidden element and adds it to a JSON structure
	function addToJSON (arr, name, value){
		var elem = document.createElement('hidden');
	    elem.value=value;
	    elem.name=name;
	    arr[arr.length]=elem;
	}
	
	function checkCheckbox(checkboxName){
		var checkbox = document.getElementById(checkboxName);
		checkbox.checked=true;
	}
	
	function setValue(elementName,value){
		var element = document.getElementById(elementName);
		element.value=value;
	}