<%@ include file="/includes/taglibs.jsp" %>
<div id="topiclist">
	<table class="alternative-color">
		<tr>
			<th scope="col" width="50%" colspan="2"><fmt:message key="lable.topic.title.subject"/></th>
			<th scope="col" width="20%" align="center"><fmt:message key="lable.topic.title.startedby"/></th>
			<th scope="col" width="30%" align="center"><fmt:message key="lable.topic.title.update"/></th>
		</tr>
		<c:forEach items="${topicList}" var="topic" varStatus="status">
			<tr>
				<td align="left" width="48%">
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
				<td align="center">
					<c:out value="${topic.author}"/>
				</td>
				<td align="center">
					<fmt:formatDate value="${topic.message.updated}" type="time" timeStyle="short" />
					<fmt:formatDate value="${topic.message.updated}" type="date" pattern="MMM dd"/>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
