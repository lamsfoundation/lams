<%@ include file="/taglibs.jsp"%>

<h2>
	<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
	: <fmt:message key="sysadmin.edit.default.tool.content"/></h2>
<p>&nbsp;</p>

<p>
<fmt:message key="msg.edit.tool.content.1" />&nbsp;&nbsp;<fmt:message key="msg.edit.tool.content.2" />
</p>

<p>
<table class="alternative-color">
<logic:iterate name="activeTools" id="dto">
	<tr>
		<td>
			<c:out value="${dto.activityTitle}" />
		</td>
		<td>
			<a href="<lams:LAMSURL /><c:out value="${dto.authoringURL}" />?toolContentID=<c:out value="${dto.toolContentID}" />&contentFolderID=-1">
			<c:out value="${dto.toolDisplayName}" />
			</a>
		</td>
	</tr>
</logic:iterate>
</table>
</p>