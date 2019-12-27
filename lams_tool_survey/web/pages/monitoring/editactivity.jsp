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
	<form id='define-later-form' method='post' action='../authoring/definelater.do' target='definelater'>
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<input type="hidden" name="toolContentID" value="${sessionMap.toolContentID}" />
		<input type="hidden" name="contentFolderID" value="${sessionMap.contentFolderID}" />
	</form>
	
	<a href="#nogo" onclick="javascript:launchDefineLaterPopup()" class="btn btn-default pull-right">
		<fmt:message key="label.monitoring.edit.activity.edit" />
	</a>
</c:if>
