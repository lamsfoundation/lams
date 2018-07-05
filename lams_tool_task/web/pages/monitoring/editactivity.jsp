<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="taskList" value="${sessionMap.taskList}"/>

<c:if test="${sessionMap.isPageEditable}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert>
</c:if>

<table class="table table-condensed table-no-border">
	<tr>
		<td width="15%" nowrap>
			<fmt:message key="label.authoring.basic.title" />
			:
		</td>
		<td>
			<c:out value="${taskList.title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td nowrap width="15%">
			<fmt:message key="label.authoring.basic.description" />
			:
		</td>
		<td>
			<c:out value="${taskList.instructions}" escapeXml="false" />
		</td>
	</tr>
</table>

<c:url  var="authoringUrl" value="/definelater.do">
	<c:param name="toolContentID" value="${sessionMap.toolContentID}" />
	<c:param name="contentFolderID" value="${sessionMap.contentFolderID}" />
</c:url>
<a href="javascript:;" onclick="launchPopup('${authoringUrl}','definelater')" class="btn btn-default pull-right">
	<fmt:message key="label.monitoring.edit.activity.edit" />
</a>
