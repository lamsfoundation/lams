<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
            "http://www.w3.org/TR/html4/loose.dtd">
            
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-core" prefix="c"%>

<%
	String red5Url = Configuration.get(ConfigurationKeys.RED5_SERVER_URL);
	String red5RecordingsUrl = Configuration.get(ConfigurationKeys.RED5_RECORDINGS_URL);
%>

<!-- saved from url=(0014)about:internet -->
<lams:html>

<!-- 
Smart developers always View Source. 

This application was built using Adobe Flex, an open source framework
for building rich Internet applications that get delivered via the
Flash Player or to desktops via Adobe AIR. 

Learn more about Flex at http://flex.org 
// -->

<lams:head>

<lams:css style="core"/>

<title></title>
<script src="<lams:LAMSURL/>includes/javascript/AC_OETags.js" type="text/javascript"></script>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery-latest.pack.js"></script>

<!--  BEGIN Browser History required section -->
<script src="history/history.js" language="javascript"></script>
<!--  END Browser History required section -->

<style>
body { margin: 0px; overflow:hidden }
</style>

</lams:head>

<body scroll="no">
<script language="JavaScript" type="text/javascript">

// Globals
// Major version of Flash required
var requiredMajorVersion = 9;
// Minor version of Flash required
var requiredMinorVersion = 0;
// Minor version of Flash required
var requiredRevision = 124;
var requiredRevisionIE = 159;
var CKGlobal = window.opener.CKEDITOR;
var CK = CKGlobal.instances["${param.ckEditorName}"];
var languageXML = '';

<c:if test='<%= red5Url.equals("")  || red5RecordingsUrl.equals("") %>'>
		alert(CK.lang.videorecorder.videorecorder_error_noconfig);
</c:if>

function getLanguageXML(){
	var languageCollection = new Array('videorecorder_video_player', 'videorecorder_video_recorder',
		'videorecorder_web_application_not_available', 'videorecorder_net_connection_not_connected', 'videorecorder_buffering',
		'videorecorder_net_connection_closed', 'videorecorder_playing', 'videorecorder_ready', 'videorecorder_recording',
		'videorecorder_paused', 'videorecorder_waiting', 'videorecorder_description', 'videorecorder_title',
		'videorecorder_new_recording_details', 'videorecorder_recording_complete_authoring',
		'videorecorder_enter_something_here', 'videorecorder_recording_complete_fck', 'videorecorder_tooltip_play',
		'videorecorder_tooltip_pause', 'videorecorder_tooltip_resume', 'videorecorder_tooltip_save_recording',
		'videorecorder_tooltip_start_recording', 'videorecorder_tooltip_start_recording_again',
		'videorecorder_tooltip_start_recording_next', 'videorecorder_tooltip_stop_recording',
		'videorecorder_disabled', 'button_save', 'button_ok', 'button_cancel', 'button_yes', 'button_no', 'videorecorder_camera_not_available', 'videorecorder_mic_not_available');
	
	var languageOutput = "<xml><language>";
	
	for(var i = 0; i < languageCollection.length; i++){
		var key = languageCollection[i];
		var keyForXML = key.replace(/_/g, ".");
				
		languageOutput += "<entry key='" + keyForXML + "'><name>" + CK.lang.videorecorder[key] + "</name></entry>";
	}
	
	languageOutput += "</language></xml>";
	
	return languageOutput;
}

function getMyApp(appName) {
    if (navigator.appName.indexOf ("Microsoft") !=-1) {
        return window[appName];
    } else {
        return document[appName];
    }
}

