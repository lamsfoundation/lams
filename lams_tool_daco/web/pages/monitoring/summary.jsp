<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="monitoringSummary" value="${sessionMap.monitoringSummary}" />
<c:set var="anyRecordsAvailable" value="false" />
<c:url  var="refreshSummaryUrl" value="/monitoring/summary.do?sessionMapID=${sessionMapID}"/>
<c:set var="daco" value="${sessionMap.daco}"/>

<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js"></script>
<script type="text/javascript">
	function exportSummary(){
		location.href = "<c:url value='/monitoring/exportToSpreadsheet.do'/>?sessionMapID=${sessionMapID}&reqID=" + (new Date()).getTime();
	};
</script>

<h2>
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" 
		onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="label.monitoring.advancedsettings" />
	</a>
</h2>
<br />

<div class="monitoring-advanced" id="advancedDiv" style="display:none">
	<table class="alternative-color">
		<tr>
			<td>
				<fmt:message key="label.common.min" />
			</td>
			<td>
				<c:choose>
					<c:when test="${daco.minRecords==0}">
						<fmt:message key="label.authoring.advanced.record.nolimit" />
					</c:when>
					<c:otherwise>
						${daco.minRecords}
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="label.common.max" />
			</td>
			<td>
				<c:choose>
					<c:when test="${daco.maxRecords==0}">
						<fmt:message key="label.authoring.advanced.record.nolimit" />
					</c:when>
					<c:otherwise>
						${daco.maxRecords}
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="label.authoring.advanced.notify.onlearnerentry" />
			</td>
			<td>
				<c:choose>
					<c:when test="${daco.notifyTeachersOnLearnerEntry}">
						<fmt:message key="label.monitoring.advancedsettings.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.monitoring.advancedsettings.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="label.authoring.advanced.notify.onrecordsubmit" />
			</td>
			<td>
				<c:choose>
					<c:when test="${daco.notifyTeachersOnRecordSumbit}">
						<fmt:message key="label.monitoring.advancedsettings.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.monitoring.advancedsettings.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advanced.lock.on.finished" />
			</td>
			<td>
				<c:choose>
					<c:when test="${daco.lockOnFinished}">
						<fmt:message key="label.monitoring.advancedsettings.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.monitoring.advancedsettings.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="label.monitoring.advancedsettings.addNotebook" />
			</td>
			<td>
				<c:choose>
					<c:when test="${daco.reflectOnActivity}">
						<fmt:message key="label.monitoring.advancedsettings.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.monitoring.advancedsettings.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		<c:if test="${daco.reflectOnActivity}">
			<tr>
				<td>
					<fmt:message key="label.monitoring.advancedsettings.notebookinstructions" />
				</td>
				<td>
					<lams:out value="${daco.reflectInstructions}" escapeHtml="true"/>
				</td>
			</tr>
		</c:if>
	</table>
</div>


<c:choose>
	<c:when test="${empty monitoringSummary || empty monitoringSummary[0].users}">
		<div align="center" style="font-weight: bold;">
			<fmt:message key="message.monitoring.summary.no.session" />
		</div>
	</c:when>
	<c:otherwise>
		
		<table cellpadding="0" class="alternative-color">
			<tr>
				<th><fmt:message key="label.monitoring.fullname" /></th>
				<th><fmt:message key="label.monitoring.recordcount" /></th>
				<c:if test="${daco.reflectOnActivity}">
					<th><fmt:message key="label.monitoring.notebook" /></th>
				</c:if>
			</tr>
			<c:forEach var="sessionSummary" items="${monitoringSummary}">
				<c:if test="${sessionMap.isGroupedActivity}">
					<tr>
						<td colspan="4" style="font-weight: bold; text-align: center">
							<fmt:message key="label.monitoring.group" />: ${sessionSummary.sessionName}
						</td>
					</tr>
				</c:if>
				<c:forEach var="user" items="${sessionSummary.users}">
					<tr>
						<td>
							<c:out value="${user.fullName}" escapeXml="true"/>
						</td>
						<td  style="text-align: center; font-weight: bold;">
							<c:choose>
								<c:when test="${user.recordCount > 0}">
									<c:set var="anyRecordsAvailable" value="true" />
									<c:url var="viewRecordList"	value="/monitoring/listRecords.do">
										<c:param name="sessionMapID" value="${sessionMapID}" />
										<c:param name="userUid" value="${user.uid}" />
									</c:url>
									<a href="#" onclick="javascript:launchPopup('${viewRecordList }','RecordList')">
										${user.recordCount}
									</a>
								</c:when>
								<c:otherwise>
									0
								</c:otherwise>
							</c:choose>
						</td>
						<c:if test="${daco.reflectOnActivity}">
							<td style="text-align: center">
								<c:choose>
									<c:when test="${empty user.reflectionEntry}">
										<fmt:message key="label.monitoring.notebook.none" />
									</c:when>
									<c:otherwise>
										<c:url var="viewReflection"	value="/monitoring/viewReflection.do">
											<c:param name="toolSessionID" value="${sessionSummary.sessionId}" />
											<c:param name="userId" value="${user.userId}" />
											<c:param name="sessionMapID" value="${sessionMapID}" />
										</c:url>
										<a href="#" onclick="javascript:launchPopup('${viewReflection }','Reflection')">
											<fmt:message key="label.monitoring.notebook.view" />
										</a>
									</c:otherwise>
								</c:choose>
							</td>
						</c:if>
					</tr>
				</c:forEach>
			</c:forEach>
		</table>
	</c:otherwise>
</c:choose>
<p>
	<a href="#nogo"  class="button" onclick="javascript:document.location.href='${refreshSummaryUrl}';">
		<fmt:message key="label.common.summary.refresh" />
	</a>
	<c:if  test="${anyRecordsAvailable}">
		<c:url var="viewRecordList" value="/monitoring/listRecords.do?sessionMapID=${sessionMapID}" />
		<a href="#nogo" onclick="javascript:launchPopup('${viewRecordList}','RecordList')" class="button space-left">
			<fmt:message key="label.monitoring.viewrecords.all" />
		</a>
		
		<html:link href="javascript:exportSummary();" styleClass="button space-left">
			<fmt:message key="button.export" />
		</html:link>
		
	</c:if>
</p>
