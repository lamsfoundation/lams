<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="peerreview" value="${sessionMap.peerreview}"/>


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
			<c:out value="${peerreview.title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td width="15%" nowrap>
			<fmt:message key="label.authoring.basic.instruction" />:
		</td>
		<td>
			<c:out value="${peerreview.instructions}" escapeXml="false" />
		</td>
	</tr>

	<tr>
		<td colspan="2">
			<c:url  var="authoringUrl" value="/definelater.do">
				<c:param name="toolContentID" value="${sessionMap.toolContentID}" />
				<c:param name="contentFolderID" value="${sessionMap.contentFolderID}" />
			</c:url>
			<html:link href="javascript:;" onclick="launchPopup('${authoringUrl}','definelater')" styleClass="btn btn-default">
				<fmt:message key="label.monitoring.edit.activity.edit" />
			</html:link>
		</td>
	</tr>
</table>
