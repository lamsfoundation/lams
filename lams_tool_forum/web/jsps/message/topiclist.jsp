<div align="center">
<table width="100%" cellspacing="8" align="CENTER" cellspacing="3" cellpadding="3" class="form">
	<tr>
		<td valign="MIDDLE"><b>Topic</b></td>
		<td colspan="2" />
	</tr>
	<c:forEach items="${topicList}" var="topic">
		<tr>
			<td valign="MIDDLE"><c:out value="${topic.subject}" /></td>
			<td colspan="2" valign="MIDDLE"><html:link
				page="/authoring/forum/deleteTopic.do?topicName=${topic.subject}">
				<b><fmt:message key="label.delete" /></b>
			</html:link></td> 
		</tr>
	</c:forEach>
</table>
</div>
