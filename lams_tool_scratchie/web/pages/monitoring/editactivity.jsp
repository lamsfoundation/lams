<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="scratchie" value="${sessionMap.scratchie}"/>

<lams:Alert id="editWarning" type="warning" close="false">
    <fmt:message key="message.monitoring.edit.activity.warning" />
</lams:Alert>

<table class="table table-condensed">
	<tr>
		<td width="10%" nowrap>
			<fmt:message key="label.authoring.basic.title" />
			:
		</td>
		<td>
			<c:out value="${scratchie.title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td width="10%" valign="true" nowrap>
			<fmt:message key="label.authoring.basic.instruction" />
			:
		</td>
		<td>
			<c:out value="${scratchie.instructions}" escapeXml="false" />
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
