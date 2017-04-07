<%@ include file="/common/taglibs.jsp"%>
<html:errors />

<c:if test="${!isPageEditable}">
	<lams:Alert type="warn" id="no-edit" close="false">
		<fmt:message key="message.monitoring.edit.activity.not.editable" />
	</lams:Alert>
</c:if>


<table class="table table-condensed">
	<tr>
		<td class="field-name" width="10%" valign="top">
			<fmt:message key="label.authoring.basic.title" />
		</td>
		<td>
			<c:out value="${title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td class="field-name" width="10%" valign="top" NOWRAP>
			<fmt:message key="label.authoring.basic.instruction" />
		</td>
		<td>
			<c:out value="${instruction}" escapeXml="false" />
		</td>
	</tr>
</table>

<c:if test='${isPageEditable}'>
	<c:url value="/defineLater.do" var="authoringUrl">
		<c:param name="contentFolderID" value="${contentFolderID}" />
		<c:param name="toolContentID" value="${toolContentID}" />
		<c:param name="mode" value="teacher" />
	</c:url>
	<html:link href="javascript:;" onclick="launchPopup('${authoringUrl}','definelater')" styleClass="btn btn-default pull-right">
		<fmt:message key="label.monitoring.edit.activity.edit" />
	</html:link>
</c:if>

