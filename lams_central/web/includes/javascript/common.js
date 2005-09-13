<!-- Common Javascript functions for LAMS -->

	var instructionsWindow = null;
	/**
	 * Launches the popup window for the instruction files
	 */
	function launchInstructionsPopup(url) {
// add the mac test back in when we have the platform detection working.
//		if(mac){
//			window.open(url,'instructions','resizable,width=796,height=570,scrollbars');
//		}else{
			if(instructionsWindow && instructionsWindow.open && !instructionsWindow.closed){
				instructionsWindow.close();
			}
			instructionsWindow = window.open(url,'instructions','resizable,width=796,height=570,scrollbars');
			instructionsWindow.window.focus();
//		}	
	}