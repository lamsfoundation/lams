<%@ include file="/includes/taglibs.jsp"%>

<div class="voffset10">

<c:if test="${NbMonitoringForm.totalLearners >= 1}">
	<lams:Alert type="warn" id="no-edit" close="false">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert>
</c:if>

<table class="table table-condensed">
	<tr>
		<td class="field-name">
			<fmt:message key="basic.title" />
		</td>
		<td>
			<c:out value="${NbMonitoringForm.title}" escapeXml="true" />
		</td>
	</tr>
	<tr>
		<td class="field-name">
			<fmt:message key="basic.content" />
		</td>
		<td>
			<c:out value="${NbMonitoringForm.basicContent}" escapeXml="false" />
		</td>
	</tr>
</table>

<p class="align-right">
	<c:url value="/authoring.do" var="authoringUrl">
		<c:param name="toolContentID" value="${toolContentID}" />
		<c:param name="defineLater" value="true" />
		<c:param name="contentFolderID" value="${contentFolderID}" />
	</c:url>
	<a href="#nogo" onclick="javascript:launchPopup('${authoringUrl}','definelater')" class="btn btn-default pull-right">
		<fmt:message key="button.edit" />
	</a>
</p>

</div>