function saveToFCKEditor(eventObj) {

	var innerHTML = '';
	innerHTML +=	'<OBJECT id="VideoRecorderCKEditor"'
	innerHTML +=	'        width="361" height="331" >';
	innerHTML +=	'<param name="movie" value="' + CKGlobal.plugins.getPath('videorecorder') + '/VideoRecorderCKEditor.swf" />';
	innerHTML +=	'<param name="quality" value="high" />';
	innerHTML +=	'<param name="bgcolor" value="#ffffff" />';
	innerHTML +=	'<param name="allowScriptAccess" value="sameDomain" />';
	innerHTML +=	'<embed src="' + CKGlobal.plugins.getPath('videorecorder') + '/VideoRecorderCKEditor.swf" quality="high" bgcolor="#869ca7"';
	innerHTML +=	'width="361" height="331" name="VideoRecorderCKEditor" align="middle"';
	innerHTML +=	'play="true"';
	innerHTML +=	'loop="false"';
	innerHTML +=	'quality="high"';
	innerHTML +=	'allowScriptAccess="always"';
	innerHTML +=	'type="application/x-shockwave-flash"';
	innerHTML +=	'FlashVars="mode=playerModeOffline'+'&offlinePlayback=true'+'&red5ServerUrl=<%= Configuration.get(ConfigurationKeys.RED5_SERVER_URL) %>' + '&filename=' + eventObj.filename + '&languageXML=' + escape(getLanguageXML()) + '"';
	innerHTML +=	'pluginspage="http://www.adobe.com/go/getflashplayer">';
	innerHTML +=	'</embed>';
	innerHTML +=	'</OBJECT>';
	
	CK.insertHtml(innerHTML);
	
	window.close();
}
</script>
<table>
	<tr>
		<td>
			<script language="JavaScript" type="text/javascript">
			<!--
			// Version check for the Flash Player that has the ability to start Player Product Install (6.0r65)
			var hasProductInstall = DetectFlashVer(6, 0, 65);
			
			// Version check based upon the values defined in globals
			var hasRequestedVersion = (isIE == true) ? DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevisionIE) : DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);
			
			if ( hasProductInstall && !hasRequestedVersion ) {
				// DO NOT MODIFY THE FOLLOWING FOUR LINES
				// Location visited after installation is complete if installation is required
				var MMPlayerType = (isIE == true) ? "ActiveX" : "PlugIn";
				var MMredirectURL = window.location;
			    document.title = document.title.slice(0, 47) + " - Flash Player Installation";
			    var MMdoctitle = document.title;
			
				AC_FL_RunContent(
					"src", "<lams:LAMSURL/>/includes/flash/playerProductInstall",
					"FlashVars", "MMredirectURL="+MMredirectURL+'&MMplayerType='+MMPlayerType+'&MMdoctitle='+MMdoctitle+"",
					"width", "361",
					"height", "331",
					"align", "middle",
					"id", "VideoRecorderCKEditor",
					"quality", "high",
					"bgcolor", "#ffffff",
					"name", "VideoRecorderCKEditor",
					"allowScriptAccess","always",
					"type", "application/x-shockwave-flash",
					"pluginspage", "http://www.adobe.com/go/getflashplayer"
				);
			} else if (hasRequestedVersion) {
				// if we've detected an acceptable version
				// embed the Flash Content SWF when all tests are passed
				AC_FL_RunContent(
						"src", "VideoRecorderCKEditor",
						"FlashVars", "mode=recorderModeFCK" +
										'&offlinePlayback=false' + 
										'&serverUrl=<%= Configuration.get(ConfigurationKeys.SERVER_URL) %>' +
										'&red5ServerUrl=<%= Configuration.get(ConfigurationKeys.RED5_SERVER_URL) %>' +
										'&red5RecordingsUrl=<%= Configuration.get(ConfigurationKeys.RED5_RECORDINGS_URL) %>' +
										'&lamsEarDir=<%= Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) %>' +
										'&saveToLamsDestUrl=/lams-www.war/secure/'+CKGlobal.config.contentFolderID+'/Recordings/' +
										'&contentFolderUrl=/lams//www/secure/'+CKGlobal.config.contentFolderID+'/Recordings/' +
										'&languageXML=' + escape(getLanguageXML()) +
										"",
						"width", "361",
						"height", "331",
						"align", "middle",
						"id", "VideoRecorderCKEditor",
						"quality", "high",
						"bgcolor", "#ffffff",
						"name", "VideoRecorderCKEditor",
						"allowScriptAccess","sameDomain",
						"type", "application/x-shockwave-flash",
						"pluginspage", "http://www.adobe.com/go/getflashplayer"
				);
			  } else {  // flash is too old or we can't detect the plugin
			    var alternateContent = 'Alternate HTML content should be placed here. '
			  	+ 'This content requires the Adobe Flash Player. '
			   	+ '<a href=http://www.adobe.com/go/getflash/>Get Flash</a>';
			    document.write(alternateContent);  // insert non-flash content
			  }
			// -->
			</script>
			<noscript>
			  	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
						id="VideoRecorderCKEditor" width="361" height="331"
						codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
						<param name="movie" value="VideoRecorderCKEditor.swf" />
						<param name="quality" value="high" />
						<param name="bgcolor" value="#ffffff" />
						<param name="allowScriptAccess" value="sameDomain" />
						<embed src="VideoRecorderCKEditor.swf" quality="high" bgcolor="#869ca7"
							width="361" height="331" name="VideoRecorderCKEditor" align="middle"
							play="true"
							loop="false"
							quality="high"
							allowScriptAccess="always"
							type="application/x-shockwave-flash"
							pluginspage="http://www.adobe.com/go/getflashplayer">
						</embed>
				</object>
			</noscript>
		</td>
	</tr>
	<tr>
		<td>
			<fmt:message key="videorecorder.instructions.fck"></fmt:message>
		</td>
	</tr>
</table>
</body>
</lams:html>
