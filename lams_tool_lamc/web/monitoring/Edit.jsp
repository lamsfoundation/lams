<%@ include file="/common/taglibs.jsp"%>

<table class="table table-condensed">
	<tr>
		<td width="10%" ><fmt:message key="label.authoring.title.col"></fmt:message></td>
		<td><c:out value="${mcGeneralMonitoringDTO.activityTitle}" escapeXml="false"/></td>
	</tr>		

	<tr>
		<td width="10%" ><fmt:message key="label.authoring.instructions.col"></fmt:message></td>
		<td><c:out value="${mcGeneralMonitoringDTO.activityInstructions}" escapeXml="false"/></td>
	</tr>
</table>

<c:url  var="authoringUrl" value="/authoringStarter.do">
	<c:param name="toolContentID" value="${mcGeneralMonitoringDTO.toolContentID}" />
	<c:param name="contentFolderID" value="${contentFolderID}" />
	<c:param name="mode" value="teacher" />
</c:url>
<html:link href="javascript:;" onclick="launchPopup('${authoringUrl}','definelater')" styleClass="btn btn-default pull-right">
	<fmt:message key="label.edit"/>
</html:link>				 		  					
