<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>
		
		<!-- ********************  CSS ********************** -->
		<c:choose>
			<c:when test="${not empty localLinkPath}">
				<lams:css localLinkPath="${localLinkPath}" />
			</c:when>
			<c:otherwise>
				<lams:css />
			</c:otherwise>
		</c:choose>
		<link rel="stylesheet" href="<html:rewrite page='/includes/css/jRating.jquery.css'/>"  type="text/css" />
		<link rel="stylesheet" href="<html:rewrite page='/includes/css/ratingStars.css'/>"  type="text/css" />
		
		<!-- ********************  javascript ********************** -->
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
		
		<script type="text/javascript">
			var pathToImageFolder = "<html:rewrite page='/images/'/>"; 
			var removeItemAttachmentUrl = "<html:rewrite page="/learning/deleteAttachment.do" />";
		</script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jRating.jquery.js'/>"></script>
		<script type="text/javascript" src="${tool}includes/javascript/message.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$(".ratingStars").jRating({
				    phpPath : "<c:url value='/learning/rateMessage.do'/>?toolSessionID=${sessionMap.toolSessionID}",
				    rateMax : 5,
				    decimalLength : 1,
					onSuccess : function(data, messageId){
					    $("#averageRating" + messageId).html(data.averageRating);
					    $("#numberOfVotes" + messageId).html(data.numberOfVotes);
					},
					onError : function(){
					    jError('Error : please retry');
					}
				});
			    $(".ratingStarsDisabled").jRating({
			    	rateMax : 5,
			    	isDisabled : true
				});
			});
	
			function refreshTopic(){
				var reqIDVar = new Date();
				location.href= "<html:rewrite page="/learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${sessionMap.rootUid}&hideReflection=${sessionMap.hideReflection}&reqUid=" />"+reqIDVar.getTime();;
			}
		
			function removeAtt(mapID){
				removeItemAttachmentUrl =  removeItemAttachmentUrl + "?sessionMapID="+ mapID;
				removeItemAttachment();
			}
			
			function closeAndRefreshParentMonitoringWindow() {
				refreshParentMonitoringWindow();
				window.close();
			} 
		</script>		
		
	</lams:head>
	<body class="stripes">
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
		
			<c:if test="${sessionMap.mode == 'author' || sessionMap.mode == 'learner'}">
				<c:choose>
				  <c:when test="${not sessionMap.allowNewTopics and (sessionMap.minimumReply ne 0 and sessionMap.maximumReply ne 0)}">
					<div class="info">
						<fmt:message key="label.postingLimits.topic.reminder">
							<fmt:param value="${sessionMap.minimumReply}" />
							<fmt:param value="${sessionMap.maximumReply}" />
							<fmt:param value="${numOfPosts}" />
							<fmt:param value="${sessionMap.maximumReply - numOfPosts}" />
						</fmt:message>
					</div>
		                  </c:when> 
		                  <c:when test="${not sessionMap.allowNewTopics and (sessionMap.minimumReply ne 0 or sessionMap.maximumReply eq 0)}">
		                        <div class="info">
		                                <fmt:message key="label.postingLimits.topic.reminder.min">
		                                        <fmt:param value="${sessionMap.minimumReply}" />
		                                        <fmt:param value="${numOfPosts}" />
		                                        <fmt:param value="${sessionMap.maximumReply - numOfPosts}" />
		                                </fmt:message>
		                        </div>
		                  </c:when> 
		                  <c:when test="${not sessionMap.allowNewTopics and (sessionMap.minimumReply eq 0 or sessionMap.maximumReply ne 0)}">
		                        <div class="info">
		                                <fmt:message key="label.postingLimits.topic.reminder.max">
		                                        <fmt:param value="${sessionMap.maximumReply}" />
		                                        <fmt:param value="${numOfPosts}" />
		                                        <fmt:param value="${sessionMap.maximumReply - numOfPosts}" />
		                                </fmt:message>
		                        </div>
		                  </c:when>
		                </c:choose>
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

	</body>
</lams:html>













