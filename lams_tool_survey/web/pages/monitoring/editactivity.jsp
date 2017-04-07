<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="survey" value="${sessionMap.survey}"/>
<c:set var="isPageEditable" value="${sessionMap.isPageEditable}" />

<c:if test="${!isPageEditable}">
	<lams:Alert type="warn" id="no-edit" close="false">
		<fmt:message key="message.monitoring.edit.activity.not.editable" />
	</lams:Alert>
</c:if>

<table class="table table-condensed">
	<tr>
		<td nowrap width="10%">
			<fmt:message key="label.authoring.basic.title" />
			:
		</td>
		<td>
			<c:out value="${survey.title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td nowrap width="10%" valign="top">
			<fmt:message key="label.authoring.basic.instruction" />
			:
		</td>
		<td>
			<c:out value="${survey.instructions}" escapeXml="false" />
		</td>
	</tr>
</table>


<c:if test="${isPageEditable}">
	<c:url  var="authoringUrl" value="/definelater.do">
		<c:param name="toolContentID" value="${sessionMap.toolContentID}" />
		<c:param name="contentFolderID" value="${sessionMap.contentFolderID}" />
	</c:url>
	<html:link href="javascript:;" onclick="launchPopup('${authoringUrl}','definelater')" styleClass="btn btn-default pull-right">
		<fmt:message key="label.monitoring.edit.activity.edit" />
	</html:link>
</c:if>
