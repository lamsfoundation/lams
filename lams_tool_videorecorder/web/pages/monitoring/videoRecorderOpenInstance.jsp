<!DOCTYPE html>
            
<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><c:out value="${videoRecorderDTO.title}" escapeXml="false" />
		</title>
	</lams:head>

	<body>
		<script src="<lams:LAMSURL/>includes/javascript/AC_OETags.js" type="text/javascript"></script>
					<div id="videoRecorder">
						<script language="JavaScript" type="text/javascript">
						<!--
						// Globals
						// Major version of Flash required
						var requiredMajorVersion = 9;
						// Minor version of Flash required
						var requiredMinorVersion = 0;
						// Minor version of Flash required
						var requiredRevision = 124;
					
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
													'&contentFolderUrl=/lams//www/secure/${contentFolderID}/Recordings/' +
													'&toolSessionId='+${toolSessionID} +
													'&toolContentId='+${videoRecorderDTO.toolContentId} +
													'&mode='+'${mode}' +
													'&userId='+${monitoringUid} +
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
									id="VideoRecorder" width="680" height="676"
									codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
									<param name="movie" value="/includes/flash/VideoRecorder.swf" />
									<param name="quality" value="high" />
									<param name="bgcolor" value="#ffffff" />
									<param name="allowScriptAccess" value="sameDomain" />
									<embed src="./includes/flash/VideoRecorder.swf" quality="high" bgcolor="#869ca7"
										width="100%" height="676" name="VideoRecorder" align="middle"
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
	</body>
</html>