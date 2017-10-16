<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<lams:Alert id="editWarning" type="warning" close="false">
    <fmt:message key="message.monitoring.edit.activity.warning" />
</lams:Alert>

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

<c:url  var="authoringUrl" value="/authoring.do?dispatch=start">
	<c:param name="toolContentID" value="${formBean.toolContentID}" />
	<c:param name="contentFolderID" value="${formBean.contentFolderID}" />
	<c:param name="mode" value="teacher" />
</c:url>
<html:link href="javascript:;" onclick="launchPopup('${authoringUrl}','definelater')" styleClass="btn btn-default pull-right">
	<fmt:message key="label.edit"/>
</html:link> 
