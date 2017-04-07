<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${requestScope.monitoringDTO}" />

<c:if test="${dto.contentInUse}">
	<lams:Alert type="info" id="content-in-use" close="false">
		<fmt:message key="message.contentInUseSet" /> 
	</lams:Alert>
</c:if>	

<table class="table table-condensed">
	<tbody>
		<tr>
			<td class="field-name" style="width: 30%;">
				<fmt:message key="label.authoring.basic.title" />
			</td>
			<td>
				<c:out value="${dto.title}" escapeXml="true" />
			</td>
		</tr>
		<tr>
			<td class="field-name" style="width: 30%;">
				<fmt:message key="label.authoring.basic.instructions" />
			</td>
			<td>
				<c:out value="${dto.instructions}" escapeXml="false" />
			</td>
		</tr>
	</tbody>
</table>

<c:if test="${not dto.contentInUse}">
	<c:url value="/authoring.do" var="authoringUrl">
		<c:param name="toolContentID" value="${dto.toolContentID}" />
		<c:param name="mode" value="teacher" />
		<c:param name="contentFolderID" value="${contentFolderID}"></c:param>
	</c:url>
	<html:link href="#nogo" onclick="javascript:launchPopup('${fn:escapeXml(authoringUrl)}','definelater')" styleClass="btn btn-default pull-right">
		<fmt:message key="button.editActivity" />
	</html:link>
</c:if>

