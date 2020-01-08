<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${leaderselectionDTO}" />

<c:if test="${dto.contentInUse}">
	<lams:Alert type="info" id="content-in-use" close="false">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert>
</c:if>

<table class="table table-condensed">
	<tbody>
		<tr>
			<td class="field-name" width="10%" nowrap>
				<fmt:message key="label.authoring.basic.title" />
			</td>
			<td>
				<c:out value="${dto.title}" escapeXml="true" />
			</td>
		</tr>
		<tr>
			<td class="field-name" width="10%" nowrap valign="top">
				<fmt:message key="label.authoring.basic.instructions" />
			</td>
			<td>
				<c:out value="${dto.instructions}" escapeXml="false" />
			</td>
		</tr>
	</tbody>
</table>

<p class="align-right">
	<form id='define-later-form' method='post' action='authoring/definelater.do' target='definelater'>
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<input type="hidden" name="toolContentID" value="${dto.toolContentId}" />
		<input type="hidden" name="contentFolderID" value="${contentFolderID}" />
	</form>
		
	<a href="#nogo" onclick="javascript:launchDefineLaterPopup()" class="btn btn-default pull-right">
		<fmt:message key="button.editActivity" />
	</a>
</p>


