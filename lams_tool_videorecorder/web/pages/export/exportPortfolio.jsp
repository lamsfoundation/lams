<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<script src="./files/AC_OETags.js" language="javascript"></script>

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
	// -->
</script>

<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><c:out value="${videoRecorderDTO.title}" escapeXml="false" />
		</title>
		<lams:css localLinkPath="../" />
	</lams:head>

	<body class="stripes">

			<div id="content">
			
				<table>
					<tr>
						<td width="1">
							<h1>
								<c:out value="${videoRecorderDTO.title}" escapeXml="false" />
							</h1>
						</td>
						<td>
							<a href="http://wiki.lamsfoundation.org/display/lamsdocs/lavidr10#lavidr10-ExportPortfolios"><img src="../images/help.jpg"border="0" /></a>
						</td>
					</tr>		
				</table>	
	
				<p>
					<c:out value="${videoRecorderDTO.instructions}" escapeXml="false" />
				</p>
	
				<c:forEach var="recording" items="${videoRecorderRecordingDTOs}">
					<table>
						<tr>
							<td class="field-name">
								<fmt:message key="videorecorder.author" />:
							</td>
							<td>
								<c:out value="${recording.createBy.firstName}" escapeXml="false"></c:out> <c:out value="${recording.createBy.lastName}" escapeXml="false"></c:out>
							</td>
						</tr>
						<tr>
							<td class="field-name">
								<fmt:message key="label.created" />:
							</td>
							<td>
								<lams:Date value="${recording.createDate}"></lams:Date>
							</td>
						</tr>
						<tr>
							<td class="field-name">
								<fmt:message key="videorecorder.title" />:
							</td>
							<td>
								<c:out value="${recording.title}" escapeXml="false"></c:out>
							</td>
						</tr>
						<tr>
							<td class="field-name">
								<fmt:message key="videorecorder.description" />:
							</td>
							<td>
								<c:out value="${recording.description}" escapeXml="false"></c:out>
							</td>
						</tr>
						<tr>
							<td align="left" colspan="2">
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
											"src", "./files/playerProductInstall",
											"FlashVars", "MMredirectURL="+MMredirectURL+'&MMplayerType='+MMPlayerType+'&MMdoctitle='+MMdoctitle+"",
											"width", "361",
											"height", "331",
											"align", "middle",
											"id", "VideoRecorderFCKEditor",
											"quality", "high",
											"bgcolor", "#869ca7",
											"name", "VideoRecorderFCKEditor",
											"allowScriptAccess","always",
											"type", "application/x-shockwave-flash",
											"pluginspage", "http://www.adobe.com/go/getflashplayer"
										);
									} else if (hasRequestedVersion) {
										// if we've detected an acceptable version
										// embed the Flash Content SWF when all tests are passed
										AC_FL_RunContent(
												"src", "./files/VideoRecorderFCKEditor",
												<c:choose>
												<c:when test="${videoRecorderDTO.exportOffline == true}">
													"FlashVars", "&mode="+'playerModeOffline' + '&offlinePlayback=false' + '&red5ServerUrl='+'${red5ServerUrl}' + '&serverUrl='+'${serverUrl}' + '&filename='+'${recording.filename}'+'.flv' + '&languageXML=' + "${languageXML}"+"",
												</c:when>
												<c:otherwise>
													"FlashVars", "&mode="+'playerModeOnline' + '&offlinePlayback=true' + '&red5ServerUrl='+'${red5ServerUrl}' + '&serverUrl='+'${serverUrl}' + '&filename='+'${recording.filename}'+'.flv' + '&languageXML=' + "${languageXML}"+"",
												</c:otherwise>
												</c:choose>											
												"width", "361",
												"height", "331",
												"align", "middle",
												"id", "VideoRecorderFCKEditor",
												"quality", "high",
												"bgcolor", "#869ca7",
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
							</td>
						</tr>
						<tr>
							<td class="field-name">
								<fmt:message key="videorecorder.rating" />:
							</td>
							<td>
								<c:out value="${recording.rating}" escapeXml="false"></c:out>
							</td>
						</tr>
						<tr>
							<td class="field-name">
								<fmt:message key="videorecorder.comment" />s:
							</td>
						</tr>
						<c:forEach var="comment" items="${recording.comments}">
							<tr>
								<td class="field-name">
									<fmt:message key="videorecorder.author" />:
								</td>
								<td>
									<c:out value="${comment.createBy.firstName}" escapeXml="false"></c:out> <c:out value="${comment.createBy.lastName}" escapeXml="false"></c:out>
								</td>
							</tr>
							<tr>
								<td class="field-name">
									<fmt:message key="label.created" />:
								</td>
								<td>
									<lams:Date value="${comment.createDate}"></lams:Date>
								</td>
							</tr>
							<tr>
							<td class="field-name">
									<fmt:message key="videorecorder.comment" />:
								</td>
								<td>
									<c:out value="${comment.text}" escapeXml="false"></c:out>
								</td>
							</tr>
						</c:forEach>
						
					</table>
				</c:forEach>
			</div>
			<!--closes content-->

			<div id="footer">
			</div>
			<!--closes footer-->

	</body>
</html>

