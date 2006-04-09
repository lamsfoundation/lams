<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration" import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.themes.dto.CSSThemeBriefDTO" %>
<HTML>
<HEAD>
<meta http-equiv=Content-Type content="text/html; charset=iso-8859-1">

<%
String protocol = request.getProtocol();
if(protocol.startsWith("HTTPS")){
	protocol = "https://";
}else{
	protocol = "http://";
}
String pathToRoot = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
String pathToShare = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/../..";

String authoringClientVersion = Configuration.get(ConfigurationKeys.AUTHORING_CLIENT_VERSION);
String serverLanguage = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE);
String languageDate = Configuration.getDictionaryDateForLanguage(serverLanguage);

%>

<script language="JavaScript" type="text/JavaScript">
<!--

//browser detect:
var ns = (document.layers)? true: false
var ie = (document.all)? true: false
var b = (ns)? "Netscape": (ie) ?"Explorer" : " Sorry, LAMS does not support your browser.  Please contact technical.support@lamsinternational.com "
var platformDetected = "<%=session.getAttribute("lamsPlatformDetected")%>";
var mac = platformDetected.indexOf('mac') != -1;

//flag to show if the content (LD) in the flash UI has changed.
var saved = true;
var thePopUp = null;
var theFilePopUp = null;

var previewSessionId = null;

function getWindowSize() {
  var myWidth = 0, myHeight = 0;
  if( typeof( window.innerWidth ) == 'number' ) {
    //Non-IE
    myWidth = window.innerWidth;
    myHeight = window.innerHeight;
  } else {
    if( document.documentElement &&
        ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
      //IE 6+ in 'standards compliant mode'
      myWidth = document.documentElement.clientWidth;
      myHeight = document.documentElement.clientHeight;
    } else {
      if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
        //IE 4 compatible
        myWidth = document.body.clientWidth;
        myHeight = document.body.clientHeight;
      }
    }
  }

  var r = {w:myWidth, h:myHeight};

  return r;
}

var isInternetExplorer = navigator.appName.indexOf("Microsoft") != -1;
function authoring_DoFSCommand(command, args) {
	//alert("command:"+command+","+args);
	var authoringObj = isInternetExplorer ? document.all.authoring : document.authoring;
	if (command == "alert") {
		doAlert(args);
	}else if (command == "confirm"){
		doConfirm(args);
	}else if (command == "openPopUp"){
		openPopUp(args);
	}else if(command == "openFilePopUp"){
		openFilePopUp(args);
	}else if (command == "setSaved"){
		setSaved(args);
	}else if (command == "closeUI"){
		closeUI();
	}else if(command == "openPreview"){
		//this is called for Preview - to launch the learner UI. args will contain the sessionId to open
		openPreview(args);
	}else if(command == "closeWindow"){
		closeWindow(args);
	}

}

// Hook for Internet Explorer.
if (navigator.appName && navigator.appName.indexOf("Microsoft") != -1 && navigator.userAgent.indexOf("Windows") != -1 && navigator.userAgent.indexOf("Windows 3.1") == -1) {
	document.write('<script language=\"VBScript\"\>\n');
	document.write('On Error Resume Next\n');
	document.write('Sub authoring_FSCommand(ByVal command, ByVal args)\n');
	document.write('	Call authoring_DoFSCommand(command, args)\n');
	document.write('End Sub\n');
	document.write('</script\>\n');
}

function doAlert(arg){
	alert(arg);
}

function doConfirm(arg){
	var answer = confirm (arg)
	if (answer)
		alert ("Oh yeah?")
	else
		alert ("Why not?")
}

function openPopUp(args, title, h, w, resize, status, scrollbar){
	if(thePopUp && thePopUp.open && !thePopUp.closed){
			thePopUp.focus();
			
	}else{
		//mozilla seems to want a full url
		
		var args = getHostURL()+args;
		thePopUp = window.open(args,title,"HEIGHT="+h+",WIDTH="+w+",resizable="+resize+",scrollbars="+scrollbar+",status="+status+"");
		//thePopUp = window.open(args,"learnerPop","HEIGHT=450,WIDTH=550,resizable,scrollbars");
	}
}


function closeWindow(win){
	if(win == 'previewWin'){
		previewWin.close();
	}


}

