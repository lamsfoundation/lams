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
	
	function newFunction()
					{
							document.getElementById('eadventure').setAppletURL(url);
				}
