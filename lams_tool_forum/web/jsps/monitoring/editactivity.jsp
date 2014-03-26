<%@ include file="/common/taglibs.jsp"%>
<html:errors />
<table cellpadding="0">
	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="label.authoring.basic.title" />
			:
		</td>
		<td>
			<c:out value="${title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="label.authoring.basic.instruction" />
			:
		</td>
		<td>
			<c:out value="${instruction}" escapeXml="false" />
		</td>
	</tr>

	<tr>
		<td colspan="2">
			<c:set var="isPageEditable" value="${isPageEditable}" />
			<c:choose>
				<c:when test='${isPageEditable == "true"}'>
					<c:url value="/defineLater.do" var="authoringUrl">
						<c:param name="contentFolderID" value="${contentFolderID}" />
						<c:param name="toolContentID" value="${toolContentID}" />
					</c:url>
					<html:link href="javascript:;" onclick="launchPopup('${authoringUrl}','definelater')" styleClass="button">
						<fmt:message key="label.monitoring.edit.activity.edit" />
					</html:link>
				</c:when>
				<c:otherwise>
					<div align="center">
						<B><fmt:message key="message.monitoring.edit.activity.not.editable" /></B>
					</div>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>

