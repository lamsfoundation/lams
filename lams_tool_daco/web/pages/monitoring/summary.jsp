<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="monitoringSummary" value="${sessionMap.monitoringSummary}" />
<c:set var="anyRecordsAvailable" value="false" />
<c:url  var="refreshSummaryUrl" value="/monitoring/summary.do?sessionMapID=${sessionMapID}"/>
<c:set var="daco" value="${sessionMap.daco}"/>

<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js"></script>

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
				<fmt:message key="label.authoring.advanced.record.min" />
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
				<fmt:message key="label.authoring.advanced.record.max" />
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
				<fmt:message key="label.authoring.advanced.lock.on.finished" />
			</td>
			<td>
				<c:choose>
					<c:when test="${daco.lockWhenFinished}">
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
					${daco.reflectInstructions}	
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
				<th><fmt:message key="label.monitoring.loginname" /></th>
				<th><fmt:message key="label.monitoring.recordcount" /></th>
				<th><fmt:message key="label.monitoring.action" /></th>
			</tr>
			<c:forEach var="sessionSummary" items="${monitoringSummary}">
				<tr>
					<td colspan="4" style="font-weight: bold; text-align: center">
						<fmt:message key="label.monitoring.group" />: ${sessionSummary.sessionName}
					</td>
				</tr>
				<c:forEach var="user" items="${sessionSummary.users}">
					<tr>
						<td>
							${user.fullName}
						</td>
						<td>
							${user.loginName}
						</td>
						<td>
						${user.recordCount}
						</td>
						<td style="width: 150px">&nbsp;
							<c:if  test="${user.recordCount > 0}">
								<c:url var="viewRecordList"
										value="/monitoring/listRecords.do?sessionMapID=${sessionMapID}&userUid=${user.uid}" />
								<c:set var="anyRecordsAvailable" value="true" />
								<a href="#" onclick="javascript:launchPopup('${viewRecordList }','RecordList')" class="button">
									<fmt:message key="label.monitoring.viewrecords" />
								</a>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</c:forEach>
		</table>
	</c:otherwise>
</c:choose>
<p>
	<a href="#"  class="button" onclick="javascript:document.location='${refreshSummaryUrl}';">
		<fmt:message key="label.common.summary.refresh" />
	</a>
	<c:if  test="${anyRecordsAvailable}">
		<c:url var="viewRecordList" value="/monitoring/listRecords.do?sessionMapID=${sessionMapID}" />
		<a href="#" onclick="javascript:launchPopup('${viewRecordList}','RecordList')" class="button space-left">
			<fmt:message key="label.monitoring.viewrecords.all" />
		</a>
	</c:if>
</p>