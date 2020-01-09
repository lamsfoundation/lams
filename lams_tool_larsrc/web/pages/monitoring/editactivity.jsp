<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="resource" value="${sessionMap.resource}"/>

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
			<c:out value="${resource.title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td width="15%" nowrap>
			<fmt:message key="label.authoring.basic.instruction" />:
		</td>
		<td>
			<c:out value="${resource.instructions}" escapeXml="false" />
		</td>
	</tr>

	<tr>
		<td colspan="2">
			<form id='define-later-form' method='post' action='../authoring/definelater.do' target='definelater'>
				<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
				<input type="hidden" name="toolContentID" value="${sessionMap.toolContentID}" />
				<input type="hidden" name="contentFolderID" value="${sessionMap.contentFolderID}" />
			</form>
	
			<a href="#nogo" onclick="javascript:launchDefineLaterPopup()" class="btn btn-default pull-right">
				<fmt:message key="label.monitoring.edit.activity.edit" />
			</a>
		</td>
	</tr>
</table>
