<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="daco" value="${sessionMap.daco}"/>

<c:if test="${!sessionMap.isPageEditable}">
	<p class="warning">
		<fmt:message key="message.learning.alertContentEdit" />
	</p>
</c:if>

<table cellpadding="0">
	<tr>
		<td>
			<fmt:message key="label.authoring.basic.title" />
			:
		</td>
		<td>
			<c:out value="${daco.title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="label.authoring.basic.instruction" />
			:
		</td>
		<td>
			<c:out value="${daco.instructions}" escapeXml="false" />
		</td>
	</tr>
	<c:if test="${sessionMap.isPageEditable}">
		<tr>
			<td colspan="2">
				<c:url var="authoringUrl" value="/definelater.do">
					<c:param name="toolContentID" value="${sessionMap.toolContentID}" />
					<c:param name="contentFolderID" value="${sessionMap.contentFolderID}" />
				</c:url>
				<a href="#" onclick="javascript:launchPopup('${authoringUrl}','definelater')" class="button">
					<fmt:message key="label.monitoring.edit.activity.edit" />
				</a>
			</td>
		</tr>
	</c:if>
</table>
