<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${contentDTO}" />

<c:if test="${dto.contentInUse}">
	<lams:Alert id="edit" type="info" close="false">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert>
</c:if>

<table class="table table-condensed">
	<tr>
		<td width="20%">
		<fmt:message key="label.authoring.basic.title" />
		</td>
		<td>
		<c:out value="${dto.title}" escapeXml="true" />
		</td>
	</tr>
	<tr>
		<td>
		<fmt:message key="label.authoring.basic.instructions" />
		</td>
		<td>
		<c:out value="${dto.instructions}" escapeXml="false" />
		</td>
	</tr>
</table>

<form id='define-later-form' method='post' action='../authoring/definelater.do' target='definelater'>
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
	<input type="hidden" name="toolContentID" value="${dto.toolContentId}" />
	<input type="hidden" name="contentFolderID" value="${contentFolderID}" />
</form>
		
<a href="#nogo" onclick="javascript:launchDefineLaterPopup()" class="btn btn-default pull-right">
	<fmt:message key="button.editActivity" />
</a>

