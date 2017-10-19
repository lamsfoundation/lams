<%@ include file="/common/taglibs.jsp"%>

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

<c:url var="authoringUrl" value="/authoring/start.do">
	<c:param name="toolContentID" value="${voteMonitoringForm.toolContentID}" />
	<c:param name="contentFolderID" value="${voteMonitoringForm.contentFolderID}" />
	<c:param name="mode" value="teacher" />
</c:url>
<a href="javascript:;" onclick="launchPopup('${authoringUrl}','definelater')" class="btn btn-default pull-right">
	<fmt:message key="label.edit"/>
</a> 
