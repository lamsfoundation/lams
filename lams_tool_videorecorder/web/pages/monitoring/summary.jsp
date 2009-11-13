<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery-latest.pack.js" ></script>
<script src="<lams:LAMSURL/>includes/javascript/AC_OETags.js" type="text/javascript"></script>
<script lang="javascript">
<!-- 
	function showMessage(url, sessionId) {
		var area=document.getElementById("videoRecorder" + sessionId);
		if(area != null){
			area.style.width="100%";
			area.style.height="100%";
			area.src=url;
			area.style.display="block";
		}
		var elem = document.getElementById("showhideVRButton" + sessionId);
		if (elem != null) {
			//elem.style.display="none";
			elem.href = "javascript: hideMessage('" + url + "', '" + sessionId + "');";
			$("#showhideVRButton" + sessionId).text("<fmt:message key="monitor.summary.button.hide.video.recorder"><fmt:param><fmt:message key="activity.title" /></fmt:param></fmt:message>");
		}
		location.hash = "videoRecorder" + sessionId;
	}
	function hideMessage(url, sessionId){
		var area=document.getElementById("videoRecorder" + sessionId);
		if(area != null){
			area.style.width="0px";
			area.style.height="0px";
			area.style.display="none";
		}
		var elem = document.getElementById("showhideVRButton" + sessionId);
		if (elem != null) {
			//elem.style.display="block";
			elem.href = "javascript: showMessage('" + url + "', '" + sessionId + "');";
			$("#showhideVRButton" + sessionId).text("<fmt:message key="monitor.summary.button.show.video.recorder"><fmt:param><fmt:message key="activity.title" /></fmt:param></fmt:message>");
		}
	}
	-->
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

<c:choose>
	<c:when test="${empty dto.sessionDTOs}">
		<p>
			<fmt:message key="message.monitoring.summary.no.session" />
		</p>
	</c:when>
	<c:otherwise>
		<c:forEach var="session" items="${dto.sessionDTOs}">
			<table cellspacing="0">
				<c:if test="${isGroupedActivity}">
					<tr>
						<td>
							<h2>
								${session.sessionName}
							</h2>
						</td>
					</tr>
				</c:if>
				<tr>
					<td>
						<c:set var="openVRInstanceUrl">
							<lams:WebAppURL />/monitoring.do?method=openVideoRecorderInstance&toolContentID=<c:out value="${dto.toolContentId}" />&toolSessionID=<c:out value="${session.sessionID}"/>&contentFolderID=<c:out value="${contentFolderID}"/>
						</c:set>
						
						<html:link href="javascript:showMessage('${openVRInstanceUrl}','${session.sessionID}')" styleClass="button" styleId="showhideVRButton${session.sessionID}" >
							<fmt:message key="monitor.summary.button.show.video.recorder">
								<fmt:param><fmt:message key="activity.title" /></fmt:param>
							</fmt:message>
						</html:link>
						
						<iframe
							onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
							id="videoRecorder<c:out value="${session.sessionID}" />" name="videoRecorder<c:out value="${session.sessionID}" />"
							style="width:0px;height:0px;border:0px;display:none" frameborder="no"
							scrolling="no">
						</iframe>
					</td>
				</tr>
			</table>
		</c:forEach>
	</c:otherwise>
</c:choose>	
