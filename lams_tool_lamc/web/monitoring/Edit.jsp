<%@ include file="/common/taglibs.jsp"%>

<lams:Alert id="editWarning" type="warning" close="false">
    <fmt:message key="message.monitoring.edit.activity.warning" />
</lams:Alert>

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

<form id='define-later-form' method='post' action='../authoring/definelater.do' target='definelater'>
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
	<input type="hidden" name="toolContentID" value="${mcGeneralMonitoringDTO.toolContentID}" />
	<input type="hidden" name="contentFolderID" value="${contentFolderID}" />
</form>
	
<a href="#nogo" onclick="javascript:launchDefineLaterPopup()" class="btn btn-default pull-right">
	<fmt:message key="label.edit" />
</a>			 		  					
