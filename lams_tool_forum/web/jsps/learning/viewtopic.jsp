<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ page import="org.lamsfoundation.lams.tool.forum.util.ForumConstants"%>

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="pageSize" value="<%= ForumConstants.DEFAULT_PAGE_SIZE %>"/>

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
		<link type="text/css" href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
		<link type="text/css" href="${tool}css/jquery.treetable.css" rel="stylesheet"/>
		<link type="text/css" href="${tool}css/jquery.treetable.forum.css" rel="stylesheet"/>
		
		<!-- ********************  javascript ********************** -->
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
		
		<script type="text/javascript">
			var removeItemAttachmentUrl = "<html:rewrite page="/learning/deleteAttachment.do" />";
			//var for jquery.jRating.js
			var pathToImageFolder = "${lams}images/css/";
		</script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
		<script type="text/javascript" src="${tool}includes/javascript/jquery.jscroll.js"></script>
		<script type="text/javascript" src="${tool}includes/javascript/jquery.treetable.js"></script>
		<script type="text/javascript" src="${tool}includes/javascript/message.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				setupJRatingSetPath();
			});
				
			function setupJRatingSetPath() {
				setupJRating("<c:url value='/learning/rateMessage.do'/>?toolSessionID=${sessionMap.toolSessionID}&sessionMapID=${sessionMapID}");
			}
			
			function refreshTopic(){
				var reqIDVar = new Date();
				location.href= "<html:rewrite page="/learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${sessionMap.rootUid}&hideReflection=${sessionMap.hideReflection}&pageLastId=0&size=${pageSize}&reqUid=" />"+reqIDVar.getTime();;
			}
		</script>		
		
	</lams:head>
	<body class="stripes">
		<div id="content">

			<h1>
				<c:out value="${sessionMap.title}" escapeXml="true"/>
			</h1>
		
			<div>
				<div class="right-buttons">
				
					<c:set var="refreshTopicURL">
					</c:set>
					<html:button property="refresh"
						onclick="javascript:refreshTopic();"
						styleClass="button">
						<fmt:message key="label.refresh" />
					</html:button>
					
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
		    	
				<!-- Rating announcements -->
				<c:if test="${sessionMap.allowRateMessages}">
					<div class="info">
					
						<c:choose>
							<c:when test="${sessionMap.minimumRate ne 0 and sessionMap.maximumRate ne 0}">
								<fmt:message key="label.rateLimits.forum.reminder">
									<fmt:param value="${sessionMap.minimumRate}"/>
									<fmt:param value="${sessionMap.maximumRate}"/>
								</fmt:message>						
							</c:when>
							
							<c:when test="${sessionMap.minimumRate ne 0 and sessionMap.maximumRate eq 0}">
								<fmt:message key="label.rateLimits.forum.reminder.min">
									<fmt:param value="${sessionMap.minimumRate}"/>
								</fmt:message>					
							</c:when>
							
							<c:when test="${sessionMap.minimumRate eq 0 and sessionMap.maximumRate ne 0}">
								<fmt:message key="label.rateLimits.forum.reminder.max">
									<fmt:param value="${sessionMap.maximumRate}"/>
								</fmt:message>					
							</c:when>
						</c:choose>
						<br>
						
						<fmt:message key="label.rateLimits.topic.reminder">
							<fmt:param value="<span id='numOfRatings'>${sessionMap.numOfRatings}</span>"/>
						</fmt:message>
						
					</div>
				</c:if>
				    	
			</c:if>
			<br>
			
			<div class="space-bottom">
			<div class="scroll" >
			<%@ include file="message/topicview.jsp"%>
			</div>
			</div>
			<script>
				<c:set var="loading_animation">${lams}images/ajax-loader.gif</c:set>
				<c:set var="loading_words"><fmt:message key="label.loading.messages" /></c:set>
				$('.scroll' ).jscroll({loadingHtml: '<img src="${loading_animation}" alt="${loading_words}" />${loading_words}',padding:30,autoTrigger:true,callback:setupJRatingSetPath});
			</script>
			
			<c:set var="refreshTopicURL">
			</c:set>
			
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
		
				<a href="javascript:refreshTopic();" class="button"> <fmt:message
						key="label.refresh" /> </a>
			</div>
		</div>
					
		</div>

	</body>
</lams:html>
