<%@ include file="/common/taglibs.jsp"%>
<table cellpadding="0">
<c:if test="${empty report}">
	<tr>
		<td>
			<div align="center">
				<b><fmt:message key="message.not.avaliable" /></b>
			</div>
		</td>
	</tr>
</c:if>
<c:forEach items="${report}" var="userList">
	<c:set var="user" value="${userList.key}" />
	<c:set var="topicList" value="${userList.value}" />
		<c:if test="${empty topicList}">
			<tr>
				<td>
					<div align="center">
						<b><fmt:message key="message.not.avaliable" /></b>
					</div>
				</td>
			</tr>
		</c:if>
	
	
		<c:forEach items="${topicList}" var="topic" varStatus="status">
			<c:if test="${status.first}">
				<tr>
					<td colspan="5">
						<c:out value="${user.loginName}" />
						,
						<c:out value="${user.firstName}" />
						<c:out value="${user.lastName}" />
						<fmt:message key="monitoring.user.post.topic" />
					</td>
				</tr>
			</c:if>
			<tr>
				<td>
					<c:set var="viewtopic">
						<html:rewrite page="/monitoring/viewTopic.do?topicID=${topic.message.uid}&create=${topic.message.created.time}&hideReflection=${sessionMap.hideReflection}" />
					</c:set>
					<html:link href="javascript:launchPopup('${viewtopic}','viewtopic');">
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
					<lams:Date  value="${topic.message.updated}"/>
				</td>
			</tr>
	
			<tr>
				<td class="field-name" width="30%">
					<fmt:message key="lable.topic.title.mark" />
					:
				</td>
				<td colspan="4">
					<c:choose>
						<c:when test="${empty topic.message.report.mark}">
							<fmt:message key="message.not.avaliable" />
						</c:when>
						<c:otherwise>
							<fmt:formatNumber value="${topic.message.report.mark}"  maxFractionDigits="2"/>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td class="field-name" width="30%">
					<fmt:message key="lable.topic.title.comment" />
					:
				</td>
				<td colspan="4">
					<c:choose>
						<c:when test="${empty topic.message.report.comment}">
							<fmt:message key="message.not.avaliable" />
						</c:when>
						<c:otherwise>
							<c:out value="${topic.message.report.comment}" escapeXml="false" />
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td colspan="5">
					<div style="float:left;padding:5px;">
					<html:form action="/monitoring/editMark" method="post">
						<html:hidden property="sessionMapID" value="${sessionMapID}"/>
						<html:hidden property="topicID" value="${topic.message.uid}"/>
						<input type="submit" value="<fmt:message key="lable.update.mark"/>" class="button" />
					</html:form>
					</div>
				</td>
			</tr>
		</c:forEach>
		<tr>
			<td>
				<div style="float:left;padding:5px;">
				<a href="javascript:closeAndRefreshParentMonitoringWindow()" class="button"><fmt:message key="button.close"/></a>
				</div>
			</td>
		</tr>
</c:forEach>
</table>
