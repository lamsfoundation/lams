<%@ include file="/includes/taglibs.jsp"%>

<c:if test="${monitoringDTO.totalLearners >= 1}">
	<lams:Alert5 type="warn" id="no-edit">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert5>
</c:if>

<div class="ltable table-condensed no-header">
	<div class="row">
		<div class="col field-name">
			<fmt:message key="basic.title" />
		</div>
		<div class="col">
			<c:out value="${monitoringDTO.title}" escapeXml="true" />
		</div>
	</div>
	<div class="row">
		<div class="col field-name">
			<fmt:message key="basic.content" />
		</div>
		<div class="col">
			<c:out value="${monitoringDTO.basicContent}" escapeXml="false" />
		</div>
	</div>
</div>

<div class="float-end">
	<form id='define-later-form' method='post' action='../authoring/definelater.do' target='definelater'>
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<input type="hidden" name="toolContentID" value="${toolContentID}" />
		<input type="hidden" name="contentFolderID" value="${contentFolderID}" />
	</form>
	
	<button type="button" onclick="launchDefineLaterPopup()" class="btn btn-secondary btn-icon-pen">
		<fmt:message key="button.edit" />
	</button>
</div>

