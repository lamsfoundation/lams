<%@ include file="/includes/taglibs.jsp" %>
<div align="center">
<div id="topiclist">
	<div id="datatablecontainer">
	<table width="100%" align="CENTER" 	class="form">
		<tr>
			<td valign="MIDDLE"><b><fmt:message key="title.message.view"/></b></td>
			<td colspan="3" />
		</tr>
		<tr>
			<th scope="col" width="50%" colspan="2"><fmt:message key="lable.topic.title.subject"/></th>
			<th scope="col" width="25%"><fmt:message key="lable.topic.title.startedby"/></th>
			<th scope="col" width="25%"><fmt:message key="lable.topic.title.update"/></th>
		</tr>
		<c:forEach items="${topicList}" var="topic" varStatus="status">
			<tr>
				<td valign="MIDDLE" width="48%">
					<c:set var="viewtopic">
						<html:rewrite page="/authoring/viewTopic.do?topicIndex=${status.index}&create=${topic.message.updated.time}" />
					</c:set> 
					<html:link href="javascript:showMessage('${viewtopic}')">
						<c:out value="${topic.message.subject}" />
					</html:link>
				</td>
				<td width="2%">
					<c:if test="${topic.hasAttachment}">
						<img src="<html:rewrite page="/images/paperclip.gif"/>">
					</c:if>
				</td>
				<td>
					<c:out value="${topic.author}"/>
				</td>
				<td>
					<fmt:formatDate value="${topic.message.updated}" type="time" timeStyle="short" />
					<fmt:formatDate value="${topic.message.updated}" type="date" dateStyle="full" />
				</td>
			</tr>
		</c:forEach>
	</table>
	</div>
</div>
</div>
