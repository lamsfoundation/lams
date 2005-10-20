<div align="center"><script type="text/javascript"
	src="<html:rewrite page='/includes/javascript/xmlrequest.js'/>"></script>
<div id="topiclist">
<table width="100%" cellspacing="8" align="CENTER" cellspacing="3"
	cellpadding="3" class="form">
	<tr>
		<td valign="MIDDLE"><b>Topic</b></td>
		<td colspan="2" />
	</tr>
	<c:forEach items="${topicList}" var="topic">
		<tr>
			<td valign="MIDDLE"><c:out value="${topic.subject}" /></td>
			<td colspan="2" valign="MIDDLE">
			<c:set var="deletetopic">
				<html:rewrite page="/authoring/deleteTopic.do?topicName=${topic.subject}" />
			</c:set> 
			<html:link href="javascript:loadDoc('${deletetopic}','topiclist')">
				<b><fmt:message key="label.delete" /></b>
			</html:link>
			</td>
		</tr>
	</c:forEach>
</table>
</div>
</div>
