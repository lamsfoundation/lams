<%@ include file="/common/taglibs.jsp"%>

<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<table class="table table-condensed">
	<tr>
		<td class="field-name" width="10%" valign="top">
			<fmt:message key="label.authoring.title" />
		</td>
		<td>
			<c:out value="${content.title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td class="field-name" width="10%" valign="top" NOWRAP>
			<fmt:message key="label.authoring.instructions" />
		</td>
		<td>
			<c:out value="${content.instructions}" escapeXml="false" />
		</td>
	</tr>
</table>

<c:url value="/authoringStarter.do" var="authoringUrl">
	<c:param name="mode" value="teacher" />
	<c:param name="toolContentID" value="${formBean.toolContentID}" />
	<c:param name="contentFolderID" value="${formBean.contentFolderID}" />
</c:url>
<html:link href="javascript:;" onclick="launchPopup('${authoringUrl}','definelater')" styleClass="btn btn-default pull-right">
	<fmt:message key="label.edit" />
</html:link>