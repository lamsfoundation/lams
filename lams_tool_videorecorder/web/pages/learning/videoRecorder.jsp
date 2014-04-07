<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ include file="/common/taglibs.jsp"%>

<%
	String red5Url = Configuration.get(ConfigurationKeys.RED5_SERVER_URL);
	String red5RecordingsUrl = Configuration.get(ConfigurationKeys.RED5_RECORDINGS_URL);
%>

<c:if test='<%= red5Url.equals("")  || red5RecordingsUrl.equals("") %>'>
	<script type="text/javascript">	
		alert("<fmt:message key='videorecorder.error.noconfig'/>");
	</script>
</c:if>

<c:set var="formBean"value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<script src="<lams:LAMSURL/>includes/javascript/AC_OETags.js" type="text/javascript"></script>
<script type="text/javascript">
<!--

	// Globals
	// Major version of Flash required
	var requiredMajorVersion = 9;
	// Minor version of Flash required
	var requiredMinorVersion = 0;
	// Minor version of Flash required
	var requiredRevision = 124;
	var requiredRevisionIE = 159;

	var mode = "${mode}";

	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}

	function validateForm() {
	
		// Validates that there's input from the user. 
		
		// disables the Finish button to avoid double submittion 
		disableFinishButton();

 		if (mode == "learner") {
			// if this is learner mode, then we add this validation see (LDEV-1319)
		
			if (document.learningForm.entryText.value == "") {
				
				// if the input is blank, then we further inquire to make sure it is correct
				if (confirm("<fmt:message>message.learner.blank.input</fmt:message>"))  {
					// if correct, submit form
					return true;
				} else {
					// otherwise, focus on the text area
					document.learningForm.entryText.focus();
					document.getElementById("finishButton").disabled = false;
					return false;      
				}
			} else {
				// there was something on the form, so submit the form
				return true;
			}
		}
	}

-->
</script>

<div id="content">
	<h1>
		<c:out value="${videoRecorderDTO.title}"/>
	</h1>

	<p>
		<c:out value="${videoRecorderDTO.instructions}" escapeXml="false"/>
	</p>

	<c:if test="${videoRecorderDTO.lockOnFinish and mode == 'learner'}">
		<div class="info">
			<c:choose>
				<c:when test="${finishedActivity}">
					<fmt:message key="message.activityLocked" />
				</c:when>
				<c:otherwise>
					<fmt:message key="message.warnLockOnFinish" />
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>

	&nbsp;
	
	<div id="videoRecorder">
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
				"width", "100%",
				"height", "676",
				"align", "middle",
				"id", "VideoRecorder",
				"quality", "high",
				"bgcolor", "#ffffff",
				"name", "VideoRecorder",
				"allowScriptAccess","always",
				"type", "application/x-shockwave-flash",
				"pluginspage", "http://www.adobe.com/go/getflashplayer"
			);
		} else if (hasRequestedVersion) {
			// if we've detected an acceptable version
			// embed the Flash Content SWF when all tests are passed
			AC_FL_RunContent(
					"src", "./includes/flash/VideoRecorder",
					"FlashVars", "contentEditable="+${contentEditable} +
									'&contentFolderUrl=/lams//www/secure/${contentFolderId}/Recordings/' +
									'&toolSessionId='+${toolSessionId} + 
									'&toolContentId='+${toolContentId} +
									'&mode='+'${mode}' +
									'&userId='+${userId} +
									'&allowUseVoice='+${videoRecorderDTO.allowUseVoice} +
									'&allowUseCamera='+${videoRecorderDTO.allowUseCamera} +
									'&allowLearnerVideoVisibility='+${videoRecorderDTO.allowLearnerVideoVisibility} +
									'&allowComments='+${videoRecorderDTO.allowComments} +
									'&allowRatings='+${videoRecorderDTO.allowRatings} +
									'&red5ServerUrl='+'${red5ServerUrl}' +
									'&serverUrl='+'${serverUrl}' +
									'&languageXML='+"${languageXML}" +
									"",
					"width", "100%",
					"height", "676",
					"align", "middle",
					"id", "VideoRecorder",
					"quality", "high",
					"bgcolor", "#ffffff",
					"name", "VideoRecorder",
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
					id="VideoRecorder" width="100%" height="676"
					codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
					<param name="movie" value="/includes/flash/VideoRecorder.swf" />
					<param name="quality" value="high" />
					<param name="bgcolor" value="#ffffff" />
					<param name="allowScriptAccess" value="sameDomain" />
					<embed src="./includes/flash/VideoRecorder.swf" quality="high" bgcolor="#869ca7"
						width="526" height="676" name="VideoRecorder" align="middle"
						play="true"
						loop="false"
						quality="high"
						allowScriptAccess="always"
						type="application/x-shockwave-flash"
						pluginspage="http://www.adobe.com/go/getflashplayer">
					</embed>
			</object>
		</noscript>
	</div>
	
	<c:if test="${mode == 'learner' || mode == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>
</div>
