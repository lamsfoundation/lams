<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="taskList" value="${sessionMap.taskList}"/>

<c:if test="${sessionMap.isPageEditable}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert>
</c:if>

<table class="table table-condensed table-no-border">
	<tr>
		<td width="15%" nowrap>
			<fmt:message key="label.authoring.basic.title" />
			:
		</td>
		<td>
			<c:out value="${taskList.title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td nowrap width="15%">
			<fmt:message key="label.authoring.basic.description" />
			:
		</td>
		<td>
			<c:out value="${taskList.instructions}" escapeXml="false" />
		</td>
	</tr>
</table>

<form id='define-later-form' method='post' action='../authoring/definelater.do' target='definelater'>
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
	<input type="hidden" name="toolContentID" value="${sessionMap.toolContentID}" />
	<input type="hidden" name="contentFolderID" value="${sessionMap.contentFolderID}" />
</form>
	
<a href="#nogo" onclick="javascript:launchDefineLaterPopup()" class="btn btn-default pull-right">
	<fmt:message key="label.monitoring.edit.activity.edit" />
</a>
