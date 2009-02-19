<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script src="./includes/flash/AC_OETags.js" language="javascript"></script>
<script type="text/javascript">
<!--
	var evalcomixWindow = null;
	
	function openEvalcomixWindow(url)
	{
    	evalcomixWindow=window.open(url,'evalcomixWindow','width=800,height=600,scrollbars=yes,resizable=yes');
		if (window.focus) {evalcomixWindow.focus()}
	}
//-->
</script>

<c:set var="dto" value="${videoRecorderDTO}" />

<h1>
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.th.advancedSettings" />
	</a>
</h1>
<br />

<div class="monitoring-advanced" id="advancedDiv" style="display:none">
<table class="alternative-color">

	<tr>
		<td>
			<fmt:message key="advanced.lockOnFinished" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.lockOnFinish}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="advanced.allowUseVoice" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.allowUseVoice}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="advanced.allowUseCamera" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.allowUseCamera}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="advanced.allowLearnerVideoVisibility" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.allowLearnerVideoVisibility}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="advanced.allowComments" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.allowComments}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="advanced.allowRatings" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.allowRatings}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="advanced.exportOffline" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.exportOffline}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="advanced.exportAll" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.exportAll}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
</table>
</div>

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
			"src", "./includes/flash/playerProductInstall",
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
				"FlashVars", "contentEditable"+${contentEditable}+'&toolSessionId='+${toolSessionId}+'&toolContentId='+${toolContentId}+'&mode='+'${mode}'+'&userId='+${userId}+'&allowUseVoice='+${videoRecorderDTO.allowUseVoice}+'&allowUseCamera='+${videoRecorderDTO.allowUseCamera}+'&allowLearnerVideoVisibility='+${videoRecorderDTO.allowLearnerVideoVisibility}+'&allowComments='+${videoRecorderDTO.allowComments}+'&allowRatings='+${videoRecorderDTO.allowRatings}+'&red5ServerUrl='+'${red5ServerUrl}'+'&serverUrl='+'${serverUrl}'+'&languageXML='+"${languageXML}"+"",
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
