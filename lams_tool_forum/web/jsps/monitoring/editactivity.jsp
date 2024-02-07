<%@ include file="/common/taglibs.jsp"%>

<lams:errors5/>
		
<c:if test="${!isPageEditable}">
	<lams:Alert5 type="warn" id="no-edit">
		<fmt:message key="message.monitoring.edit.activity.not.editable" />
	</lams:Alert5>
</c:if>

<div class="ltable no-header">
	<div class="row">
		<div class="col-2">
			<fmt:message key="label.authoring.basic.title" />
		</div>
		<div class="col">
			<c:out value="${title}" escapeXml="true" />
		</div>
	</div>

	<div class="row">
		<div class="col-2">
			<fmt:message key="label.authoring.basic.instruction" />
		</div>
		<div class="col">
			<c:out value="${instruction}" escapeXml="false" />
		</div>
	</div>
</div>

<c:if test='${isPageEditable}'>
	<div class="float-end">
		<form id='define-later-form' method='post' action='../authoring/definelater.do' target='definelater'>
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<input type="hidden" name="toolContentID" value="${toolContentID}" />
			<input type="hidden" name="contentFolderID" value="${contentFolderID}" />
		</form>
		
		<button type="button" onclick="launchDefineLaterPopup()" class="btn btn-secondary btn-icon-pen">
			<fmt:message key="label.monitoring.edit.activity.edit" />
		</button>
	</div>
</c:if>

