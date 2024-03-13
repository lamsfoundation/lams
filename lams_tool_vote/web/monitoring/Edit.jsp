<%@ include file="/common/taglibs.jsp"%>

<lams:Alert5 id="editWarning" type="warning">
    <fmt:message key="message.monitoring.edit.activity.warning" />
</lams:Alert5>

<table class="table table-condensed">
	<tr>
		<td width="15%"><fmt:message key="label.authoring.title.col"></fmt:message></td>
		<td><c:out value="${voteGeneralAuthoringDTO.activityTitle}" escapeXml="true"/></td>
	</tr>
						
	<tr>
		<td><fmt:message key="label.authoring.instructions.col"></fmt:message></td>
		<td><c:out value="${voteGeneralAuthoringDTO.activityInstructions}" escapeXml="false"/></td>
	</tr>
	
	<c:forEach items="${listQuestionDTO}" var="currentDTO" varStatus="status">
		<tr>
			<td>
				<fmt:message key="label.nomination" />:
			</td>
			<td>
				<c:out value="${currentDTO.question}" escapeXml="false"/> 
			</td>
		</tr>
	</c:forEach>
</table>

<form id='define-later-form' method='post' action='../authoring/definelater.do' target='definelater'>
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
	<input type="hidden" name="toolContentID" value="${toolContentID}" />
	<input type="hidden" name="contentFolderID" value="${contentFolderID}" />
</form>
	
<a href="#nogo" onclick="javascript:launchDefineLaterPopup()" class="btn btn-default pull-right">
	<fmt:message key="label.edit" />
</a>

