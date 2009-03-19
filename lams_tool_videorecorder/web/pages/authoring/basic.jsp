<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="formBean"
	value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
<c:set var="dto" value="${videoRecorderDTO}" />

<script src="<lams:LAMSURL/>includes/javascript/AC_OETags.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript">
<!--
// -----------------------------------------------------------------------------
// Globals
// Major version of Flash required
var requiredMajorVersion = 9;
// Minor version of Flash required
var requiredMinorVersion = 0;
// Minor version of Flash required
var requiredRevision = 124;

// -----------------------------------------------------------------------------
</script>

<!-- ========== Basic Tab ========== -->
<table cellpadding="0">
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.title"></fmt:message>
			</div>
			<html:text property="title" style="width: 99%;"></html:text>
		</td>
	</tr>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.instructions"></fmt:message>
			</div>
			<lams:FCKEditor id="instructions"
				value="${formBean.instructions}"
				contentFolderID="${sessionMap.contentFolderID}"></lams:FCKEditor>
		</td>
	</tr>
	<tr align="center">
		<td>
			<script language="JavaScript" type="text/javascript">
			<!--
			// Version check for the Flash Player that has the ability to start Player Product Install (6.0r65)
			var hasProductInstall = DetectFlashVer(6, 0, 65);
			
			// Version check based upon the values defined in globals
			var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);
			
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
					"id", "VideoRecorderFCKEditor",
					"quality", "high",
					"bgcolor", "#ffffff",
					"name", "VideoRecorderFCKEditor",
					"allowScriptAccess","always",
					"type", "application/x-shockwave-flash",
					"pluginspage", "http://www.adobe.com/go/getflashplayer"
				);
			} else if (hasRequestedVersion) {
				// if we've detected an acceptable version
				// embed the Flash Content SWF when all tests are passed
				AC_FL_RunContent(
						"src", "./includes/flash/VideoRecorderFCKEditor",
						"FlashVars", "mode=recorderModeAuthor"+'&red5ServerUrl=<%= Configuration.get(ConfigurationKeys.RED5_SERVER_URL) %>'+'&serverUrl=<%= Configuration.get(ConfigurationKeys.SERVER_URL) %>'+'&filename='+'${videoRecorderRecordingDTO.filename}'+'&toolContentId='+${toolContentId}+'&languageXML='+"${languageXML}"+"",
						"width", "361",
						"height", "331",
						"align", "middle",
						"id", "VideoRecorderFCKEditor",
						"quality", "high",
						"bgcolor", "#ffffff",
						"name", "VideoRecorderFCKEditor",
						"allowScriptAccess","always",
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
						id="VideoRecorderFCKEditor" width="361" height="331"
						codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
						<param name="movie" value="VideoRecorderFCKEditor.swf" />
						<param name="quality" value="high" />
						<param name="bgcolor" value="#ffffff" />
						<param name="allowScriptAccess" value="sameDomain" />
						<embed src="./includes/flash/VideoRecorderFCKEditor.swf" quality="high" bgcolor="#869ca7"
							width="361" height="331" name="VideoRecorderFCKEditor" align="middle"
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
			<fmt:message key="videorecorder.instructions.author"></fmt:message>
		</td>
	</tr>
</table>
