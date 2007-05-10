

// Flash Version Detector  v1.1.5
// http://www.dithered.com/javascript/flash_detect/index.html
// code by Chris Nott (chris@NOSPAMdithered.com - remove NOSPAM)
// with VBScript code from Alastair Hamilton

var flashVersion = 0;
function getFlashVersion() {
	var agent = navigator.userAgent.toLowerCase(); 
	
   // NS3 needs flashVersion to be a local variable
   if (agent.indexOf("mozilla/3") != -1 && agent.indexOf("msie") == -1) {
      flashVersion = 0;
   }
   
	// NS3+, Opera3+, IE5+ Mac (support plugin array):  check for Flash plugin in plugin array
	if (navigator.plugins != null && navigator.plugins.length > 0) {
		var flashPlugin = navigator.plugins['Shockwave Flash'];
		if (typeof flashPlugin == 'object') { 
			if (flashPlugin.description.indexOf('9.') != -1) flashVersion = 9;	// latest version added 
			if (flashPlugin.description.indexOf('8.') != -1) flashVersion = 8;	// newer version added
			if (flashPlugin.description.indexOf('7.') != -1) flashVersion = 7;
			else if (flashPlugin.description.indexOf('6.') != -1) flashVersion = 6;
			else if (flashPlugin.description.indexOf('5.') != -1) flashVersion = 5;
			else if (flashPlugin.description.indexOf('4.') != -1) flashVersion = 4;
			else if (flashPlugin.description.indexOf('3.') != -1) flashVersion = 3;
		}
	}

	// IE4+ Win32:  attempt to create an ActiveX object using VBScript
	else if (agent.indexOf("msie") != -1 && parseInt(navigator.appVersion) >= 4 && agent.indexOf("win")!=-1 && agent.indexOf("16bit")==-1) {
	   document.write('<scr' + 'ipt language="VBScript"\> \n');
		document.write('on error resume next \n');
		document.write('dim obFlash \n');
		document.write('set obFlash = CreateObject("ShockwaveFlash.ShockwaveFlash.9") \n');
		document.write('if IsObject(obFlash) then \n');
		document.write('flashVersion = 9 \n');
		document.write('else set obFlash = CreateObject("ShockwaveFlash.ShockwaveFlash.8") end if \n');
		document.write('if flashVersion < 9 and IsObject(obFlash) then \n');
		document.write('flashVersion = 8 \n');
		document.write('else set obFlash = CreateObject("ShockwaveFlash.ShockwaveFlash.7") end if \n');
		document.write('if flashVersion < 8 and IsObject(obFlash) then \n');
		document.write('flashVersion = 7 \n');
		document.write('else set obFlash = CreateObject("ShockwaveFlash.ShockwaveFlash.6") end if \n');
		document.write('if flashVersion < 7 and IsObject(obFlash) then \n');
		document.write('flashVersion = 6 \n');
		document.write('else set obFlash = CreateObject("ShockwaveFlash.ShockwaveFlash.5") end if \n');
		document.write('if flashVersion < 6 and IsObject(obFlash) then \n');
		document.write('flashVersion = 5 \n');
		document.write('else set obFlash = CreateObject("ShockwaveFlash.ShockwaveFlash.4") end if \n');
		document.write('if flashVersion < 5 and IsObject(obFlash) then \n');
		document.write('flashVersion = 4 \n');
		document.write('else set obFlash = CreateObject("ShockwaveFlash.ShockwaveFlash.3") end if \n');
		document.write('if flashVersion < 4 and IsObject(obFlash) then \n');
		document.write('flashVersion = 3 \n');
		document.write('end if');
		document.write('</scr' + 'ipt\> \n');
  }
		
	// WebTV 2.5 supports flash 3
	else if (agent.indexOf("webtv/2.5") != -1) flashVersion = 3;

	// older WebTV supports flash 2
	else if (agent.indexOf("webtv") != -1) flashVersion = 2;

	// Can't detect in all other cases
	else {
		flashVersion = flashVersion_DONTKNOW;
	}

	return flashVersion;
}

flashVersion_DONTKNOW = -1;
