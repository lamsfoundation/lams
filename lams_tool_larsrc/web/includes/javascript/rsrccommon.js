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
	function disableButtons() {
		$('.btn-disable-on-submit').prop('disabled', true);
		$("#saveCancelButtons .btn").prop('disabled', true);
	}
	function enableButtons() {
		$('.btn-disable-on-submit').prop('disabled', false);
		$("#saveCancelButtons .btn").prop('disabled', false);
	}

