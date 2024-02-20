<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="assessment" value="${sessionMap.assessment}"/>

<lams:Alert5 id="editWarning" type="warning">
    <fmt:message key="message.monitoring.edit.activity.warning" />
</lams:Alert5>

<div class="ltable no-header">
	<div class="row">
		<div class="col-2">
			<fmt:message key="label.authoring.basic.title" />:
		</div>
		<div class="col">
			<c:out value="${assessment.title}" escapeXml="true" />
		</div>
	</div>

	<div class="row">
		<div class="col-2">
			<fmt:message key="label.authoring.basic.instruction" />:
		</div>
		<div class="col">
			<c:out value="${assessment.instructions}" escapeXml="false" />
		</div>
	</div>
</div>

<div class="float-end">
	<form id='define-later-form' method='post' action='../authoring/definelater.do' target='definelater'>
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<input type="hidden" name="toolContentID" value="${sessionMap.toolContentID}" />
		<input type="hidden" name="contentFolderID" value="${sessionMap.contentFolderID}" />
	</form>

	<button type="button" onclick="launchDefineLaterPopup()" class="btn btn-secondary btn-icon-pen">
		<fmt:message key="label.monitoring.edit.activity.edit" />
	</button>
</div>
