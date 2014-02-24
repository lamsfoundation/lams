<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<%@ taglib uri="tags-tiles" prefix="tiles"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="backToForum"><html:rewrite page="/learning/viewForum.do?mode=${sessionMap.mode}&sessionMapID=${sessionMapID}&toolSessionID=${sessionMap.toolSessionID}&hideReflection=${sessionMap.hideReflection}" /></c:set>
<c:set var="refresh"><html:rewrite page="/learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${sessionMap.rootUid}&hideReflection=${sessionMap.hideReflection}" /></c:set>

<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>
		
		<link type="text/css" href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
		<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
		<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />

		<!-- ********************  javascript from header.jsp ********************** -->
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
		<script type="text/javascript">
			var removeItemAttachmentUrl = "<html:rewrite page="/learning/deleteAttachment.do" />";
			//var for jquery.jRating.js
			var pathToImageFolder = "${lams}images/css/"; 
			window.close();
		</script>		
		<script type="text/javascript" src="${tool}includes/javascript/message.js"></script>
		<script src="${lams}includes/javascript/jquery.js" type="text/javascript"></script>
		<script src="${lams}includes/javascript/jquery.mobile.js" type="text/javascript"></script>	
		<script src="${lams}includes/javascript/jquery.jRating.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(document).bind('pageinit', function(){
				$(".rating-stars").jRating({
				    phpPath : "<c:url value='/learning/rateMessage.do'/>?toolSessionID=${sessionMap.toolSessionID}&sessionMapID=${sessionMapID}",
				    rateMax : 5,
				    decimalLength : 1,
					onSuccess : function(data, messageId){
					    $("#averageRating" + messageId).html(data.averageRating);
					    $("#numberOfVotes" + messageId).html(data.numberOfVotes);
					    $("#numOfRatings").html(data.numOfRatings);
					    
					    //disable rating feature in case maxRate limit reached
					    if (data.noMoreRatings) {
					    	$(".rating-stars").each(function() {
					    		$(this).jRating('readOnly');
					    	});
					    }
					},
					onError : function(){
					    jError('Error : please retry');
					}
				});
			    $(".rating-stars-disabled").jRating({
			    	rateMax : 5,
			    	isDisabled : true
				});
			});
			
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
	<body class="large-font">
<div data-role="page" data-dom-cache="false">

	<div data-role="header" data-theme="b">
		<a id="backToForum"	href="${backToForum}" data-role="button" data-icon="arrow-l">
			Back
		</a>
		<h1>
			${sessionMap.title}
		</h1>
	</div>

	<div data-role="content">
		<div>
			<div class="right-buttons">

			</div>
			<h3>
				<fmt:message key="title.message.view.topic" />
			</h3>
	
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
		
		<%@ include file="message/topicview.jsp"%>
	
		<div style="padding-top: 7px; padding-left: 5px;" data-role="controlgroup" >
			<a href="${refresh}" id="refresh" data-theme="c" data-icon="refresh" data-role="button"  onclick="this.href += '&reqID=' + (new Date()).getTime();">
				<fmt:message key="label.refresh" />
			</a>
		</div>
	</div>
	
	<div data-role="footer" data-theme="b" class="ui-bar">
		<a id="backToForumFooter"	href="${backToForum}" data-role="button" data-icon="arrow-l">
			Back
		</a>
	</div><!-- /footer -->
</div>

	</body>
</lams:html>






