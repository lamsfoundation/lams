/**
 * Launches the popup window for the instruction files
 */
function showMessage(url) {
	$.ajaxSetup({ cache: true });
	$("#reourceInputArea").load(url, function() {
		var area=document.getElementById("reourceInputArea");
		if (area != null) {
			area.style.width="100%";
			area.style.height="100%";
			area.style.display="block";
		}
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="none";
		}
	});
}
	
function showBusy(targetDiv){
	if($(targetDiv+"_Busy") != null){
		document.getElementById(targetDiv+"_Busy").style.display = '';
	}
}

function hideBusy(targetDiv){
	if($(targetDiv+"_Busy") != null){
		document.getElementById(targetDiv+"_Busy").style.display = 'none';
	}				
}
