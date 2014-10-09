function showBusy(targetDiv){
	if($(targetDiv+"_Busy") != null){
		Element.show(targetDiv+"_Busy");
	}
}
function hideBusy(targetDiv){
	if($(targetDiv+"_Busy") != null){
		Element.hide(targetDiv+"_Busy");
	}				
}
