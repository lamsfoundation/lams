<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<script type="text/javascript">
	function refreshTopic(){
		var reqIDVar = new Date();
		location.href= "<html:rewrite page="/learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${sessionMap.rootUid}&hideReflection=${sessionMap.hideReflection}&reqUid=" />"+reqIDVar.getTime();;
	}
</script>


<div id="content">

	<h1>
		${sessionMap.title}
	</h1>

	<div>
		<div class="right-buttons">
			<c:set var="backToForum">
				<html:rewrite
					page="/learning/viewForum.do?mode=${sessionMap.mode}&sessionMapID=${sessionMapID}&toolSessionID=${sessionMap.toolSessionID}&hideReflection=${sessionMap.hideReflection}" />
			</c:set>
			<html:button property="backToForum"
				onclick="javascript:location.href='${backToForum}';"
				styleClass="button">
				<fmt:message key="label.back.to.forum" />
			</html:button>
		</div>
		<h2>
			<fmt:message key="title.message.view.topic" />
		</h2>

	</div>

	<c:if
		test="${sessionMap.mode == 'author' || sessionMap.mode == 'learner'}">
		<c:if
			test="${not sessionMap.allowNewTopics and (sessionMap.minimumReply ne 0 or sessionMap.maximumReply ne 0)}">
			<div class="info">
				<fmt:message key="label.postingLimits.topic.reminder">
					<fmt:param value="${sessionMap.minimumReply}" />
					<fmt:param value="${sessionMap.maximumReply}" />
					<fmt:param value="${numOfPosts}" />
					<fmt:param value="${sessionMap.maximumReply - numOfPosts}" />
				</fmt:message>
			</div>
		</c:if>
	</c:if>
	<br>
	
	<%@ include file="message/topicview.jsp"%>
	<c:set var="refreshTopicURL">
	</c:set>

	<div class="space-bottom-top">

		<div class="right-buttons">
			<c:set var="backToForum">
				<html:rewrite
					page="/learning/viewForum.do?mode=${sessionMap.mode}&sessionMapID=${sessionMapID}&toolSessionID=${sessionMap.toolSessionID}&hideReflection=${sessionMap.hideReflection}" />
			</c:set>
			<html:button property="backToForum"
				onclick="javascript:location.href='${backToForum}';"
				styleClass="button">
				<fmt:message key="label.back.to.forum" />
			</html:button>
		</div>

		<a href="javascript:refreshTopic();" class="button"> <fmt:message
				key="label.refresh" /> </a>

	</div>
</div>
