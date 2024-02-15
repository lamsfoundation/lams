<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${leaderselectionDTO}" />

<c:if test="${dto.contentInUse}">
	<lams:Alert5 type="info" id="content-in-use">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert5>
</c:if>

<div class="ltable no-header">
	<div class="row">
		<div class="col-2">
			<fmt:message key="label.authoring.basic.title" />
		</div>
		<div class="col">
			<c:out value="${dto.title}" escapeXml="true" />
		</div>
	</div>
	<div class="row">
		<div class="col-2">
			<fmt:message key="label.authoring.basic.instructions" />
		</div>
		<div class="col">
			<c:out value="${dto.instructions}" escapeXml="false" />
		</div>
	</div>
</div>

<div class="float-end">
	<form id='define-later-form' method='post' action='authoring/definelater.do' target='definelater'>
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<input type="hidden" name="toolContentID" value="${dto.toolContentId}" />
		<input type="hidden" name="contentFolderID" value="${contentFolderID}" />
	</form>
		
	<button type="button" onclick="javascript:launchDefineLaterPopup()" class="btn btn-secondary btn-icon-pen">
		<fmt:message key="button.editActivity" />
	</button>
</div>
