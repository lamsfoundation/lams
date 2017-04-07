<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="daco" value="${sessionMap.daco}"/>

<div class="voffset10">

<c:if test="${!sessionMap.isPageEditable}">
	<lams:Alert type="warn" id="no-edit" close="false">
		<fmt:message key="message.learning.alertContentEdit" />
	</lams:Alert>
</c:if>

<table class="table table-condensed">
	<tr>
		<td width="10%" nowrap>
			<fmt:message key="label.authoring.basic.title" />
			:
		</td>
		<td>
			<c:out value="${daco.title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td width="10%" nowrap valign="top">
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
				<a href="#" onclick="javascript:launchPopup('${authoringUrl}','definelater')" class="btn btn-default pull-right">
					<fmt:message key="label.monitoring.edit.activity.edit" />
				</a>
			</td>
		</tr>
	</c:if>
</table>

</div>