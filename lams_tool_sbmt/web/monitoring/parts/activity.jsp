<%@ include file="/common/taglibs.jsp"%>

<input type="hidden" name="toolContentID" value="<c:out value='${authoring.contentID}'/>" />

<c:if test="${isPageEditable}">
	<p class="warning">
		<fmt:message key="message.alertContentEdit" />
	</p>
</c:if>

<table cellpadding="0">
	<tr>
		<td class="field-name" width="10%" nowrap>
			<fmt:message key="label.authoring.basic.title" />
			:
		</td>
		<td>
			<c:out value="${authoring.title}" escapeXml="true" />
		</td>
	</tr>
	<!-- Instructions Row -->
	<tr>
		<td class="field-name" width="10%" nowrap valign="top">
			<fmt:message key="label.authoring.basic.instruction" />
			:
		</td>
		<td>
			<c:out value="${authoring.instruction}" escapeXml="false" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<c:url value="/definelater.do" var="authoringUrl">
				<c:param name="mode" value="teacher" />
				<c:param name="toolContentID" value="${authoring.contentID}" />
				<c:param name="contentFolderID" value="${contentFolderID}" />
			</c:url>
			<html:link href="javascript:;" onclick="launchPopup('${authoringUrl}','definelater')" styleClass="button">
					<fmt:message key="label.monitoring.edit.activity.edit" />
			</html:link>
		</td>
	</tr>
</table>
