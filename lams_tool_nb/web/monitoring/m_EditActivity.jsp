<%@ include file="/includes/taglibs.jsp"%>

<div class="voffset10">

<c:if test="${monitoringDTO.totalLearners >= 1}">
	<lams:Alert type="warn" id="no-edit" close="false">
		<fmt:message key="message.alertContentEdit" />
	</lams:Alert>
</c:if>

<table class="table table-condensed">
	<tr>
		<td class="field-name">
			<fmt:message key="basic.title" />
		</td>
		<td>
			<c:out value="${monitoringDTO.title}" escapeXml="true" />
		</td>
	</tr>
	<tr>
		<td class="field-name">
			<fmt:message key="basic.content" />
		</td>
		<td>
			<c:out value="${monitoringDTO.basicContent}" escapeXml="false" />
		</td>
	</tr>
</table>

<p class="align-right">
	<form id='define-later-form' method='post' action='../authoring/definelater.do' target='definelater'>
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<input type="hidden" name="toolContentID" value="${toolContentID}" />
		<input type="hidden" name="contentFolderID" value="${contentFolderID}" />
	</form>
		
	<a href="#nogo" onclick="javascript:launchDefineLaterPopup()" class="btn btn-default pull-right">
		<fmt:message key="button.edit" />
	</a>
</p>

</div>

