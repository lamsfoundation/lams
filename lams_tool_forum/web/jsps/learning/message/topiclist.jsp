<%@ page import="org.lamsfoundation.lams.tool.forum.util.ForumConstants"%>
<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/jquery.timeago.js"></script>

<c:set var="pageSize" value="<%=ForumConstants.DEFAULT_PAGE_SIZE%>" />
<div class="panel panel-default">
	<div class="panel-heading panel-title">
		<fmt:message key="label.topics" />

		<div class="btn-group pull-right">
			<a href="javascript:location.href='${refresh}';" type="button" class="btn btn-xs btn-default"><i class="fa fa-xm fa-refresh"></i>
				<fmt:message key="label.refresh" /></a>

			<c:if test='${sessionMap.allowNewTopics}'>
				<a href="javascript:location.href='${newtopic}';" type="button" class="btn btn-xs btn-default"> <i
					class="fa fa-xm fa-plus"></i> <fmt:message key="label.newtopic" /></a>
			</c:if>
		</div>


	</div>
	<div class="panel-body">
		<div class="table-responsive">
			<table id="topicTable" class="table table-hover table-condensed">
				<thead>
					<tr>
						<th width=""><fmt:message key="lable.topic.title.subject" /></th>
						<th width="15%"><fmt:message key="lable.topic.title.startedby" /></th>
						<th width="8%"><fmt:message key="lable.topic.title.replies" /></th>
						<th width="8%"><fmt:message key="lable.topic.title.repliesnew" /></th>
						<th width="20%"><fmt:message key="lable.topic.title.update" /></th>
						<c:if test="${not sessionMap.allowNewTopics and sessionMap.minimumReply ne 0}">
							<th width="8%">&nbsp;</th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${topicList}" var="topic">
						<tr>
							<td><c:set var="viewtopic">
									<html:rewrite
										page="/learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${topic.message.uid}&create=${topic.message.created.time}&hideReflection=${sessionMap.hideReflection}&pageLastId=0&size=${pageSize}" />
								</c:set> <html:link href="${viewtopic}" styleId="topicTitle">
									<c:choose>
										<c:when test="${topic.newPostingsNum > 0}">
											<b><c:out value="${topic.message.subject}" /></b>
										</c:when>
										<c:otherwise>
											<c:out value="${topic.message.subject}" />
										</c:otherwise>
									</c:choose>
								</html:link> <c:if test="${topic.hasAttachment}">
									<i class="fa fa-paperclip"></i>
								</c:if></td>
							<td><c:set var="author" value="${topic.author}" /> <c:if test="${empty author}">
									<c:set var="author">
										<fmt:message key="label.default.user.name" />
									</c:set>
								</c:if> <c:out value="${author}" escapeXml="true" /></td>

							<td align="center"><c:out value="${topic.message.replyNumber}" /></td>

							<td align="center"><c:out value="${topic.newPostingsNum}" /></td>
							<td><time class="timeago"
									datetime="<fmt:formatDate value='${topic.message.lastReplyDate}' pattern="yyyy-MM-dd'T'HH:mm:ss.S'Z'"/>">
									<lams:Date value="${topic.message.lastReplyDate}" />
								</time></td>
							<c:if test="${not sessionMap.allowNewTopics and sessionMap.minimumReply ne 0}">
								<td>${topic.numOfPosts}/${sessionMap.minimumReply}</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>

<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery("time.timeago").timeago();
	});
</script>