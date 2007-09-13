<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration" import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<%
String authoringClientVersion = Configuration.get(ConfigurationKeys.AUTHORING_CLIENT_VERSION);
String languageDate = Configuration.get(ConfigurationKeys.DICTIONARY_DATE_CREATED);
String actColour = Configuration.get(ConfigurationKeys.AUTHORING_ACTS_COLOUR);
String version = Configuration.get(ConfigurationKeys.VERSION);
%>

<lams:html>
<lams:head>
<script src="<lams:LAMSURL/>includes/javascript/openUrls.js" type="text/javascript"></script>
<script src="<lams:LAMSURL/>includes/javascript/AC_RunActiveContent.js" type="text/javascript"></script>
<script src="<lams:LAMSURL/>includes/javascript/getSysInfo.js" type="text/javascript"></script>
<script language="JavaScript" type="text/JavaScript">
<!--

//browser detect:
var ns = (document.layers)? true: false
var ie = (document.all)? true: false
var b = (ns)? "Netscape": (ie) ?"Explorer" : " Sorry, LAMS does not support your browser.  Please contact technical.support@lamsinternational.com "

//flag to show if the content (LD) in the flash UI has changed.
var saved = true;
var thePopUp = null;
var theFilePopUp = null;

var previewWin = null;
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
		openPopUpFS(args);
	}else if(command == "openFilePopUp"){
		openFilePopUp(args);
	}else if (command == "setSaved"){
		setSaved(args);
	}else if(command == "openPreview"){
		//this is called for Preview - to launch the learner UI. args will contain the sessionId to open
		openPreview(args);
	}else if(command == "closeWindow"){
		closeWindow();
	}else if(command == "openURL"){
		openURL(args, "_blank");
	}else if(command == "openMonitorLesson") {
		openMonitorLesson(args); 
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

function openPopUp(args, title, h, w, resize, status, scrollbar, menubar, toolbar){
	// refocus code commented out as we want to replace contents due to tool's session issues. Code will be 
	// wanted again the future.
	//if(thePopUp && thePopUp.open && !thePopUp.closed){
	//		thePopUp.focus();
			
	//}else{
		thePopUp = window.open(args,title,"HEIGHT="+h+",WIDTH="+w+",resizable="+resize+",scrollbars=yes,status="+status+",menubar="+menubar+", toolbar="+toolbar);
	//}
}

function openPopUpFS(args){
	var params = args.split(",");
	
	// assigned the args
	var url = params[0];
	var title = params[1];
	var h = params[2];
	var w = params[3];
	var resize = params[4];
	var status = params[5];
	var scrollbar = params[6];
	var menubar = params[7];
	var toolbar = params[8];
	
	openPopUp(url, title, h, w, resize, status, scrollbar, menubar, toolbar);
}

function openPreview( lessonId )
{
	if(previewWin && !previewWin.closed )
	{
		previewWin.location = 'home.do?method=learner&mode=preview&lessonID='+lessonId;		
		previewWin.focus();
	}
	else
	{
		previewWin = window.open('home.do?method=learner&mode=preview&lessonID='+lessonId,'pWindow','width=796,height=570,resizable,status=yes');
	}
}

function openURL(args){
	window.open(args);
}

var learnWin = null;

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

function closeWindow(){
	// refresh the parent window
	var parentURL = "${notifyCloseURL}";
	
	if (parentURL != "") {
		window.parent.opener.location.href = parentURL;
	}
	
	if(isInternetExplorer) {
		this.focus();
		window.opener = this;
		window.close();
	} else {
		window.location.href = "javascript: window.close()";
	}
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
	//alert("myOnBeforeUnload");
	if(!saved){
	  var message = "<fmt:message key="msg.design.not.saved"/>";
	  if (typeof evt == 'undefined') {
		 evt = window.event;
	  }
	  if (evt) {
		evt.returnValue = message;
	  }
	  return message;
	}
}

function openMonitorLesson(lessonID) {	
	try{
		window.opener.openMonitorLesson(lessonID);
	}catch(e){
		returnToMonitorLessonIntegrated(lessonID);
	}
}
		

if(window.attachEvent) { window.attachEvent("onbeforeunload", myOnBeforeUnload); }
else { window.onbeforeunload = myOnBeforeUnload; }
	
//-->
</script>

<TITLE><fmt:message key="title.author.window"/></TITLE>
</lams:head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<c:set var="authorurl_params">?loadFile=lams_authoring.swf&loadLibrary=lams_authoring_library.swf&userID=<lams:user property="userID"/>&serverURL=<lams:LAMSURL/>&build=<%=authoringClientVersion%>&version=<%=version%>&lang=<lams:user property="localeLanguage"/>&country=<lams:user property="localeCountry"/>&langDate=<%=languageDate%>&theme=<lams:user property="flashTheme"/>&actColour=<%= actColour %><c:if test="${not empty requestSrc}">&requestSrc=${requestSrc}</c:if><c:if test="${not empty learningDesignID}">&learningDesignID=${learningDesignID}</c:if><c:if test="${not empty layout}">&layout=${layout}</c:if>&uniqueID=<lams:generateID/></c:set>
<c:set var="authorurl_nojs">lams_preloader.swf<c:out value="${authorurl_params}"/></c:set>
<c:set var="authorurl_js">lams_preloader<c:out value="${authorurl_params}"/></c:set>

	<script language="JavaScript" type="text/javascript">
		Its();
		AC_FL_RunContent('classid', 'clsid:D27CDB6E-AE6D-11cf-96B8-444553540000', 'codebase','http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,47,0','width','100%','height','100%','align','left','src','<c:out value="${authorurl_js}" escapeXml="false"/>&amp;isMac=' + isMac,'quality','high','scale','noscale','bgcolor','#FFFFFF','name','authoring', 'id', 'authoring', 'allowscriptaccess','sameDomain', 'swliveconnect', true, 'type', 'application/x-shockwave-flash', 'pluginspage','http://www.macromedia.com/go/getflashplayer','movie', '<c:out value="${authorurl_js}" escapeXml="false"/>&amp;isMac=' + isMac);
			
	</script>
	
	<noscript>
		<!-- URL's used in the movie-->
		<!-- text used in the movie-->
		<!--Library-->
		<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
		 codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,47,0" name="authoring"
		 width="100%" height="100%" align="" id="authoring">
		  <param name="allowScriptAccess" value="sameDomain" />
		
		  <param name="movie" value="${authorurl_nojs}">
		  <param name="quality" value="high">
		  <param name="scale" value="noscale">
		  <param name="bgcolor" value="#FFFFFF">
		  <embed 	
		   	  src="${authorurl_nojs}"
			  quality="high" 
			  scale="noscale" 
			  bgcolor="#FFFFFF"  
			  width="100%" 
			  height="100%" 
			  swliveconnect=true 
			  id="authoring" 
			  name="authoring" 
			  align=""
			  type="application/x-shockwave-flash" 
			  pluginspage="http://www.macromedia.com/go/getflashplayer" />
		</object>
	</noscript>
</BODY>
</lams:html>
