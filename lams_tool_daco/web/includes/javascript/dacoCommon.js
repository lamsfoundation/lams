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
	
	function resizeHorizontalRecordListFrame(elementIdPrefix,questionListLength){
		var horizontalRecordListFrame = document.getElementById(elementIdPrefix+'horizontalRecordListFrame');
	    var doc = horizontalRecordListFrame.contentDocument? horizontalRecordListFrame.contentDocument : horizontalRecordListFrame.contentWindow.document;
        var body = doc.body;
        var html = doc.documentElement;
        var height = Math.max( body.scrollHeight, body.offsetHeight, 
            html.clientHeight, html.scrollHeight, html.offsetHeight );
        horizontalRecordListFrame.style.height = height + 60 + "px";
	}
