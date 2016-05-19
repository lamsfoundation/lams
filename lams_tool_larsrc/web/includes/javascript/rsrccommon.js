	function showBusy(targetDiv){
		var div = document.getElementById(targetDiv+"_Busy");
		if(div != null){
			document.getElementById(targetDiv+"_Busy").style.display = '';
		}
	}
	function hideBusy(targetDiv){
		var div = document.getElementById(targetDiv+"_Busy");
		if(div != null){
			document.getElementById(targetDiv+"_Busy").style.display = 'none';
		}				
	}