var learnWin = null;
/*function openPreview(sessionId)	{
	//debug: To check we are gettin a session Id to open
	var welcomeLearnerOpen = false;
	previewSessionId = sessionId;
	var url = getHostURL() + '/lams/home.do?method=learner&sessionId='+sessionId;
	//var url = 'home.do?method=learner&sessionId='+sessionId;
	//alert('Opening Preview, url='+url);


	learnWin = window.open(url,'lWindow','width=796,height=570,resizable,status=yes');
	learnWin.focus();


}*/


function openFilePopUp(args){
	//mozilla seems to want a full url
	//alert('args:'+args);
	args = location.protocol+'//'+location.host+args;
	//alert('openFilePopUp url:'+args);	
	var POP_UP_WIDTH = 372;
	var POP_UP_HEIGHT = 125;
	var size = getWindowSize();
	theFilePopUp = window.open(args,"filePop","HEIGHT="+POP_UP_HEIGHT+",WIDTH="+POP_UP_WIDTH+"");
	var xPos =((size.w - POP_UP_WIDTH) / 2) + theFilePopUp.opener.screenLeft;
	var yPos =((size.h - POP_UP_HEIGHT) / 2) + theFilePopUp.opener.screenTop;
	//alert("xPos:"+xPos+"yPos:"+yPos);
	theFilePopUp.moveTo(xPos, yPos);
}

function closeUI(){
	window.close();
}

function getHostURL(){
	//		http:					uklams.net:8080
	var p = location.protocol+'//'+location.host;
	//alert('pathname:'+location.pathname);
	//debug:
	//alert('getPathToRoot:'+p);
	return p;
}

function setSaved(args){
		//convert the strings returned from flash to proper boolean values
		if(args=="true"){
			saved = true;
		}else{
			saved = false;
		}
}

function myOnBeforeUnload(){
	if(!saved && !mac){
		window.event.returnValue = "Your design is not saved, any changes you have made since you last saved will be lost.";
	}
}

function endPreviewSession(){
	if(previewSessionId != null){
		//debug: alert('Opening URL:'+"staff/staff.do?method=removeSession&sid="+previewSessionId+"&session=preview");
		document.location = "staff/staff.do?method=removeSession&sid="+previewSessionId+"&session=preview";
	}
}

window.onbeforeunload = myOnBeforeUnload;


/*
function checkSaved(){
	if(saved){
		alert("closing");
		return true;
	}else{
		var answer = confirm ("Your design is not saved, any changes you have made since you last saved will be lost.\nare you sure you want to close?")
		if (answer)
			//alert ("Oh yeah?")
			return true;
		else
			return false;
	}
}
*/
//-->
</script>
<TITLE>Author :: LAMS</TITLE>
</HEAD>
<BODY bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<!-- URL's used in the movie-->
<!-- text used in the movie-->
<!--Library-->
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
 codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,47,0" name="authoring"
 width="100%" height="100%" align="" id="authoring">
  <param name="allowScriptAccess" value="sameDomain" />

 <!-- <param name="movie" value="lams_authoring.swf?userID=<lams:user property="userID"/>&serverURL=<%=pathToRoot%>&build=<%=authoringClientVersion%>&lang=<%=serverLanguage%>&date=<%=languageDate%>">-->
  <param name="movie" value="lams_authoring.swf?userID=<lams:user property="userID"/>&serverURL=<%=pathToRoot%>&build=<%=authoringClientVersion%>&lang=es&date=<%=languageDate%>&theme=<lams:user property="flashTheme"/>">
  <param name="quality" value="high">
  <param name="scale" value="noscale">
  <param name="bgcolor" value="#B3B7C8">
  <embed 	
   	  src="lams_authoring.swf?userID=<lams:user property="userID"/>&serverURL=<%=pathToRoot%>&build=<%=authoringClientVersion%>&lang=es&date=<%=languageDate%>&theme=<lams:user property="flashTheme"/>"
	  quality="high" 
	  scale="noscale" 
	  bgcolor="#B3B7C8"  
	  width="100%" 
	  height="100%" 
	  swliveconnect=true 
	  id="authoring" 
	  name="authoring" 
	  align=""
	  type="application/x-shockwave-flash" 
	  pluginspage="http://www.macromedia.com/go/getflashplayer" />
</object>

</BODY>
</HTML>
