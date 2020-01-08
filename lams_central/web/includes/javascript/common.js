/**
 * Launches the popup window for the instruction files
 */
var instructionsWindow = null;
function launchInstructionsPopup(url) {
// add the mac test back in when we have the platform detection working.
//		if(mac){
//			window.open(url,'instructions','resizable,width=796,height=570,scrollbars');
//		}else{
			if(instructionsWindow && instructionsWindow.open && !instructionsWindow.closed){
				instructionsWindow.close();
			}
			instructionsWindow = window.open(url,'instructions','resizable,width=1152,height=648,scrollbars');
			instructionsWindow.window.focus();
//		}	
}

function launchPopup(url, title) {
	var wd = null;
	if(wd && wd.open && !wd.closed){
		wd.close();
	}
	var left = ((screen.width / 2) - (1152 / 2)),
		// open the window a bit higher than center
		top = ((screen.height / 2) - (648 / 2)) / 2;
	wd = window.open(url,title,'resizable,width=1152,height=648,scrollbars,top=' + top + ',left=' + left);
	wd.window.focus();
}

function launchDefineLaterPopup() {
	launchPopup('about:blank', 'definelater');
	document.getElementById('define-later-form').submit();
}
	
function filterData(src,target){
	if(src.value != null) {
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
		if (src.value.indexOf("\r\n") != -1) {
			target.value=srcV.replace(/\r\n/g,"<BR>");
		//for *nix system
		} else if (src.value.indexOf("\n") != -1) {
			target.value=srcV.replace(/\n/g,"<BR>");
		}
	}
}
function trimAll( strValue ) {
	 var replaceExpr = /^(\s*)$/;

    //check for all spaces
    if(replaceExpr.test(strValue)) {
       strValue = strValue.replace(replaceExpr, '');
       if( strValue.length == 0) {
          return strValue;
       }
    }
	
   //check for leading & trailing spaces
   replaceExpr = /^(\s*)([\W\w]*)(\b\s*$)/;
   if(replaceExpr.test(strValue)) {
       //remove leading and trailing whitespace characters
       strValue = strValue.replace(replaceExpr, '$2');
    }
   
	return strValue;
}
	
function isEmpty( strValue ) {
	
   var strTemp = strValue;
   strTemp = trimAll(strTemp);
   if (strTemp.length > 0) {
     return false;
   }
   return true;
}
	
// refresh the parent window if the parent window is a tool monitoring window
function refreshParentMonitoringWindow() {
	if ( window.opener && ! window.opener.closed && window.opener.name.indexOf("MonitorActivity") >= 0 ) {
		var monitoringURL = String(window.opener.location);
		var currentTab = window.opener.selectedTabID;
		if ( currentTab ) {
			var indexStart = monitoringURL.indexOf("&currentTab=");
			if ( indexStart != -1 ) {
				var indexEnd = monitoringURL.indexOf("&",indexStart+1);
				if ( indexEnd == -1 ) {
					monitoringURL = monitoringURL.substring(0,indexStart);
				} else {
					monitoringURL = monitoringURL.substring(0,indexStart)+monitoringURL.substring(indexEnd);
				}
			}
			monitoringURL = monitoringURL+"&currentTab="+currentTab;
		}
		window.opener.location.href = monitoringURL;
	}
}
	
//does Ajax call (pure JavaScript without relying on frameworks)
function doAjaxCall(url) {
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
   	}
   	xmlhttp.open("POST", url, false);
   	xmlhttp.send();
}
    
function getNumberOfWords(value, isRemoveHtmlTags) {
    	
	//HTML tags stripping 
	if (isRemoveHtmlTags) {
		value = value.replace(/&nbsp;/g, '').replace(/<\/?[a-z][^>]*>/gi, '');
	}
	value = value.trim();
    	
	var wordCount = value ? (value.replace(/['";:,.?\-!]+/g, '').match(/\S+/g) || []).length : 0;
	return wordCount;
}
	