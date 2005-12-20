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
	
	function filterData(src,target){
		if(src.value != null){
			//replace specified string for characters that are sensitive to HTML interpreters
			var srcV = src.value;
			//must replace & first, otherwise, this will conflict with others.
			srcV = srcV.replace(/&/g,"&amp;");
			srcV = srcV.replace(/</g,"&lt;");
			srcV = srcV.replace(/>/g,"&gt;");
			srcV = srcV.replace(/"/g,"&quot;");
			srcV = srcV.replace(/\\/g,"&#39;");
			target.value = srcV;
			//for windows system
			if(src.value.indexOf("\r\n") != -1)
				target.value=srcV.replace(/\r\n/g,"<BR>");
			//for *nix system
			else if(src.value.indexOf("\n") != -1)
				target.value=srcV.replace(/\n/g,"<BR>");
			
		}
	}