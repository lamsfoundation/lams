<!DOCTYPE html>

<%@ page import="org.lamsfoundation.lams.tool.forum.ForumConstants"%>

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="pageSize" value="<%=ForumConstants.DEFAULT_PAGE_SIZE%>" />

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>

	<!-- ********************  CSS ********************** -->
	<lams:css />
	<lams:css suffix="jquery.jRating"/>
	<lams:css suffix="treetable"/>
	<link type="text/css" href="${tool}css/treetable.forum.css" rel="stylesheet" />

	<!-- ********************  javascript ********************** -->
	<lams:JSImport src="includes/javascript/common.js" />
	<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>

	<script type="text/javascript">
		var removeItemAttachmentUrl = "<lams:WebAppURL />learning/deleteAttachment.do";
		//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/";
	</script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.treetable.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jscroll.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/message.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$.ajaxSetup({ cache: true });
			setupJRatingSetPath();
		});

		function setupJRatingSetPath() {
				setupJRating("<c:url value='/learning/rateMessage.do'/>?toolSessionID=${sessionMap.toolSessionID}&sessionMapID=${sessionMapID}");
			}
		
		<c:set var="refresh">
			<lams:WebAppURL />learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${sessionMap.rootUid}&hideReflection=${sessionMap.hideReflection}&pageLastId=0&size=${pageSize}&reqUid=${reqIDVar.getTime()};
		</c:set>
		
		function refreshTopic(){
				var reqIDVar = new Date();
				location.href= '${refresh}';
			}
		</script>
</lams:head>
<body class="stripes">
	<lams:Page type="learner" title="${sessionMap.title}">

		<!-- Announcements and advanced settings -->
		<c:if test="${sessionMap.mode == 'author' || sessionMap.mode == 'learner'}">

			<c:if test="${not sessionMap.allowNewTopics and ( sessionMap.minimumReply ne 0 or sessionMap.maximumReply ne 0)}">
				<lams:Alert id="postingLimits" type="info" close="true">
					<c:if test="${(sessionMap.minimumReply ne 0 and sessionMap.maximumReply ne 0)}">
						<fmt:message key="label.postingLimits.topic.reminder">
							<fmt:param value="${sessionMap.minimumReply}" />
							<fmt:param value="${sessionMap.maximumReply}" />
							<fmt:param value="${numOfPosts}" />
							<fmt:param value="${sessionMap.maximumReply - numOfPosts}" />
						</fmt:message>
					</c:if>
					<c:if test="${(sessionMap.minimumReply ne 0 and sessionMap.maximumReply eq 0)}">
						<fmt:message key="label.postingLimits.topic.reminder.min">
							<fmt:param value="${sessionMap.minimumReply}" />
							<fmt:param value="${numOfPosts}" />
							<fmt:param value="${sessionMap.maximumReply - numOfPosts}" />
						</fmt:message>
					</c:if>
					<c:if test="${(sessionMap.minimumReply eq 0 and sessionMap.maximumReply ne 0)}">
						<fmt:message key="label.postingLimits.topic.reminder.max">
							<fmt:param value="${sessionMap.maximumReply}" />
							<fmt:param value="${numOfPosts}" />
							<fmt:param value="${sessionMap.maximumReply - numOfPosts}" />
						</fmt:message>
					</c:if>
				</lams:Alert>
			</c:if>

			<!-- Rating announcements -->
			<c:if test="${sessionMap.allowRateMessages}">
				<lams:Alert id="rateMessages" type="info" close="true">
					<c:choose>
						<c:when test="${sessionMap.minimumRate ne 0 and sessionMap.maximumRate ne 0}">
							<fmt:message key="label.rateLimits.forum.reminder">
								<fmt:param value="${sessionMap.minimumRate}" />
								<fmt:param value="${sessionMap.maximumRate}" />
							</fmt:message>
						</c:when>

						<c:when test="${sessionMap.minimumRate ne 0 and sessionMap.maximumRate eq 0}">
							<fmt:message key="label.rateLimits.forum.reminder.min">
								<fmt:param value="${sessionMap.minimumRate}" />
							</fmt:message>
						</c:when>

						<c:when test="${sessionMap.minimumRate eq 0 and sessionMap.maximumRate ne 0}">
							<fmt:message key="label.rateLimits.forum.reminder.max">
								<fmt:param value="${sessionMap.maximumRate}" />
							</fmt:message>
						</c:when>
					</c:choose>
					<br>

					<fmt:message key="label.rateLimits.topic.reminder">
						<fmt:param value="<span id='numOfRatings'>${sessionMap.numOfRatings}</span>" />
					</fmt:message>

				</lams:Alert>
			</c:if>
		</c:if>
		<!-- End announcements and advanced settings -->

		<c:set var="buttonPanel">
			<c:set var="refreshTopicURL"></c:set>
				<!--  Button Panel -->
				<div class="row g-0">
					<div class="col-12">
						<a href="javascript:refreshTopic();" class="btn btn-sm btn-secondary mt-2  float-start" role="button"> <fmt:message
								key="label.refresh" />
						</a>
						<c:set var="backToForum">
							<lams:WebAppURL />learning/viewForum.do?mode=${sessionMap.mode}&sessionMapID=${sessionMapID}&toolSessionID=${sessionMap.toolSessionID}&hideReflection=${sessionMap.hideReflection}
						</c:set>
						<button name="backToForum" onclick="javascript:location.href='${backToForum}';"
							class="btn btn-sm btn-primary mt-2 float-end">
							<fmt:message key="label.back.to.forum" />
						</button>
					</div>
				</div>
				<!--  Button Panel -->
		</c:set>
		
		${buttonPanel}

		<div class="scroll">
			<%@ include file="message/topicview.jsp"%>
		</div>
		<script>
			<c:set var="loading_words"><fmt:message key="label.loading.messages" /></c:set>
			$('.scroll' ).jscroll({loadingHtml: '<i class="fa fa-refresh fa-spin fa-fw"></i> ${loading_words}',padding:30,autoTrigger:true,callback:setupJRatingSetPath});
		</script>
			
		${buttonPanel}

	</lams:Page>

</body>
</lams:html>