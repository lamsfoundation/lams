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
		
		<link rel="stylesheet" href="<html:rewrite page='/includes/css/jRating.jquery.css'/>"  type="text/css" />
		<link rel="stylesheet" href="<html:rewrite page='/includes/css/ratingStars.css'/>"  type="text/css" />
		<style type="text/css">
/*layout of the table with the alternate color for the table heading cell */
table.alternative-color {
 	 width:100%; 
	 margin-left:0px; 
	 padding-top:0px; 
	 margin-bottom:15px; 
	 text-align:left;
}

table.alternative-color th {

	font-size: 12px;
	height: 30px;
}

table.alternative-color td {
	padding:5px;
	padding-left: 2px;
	font-size: 11px;
	border-bottom: 1px solid #efefef;
	vertical-align: middle; 
	background:url('../images/css/greyfade_bg.jpg') repeat-x 3px 0px
}

table.alternative-color td.first, table.alternative-color th.first {
	border-left: none;
	padding-left: 2px;
}
table.alternative-color .ui-btn-up-c {
font-weight: normal; cursor: default;
}
b.error {color:#cc0000;} 

/*layout of the forum tables with the colored table heading cell */
table.forum { 
	width:93%;
	margin-bottom:10px;
	background:url('../images/css/greyfade_bg.jpg') repeat-x 3px 49px; 
	text-align:left; 
	border-bottom:1px solid #efefef;}

table.forum th { 
	height:20px; 
	padding-top:5px; 
	padding-left:15px; 
	border-left:1px solid #cacdd1;  
	font-size:12px; 
	vertical-align:middle;
}

table.forum td.posted-by {
	background: url('../images/css/forum_postedby_bg.jpg') repeat-x; 
	height:20px; 
	padding-left:15px; 
	padding-top:0px; 
	font-size:10px;
	vertical-align:top; 
	border-left:1px solid #cacdd1; 
	color:#666;
}

table.forum td {
	padding-left:20px; 
	font-size:11px; 
	vertical-align:top; 
}
		</style>

		<!-- ********************  javascript from header.jsp ********************** -->
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
		<script type="text/javascript">
			function closeAndRefreshParentMonitoringWindow() {
				refreshParentMonitoringWindow();
				window.close();
			}  				
		</script>
		<!-- End of javascript from header.jsp -->
		
		<script type="text/javascript">
			var removeItemAttachmentUrl = "<html:rewrite page="/learning/deleteAttachment.do" />";
			var pathToImageFolder = "<html:rewrite page='/images/'/>"; 
		</script>		
		<script type="text/javascript" src="${tool}includes/javascript/message.js"></script>	
		<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
		<script src="${lams}includes/javascript/jquery-1.7.1.min.js"></script>
		<script src="${lams}includes/javascript/jquery.mobile.js"></script>	
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jRating.jquery.js'/>"></script>
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
		
			function removeAtt(mapID){
				removeItemAttachmentUrl =  removeItemAttachmentUrl + "?sessionMapID="+ mapID;
				removeItemAttachment();
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
	<body>
<div data-role="page" data-cache="false">

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
		</c:if>
		<br>
		
		<%@ include file="message/topicview.jsp"%>
	
		<div style="padding-top: 7px; padding-left: 5px;" data-role="controlgroup" >
			<a href="${refresh}" id="refresh" data-theme="c" data-icon="refresh" data-role="button"  onclick="this.href += '&reqID=' + (new Date()).getTime();">
				<fmt:message key="label.refresh" />
			</a>
		</div>
	</div>
	
	<div data-role="footer" data-theme="b" style="padding: 0px;">
		<a id="backToForumFooter"	href="${backToForum}" data-role="button" data-icon="arrow-l">
			Back
		</a>
	</div><!-- /footer -->
</div>

	</body>
</lams:html>






