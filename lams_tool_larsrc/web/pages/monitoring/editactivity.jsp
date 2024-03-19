<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="resource" value="${sessionMap.resource}"/>

<c:set var="adTitle"><fmt:message key="monitoring.tab.edit.activity" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">

<c:if test="${sessionMap.isPageEditable}">
	<lams:Alert5 type="warn" id="no-edit">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert5>
</c:if>

<form id='define-later-form' method='post' action='../authoring/definelater.do' target='definelater'>
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
	<input type="hidden" name="toolContentID" value="${sessionMap.toolContentID}" />
	<input type="hidden" name="contentFolderID" value="${sessionMap.contentFolderID}" />
</form>

<button type="button" onclick="launchDefineLaterPopup()" class="btn btn-secondary btn-icon-pen float-end m-3">
	<fmt:message key="label.monitoring.edit.activity.edit" />
</button>

<table class="table table-striped table-condensed mb-0">
	<tr>
		<td>
			<fmt:message key="label.authoring.basic.title" />
		</td>
		<td>
			<c:out value="${resource.title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="label.authoring.basic.instruction" />
		</td>
		<td>
			<c:out value="${resource.instructions}" escapeXml="false" />
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="label.authoring.advance.lock.on.finished" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.resource.lockWhenFinished == true}">
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
			<fmt:message key="label.authoring.advance.mini.number.resources.view" />
		</td>
		
		<td>
			${sessionMap.resource.miniViewResourceNumber}
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.authoring.advance.allow.learner.add.urls" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.resource.allowAddUrls == true}">
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
			<fmt:message key="label.authoring.advance.allow.learner.add.files" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.resource.allowAddFiles == true}">
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
			<fmt:message key="label.authoring.advanced.notify.onassigmentsubmit" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.resource.notifyTeachersOnAssigmentSumbit == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
</table>
</lams:AdvancedAccordian>