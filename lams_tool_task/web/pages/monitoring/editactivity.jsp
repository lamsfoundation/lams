<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="taskList" value="${sessionMap.taskList}"/>

<c:if test="${sessionMap.isPageEditable}">
	<p class="warning">
		<fmt:message key="message.alertContentEdit" />
	</p>
</c:if>

<table cellpadding="0">
	<tr>
		<td>
			<fmt:message key="label.authoring.basic.title" />
			:
		</td>
		<td>
			<c:out value="${taskList.title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="label.authoring.basic.description" />
			:
		</td>
		<td>
			<c:out value="${taskList.instructions}" escapeXml="false" />
		</td>
	</tr>

	<tr>
		<td colspan="2">
			<c:url  var="authoringUrl" value="/definelater.do">
				<c:param name="toolContentID" value="${sessionMap.toolContentID}" />
				<c:param name="contentFolderID" value="${sessionMap.contentFolderID}" />
			</c:url>
			<html:link href="javascript:;" onclick="launchPopup('${authoringUrl}','definelater')" styleClass="button">
				<fmt:message key="label.monitoring.edit.activity.edit" />
			</html:link>
		</td>
	</tr>
</table>
