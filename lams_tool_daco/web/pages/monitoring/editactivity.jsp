<%@ include file="/common/taglibs.jsp"%>

<c:set var="adTitle"><fmt:message key="tab.monitoring.edit.activity" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
	<c:choose>
		<c:when test="${sessionMap.isPageEditable}">
			<form id='define-later-form' method='post' action='../authoring/definelater.do' target='definelater'>
				<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
				<input type="hidden" name="toolContentID" value="${sessionMap.toolContentID}" />
				<input type="hidden" name="contentFolderID" value="${sessionMap.contentFolderID}" />
			</form>
	
			<button type="button" onclick="launchDefineLaterPopup()" class="btn btn-secondary float-end me-2 mb-3">
				<fmt:message key="label.monitoring.edit.activity.edit" />
			</button>
		</c:when>
		
		<c:otherwise>
			<lams:Alert5 type="warn" id="no-edit">
				<fmt:message key="message.learning.alertContentEdit" />
			</lams:Alert5>
		</c:otherwise>
	</c:choose>
          	
	<table class="table table-striped table-condensed">
		<tr>
			<td nowrap>
				<fmt:message key="label.authoring.basic.title" />
			</td>
			<td>
				<c:out value="${daco.title}" escapeXml="true" />
			</td>
		</tr>
	
		<tr>
			<td nowrap valign="top">
				<fmt:message key="label.authoring.basic.instruction" />
			</td>
			<td>
				<c:out value="${daco.instructions}" escapeXml="false" />
			</td>
		</tr>
	
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
	</table>
</lams:AdvancedAccordian>
