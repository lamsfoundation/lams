<%@ include file="/includes/taglibs.jsp" %>
	
	<b>Following files have been submitted by 
		 <c:out value="${user.loginName}" /> , <c:out value="${user.firstName}" />  <c:out value="${user.lastName}" /> 	
	</b>
	</p>
<table width="100%"  border="0" cellspacing="0" cellpadding="0">
		<c:forEach items="${topicList}" var="topic" >
			<tr>
				<td valign="MIDDLE" width="48%">
					<c:set var="viewtopic">
						<html:rewrite page="/learning/viewTopic.do?topicId=${topic.message.uid}&create=${topic.message.created.time}" />
					</c:set> 
					<html:link href="${viewtopic}">
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
					<c:out value="${topic.message.replyNumber}"/>
				</td>
				<td>
					<fmt:formatDate value="${topic.message.updated}" type="time" timeStyle="short" />
					<fmt:formatDate value="${topic.message.updated}" type="date" dateStyle="full" />
				</td>
			</tr>
		 <tr>
			<td colspan="2">
			<br>
			<html:form action="/monitoring/editMark" method="post">
					<input type="hidden" name="messageID" value=<c:out value='${topic.message.uid}' /> >
					<input type="hidden" name="toolSessionID" value=<c:out value='${toolSessionID}' /> >
					<input type="hidden" name="userID" value=<c:out value='${user.uid}' /> >
					<input type="submit" value="Update Marks"/>
			</html:form>
			</td>
		</tr>
		</c:forEach>
</table>						

