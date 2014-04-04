<%@ include file="/common/taglibs.jsp"%>


<html:form action="/monitoring/updateMark" method="post">
	<c:set var="formBean"
		value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
	<html:hidden property="sessionMapID"/>
	<html:hidden property="topicID"/>
	<html:hidden property="hideReflection" value="${sessionMap.hideReflection}"/>

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
					<html:rewrite page="/monitoring/viewTopic.do?topicID=${topic.message.uid}&create=${topic.message.created.time}&hideReflection=${sessionMap.hideReflection}" />
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
				<c:out value="${author}" escapeXml="true"/>						
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
				<lams:CKEditor id="comment"
					value="${formBean.comment}"
					toolbarSet="DefaultMonitor"></lams:CKEditor>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div style="float:left;padding:5px;">
					<input type="submit" value="<fmt:message key="lable.update.mark"/>" class="button" />
				</div>
				<div style="float:left;padding:5px;">
					<c:if test="${sessionMap.updateMode == 'listMarks'}">
						<c:set var="cancelUrl">
							<c:url value="/monitoring/viewUserMark.do"/>?userID=${user.uid}&toolSessionID=${sessionMap.toolSessionID}
						</c:set>
					</c:if>
					<c:if test="${sessionMap.updateMode == 'listAllMarks'}">
						<c:set var="cancelUrl">
							<c:url value="/monitoring/viewAllMarks.do"/>?toolSessionID=${sessionMap.toolSessionID}
						</c:set>
					</c:if>
					<c:if test="${sessionMap.updateMode == 'viewForum'}">
						<c:set var="cancelUrl">
							<c:url value="/learning/viewTopic.do"/>?sessionMapID=${formBean.sessionMapID}&topicID=${sessionMap.rootUid}&hideReflection=${sessionMap.hideReflection}
						</c:set>
					</c:if>
					<input type="button" onclick="location.href='${cancelUrl}';" class="button" value="<fmt:message key="button.cancel"/>">
				</div>
			</td>
		</tr>
	</table>
</html:form>
