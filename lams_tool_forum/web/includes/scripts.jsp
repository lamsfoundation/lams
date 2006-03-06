<%@ include file="/includes/taglibs.jsp" %>
<!--

	function closeWin(){
		window.opener.parent.location.href = "<html:rewrite page='/authoring/finishTopic.do'/>";
		window.close();
	}
	function launchPopup(url) {
		var instructionsWindow = null;
// add the mac test back in when we have the platform detection working.
//		if(mac){
//			window.open(url,'instructions','resizable,width=796,height=570,scrollbars');
//		}else{
			if(instructionsWindow && instructionsWindow.open && !instructionsWindow.closed){
				instructionsWindow.close();
			}
			instructionsWindow = window.open(url,'newtopic','resizable,width=796,height=570,scrollbars');
			instructionsWindow.window.focus();
//		}	
	}
//-->
