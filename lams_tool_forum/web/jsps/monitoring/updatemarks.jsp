<%@ include file="/includes/taglibs.jsp"%>

<html:form action="/monitoring/updateMark" method="post">
		<input type="hidden" name="toolSessionID" value="<c:out value='${toolSessionID}'/>" />
		<input type="hidden" name="messageID" value="<c:out value='${topic.message.uid}'/>" />
		<input type="hidden" name="userID" value="<c:out value='${user.uid}'/>" />
		<input type="hidden" name="updateMode" value="${updateMode}" />

	<table cellpadding="0">
		<tr>
			<td colspan="5">
				<fmt:message key="message.assign.mark" />
				<c:out value="${user.loginName}" />
				,
				<c:out value="${user.firstName}" />
				<c:out value="${user.lastName}" />
			</td>
		</tr>

		<tr>
			<td>
				<c:set var="viewtopic">
					<html:rewrite page="/monitoring/viewTopic.do?messageID=${topic.message.uid}&create=${topic.message.created.time}" />
				</c:set>
				<html:link href="javascript:launchPopup('${viewtopic}','viewtopic')">
					<c:out value="${topic.message.subject}" />
				</html:link>
			</td>
			<td>
				<c:if test="${topic.hasAttachment}">
					<img src="<html:rewrite page="/images/paperclip.gif"/>">
				</c:if>
			</td>
			<td>
				<c:set var="author" value="${topic.author}"/>
				<c:if test="${empty author}">
					<c:set var="author">
						<fmt:message key="label.default.user.name"/>
					</c:set>
				</c:if>
				${author}						
			</td>
			<td>
				<c:out value="${topic.message.replyNumber}" />
			</td>
			<td>
				<lams:Date value="${topic.message.updated}"/>
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<td colspan="2">
				<%@ include file="/common/messages.jsp"%>
			</td>
		<tr>
			<td class="field-name" width="100">
				<fmt:message key="lable.topic.title.mark" />* 
			</td>
			<td>
				<html:text property="mark"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />		
				<span  class="field-name"><fmt:message key="lable.topic.title.comment" /><BR></span>
				<lams:FCKEditor id="comment"
					value="${formBean.comment}"
					toolbarSet="Default-Learner"></lams:FCKEditor>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" value="<fmt:message key="lable.update.mark"/>" class="button" />
				&nbsp;&nbsp;
				<c:if test="${updateMode == 'listMarks'}">
					<c:set var="cancelUrl">
						<c:url value="/monitoring/viewUserMark.do"/>?userID=${user.uid}&toolSessionID=${toolSessionID}
					</c:set>
				</c:if>
				<c:if test="${updateMode == 'listAllMarks'}">
					<c:set var="cancelUrl">
						<c:url value="/monitoring/viewAllMarks.do"/>?toolSessionID=${toolSessionID}
					</c:set>
				</c:if>
				<input type="button" onclick="location.href='${cancelUrl}';" class="button" value="<fmt:message key="button.cancel"/>">
			</td>
		</tr>
	</table>
</html:form>
