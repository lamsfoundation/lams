/**
 * Launches the popup window for the instruction files
 */
function showMessage(url) {
	$.ajaxSetup({ cache: false });
	$("#new-image-input-area").load(url, function() {
		$(this).show();
		$("#saveCancelButtons").hide();
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
