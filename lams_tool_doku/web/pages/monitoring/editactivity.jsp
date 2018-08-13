<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="dokumaran" value="${sessionMap.dokumaran}"/>

<c:if test="${sessionMap.isPageEditable}">
	<lams:Alert type="warn" id="no-edit" close="false">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert>
</c:if>

<table class="table table-condensed">
	<tr>
		<td width="15%" nowrap>
			<fmt:message key="label.authoring.basic.title" />:
		</td>
		<td>
			<c:out value="${dokumaran.title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td width="15%" nowrap>
			<fmt:message key="label.authoring.basic.instruction" />:
		</td>
		<td>
			<c:out value="${dokumaran.instructions}" escapeXml="false" />
		</td>
	</tr>

	<tr>
		<td colspan="2">
			<c:url  var="authoringUrl" value="/authoring/definelater.do">
				<c:param name="toolContentID" value="${sessionMap.toolContentID}" />
				<c:param name="contentFolderID" value="${sessionMap.contentFolderID}" />
			</c:url>
			<a href="javascript:;" onclick="launchPopup('${authoringUrl}','definelater')" class="btn btn-default pull-right">
				<fmt:message key="label.monitoring.edit.activity.edit" />
			</a>
		</td>
	</tr>
</table>
