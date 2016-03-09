<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionId" value="${sessionMap.toolSessionId}" />
<c:set var="peerreview" value="${sessionMap.peerreview}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
<c:set var="isCommentsEnabled" value="${sessionMap.isCommentsEnabled}" />

<c:choose>
	<c:when test="${peerreview.showRatingsLeftForUser}">
		<c:set var="finishButtonLabel"><fmt:message key="label.continue" /></c:set>
	</c:when>				
	<c:otherwise>
		<c:set var="finishButtonLabel"><fmt:message key="label.finished" /></c:set>
	</c:otherwise>
</c:choose>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	
	<%@ include file="/common/header.jsp"%>
	<link rel="stylesheet" href="${lams}css/jquery.jRating.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
	<link rel="stylesheet" href="<html:rewrite page='/includes/css/learning.css'/>">
	<style media="screen,projection" type="text/css">
		#no-users-info {display: none;}
	</style>

	<script type="text/javascript">
		//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/";

		//vars for rating.js
		var MAX_RATES = ${peerreview.maximumRates},
		MIN_RATES = ${peerreview.minimumRates},
		COMMENTS_MIN_WORDS_LIMIT = ${sessionMap.commentsMinWordsLimit},
		MAX_RATINGS_FOR_ITEM = ${peerreview.maximumRatesPerUser},
		LAMS_URL = '${lams}',
		COUNT_RATED_ITEMS = ${sessionMap.countRatedItems},
		COMMENT_TEXTAREA_TIP_LABEL = '<fmt:message key="label.comment.textarea.tip"/>',
		WARN_COMMENTS_IS_BLANK_LABEL = '<fmt:message key="warning.comment.blank"/>',
		WARN_MIN_NUMBER_WORDS_LABEL = "<fmt:message key="warning.minimum.number.words"><fmt:param value="${sessionMap.commentsMinWordsLimit}"/></fmt:message>";
	</script>
	<script src="${lams}includes/javascript/jquery.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.jRating.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-widgets.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-pager.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/common.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/rating.js" type="text/javascript" ></script> 	
	<script src="${lams}includes/javascript/bootstrap.min.js" type="text/javascript"></script>
	<script type="text/javascript">
	
	var YOUR_RATING_LABEL = '<fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message>',
	IS_DISABLED =  ${sessionMap.isDisabled};
	
	
	$(document).ready(function(){
		
		$(".tablesorter").tablesorter({
			theme: 'bootstrap',
		    widthFixed: true,
		    sortInitialOrder: 'desc',
		    headerTemplate : '{content} {icon}',
		    widgets: ['uitheme', 'zebra'],
	        headers: { 
	            1: { 
	                sorter: false 
	            }, 
	            2: {
	                sorter: false 
	            } 
	        }
		});
		
		$(".tablesorter").each(function() {
			$(this).tablesorterPager({
				savePages: false,
			    container: $(this).find(".ts-pager"),
			    output: '{startRow} to {endRow} ({totalRows})',
				cssPageDisplay: '.pagedisplay',
				cssPageSize: '.pagesize',
				cssDisabled: 'disabled',
				ajaxUrl : "<c:url value='/learning/getUsers.do'/>?page={page}&size={size}&{sortList:column}&sessionMapID=${sessionMapID}&toolContentId=${peerreview.contentId}&toolSessionId=${toolSessionId}&userId=<lams:user property='userID' />",
				ajaxProcessing: function (data) {
			    	if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {},
						countRatedItems = data.countRatedItems;

			    		if (data.rows.length == 0) {
			    			$(".tablesorter,.pager").hide();
			    			$("#no-users-info").show();
			    		}
			    		
						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i];
							var itemId = userData["userId"];
							var isMaximumRatesPerUserReached = (${peerreview.maximumRatesPerUser} != 0) && (userData.ratesPerUser >= ${peerreview.maximumRatesPerUser});
							
							rows += '<tr>';

							rows += '<td class="username"><span>';
							rows += userData["userName"];
							rows += '</span>'

							if (isMaximumRatesPerUserReached) {
								rows += '<br/><div class="alert alert-warning"><i class="fa fa-exclamation-circle text-muted"></i> <fmt:message key="label.cant.rate" /></div>';
							}

							rows += '</td>';
							
							rows += '<td class="rating">';
							rows += 	'<div class="rating-stars-holder">';

							// if the user has left a comment or done a rating in a batch of ratings, we need to keep all related ratings open.
							var hasStartedRating = false;
							for (j = 0; !hasStartedRating && j < userData.criteriaDtos.length; j++){
								hasStartedRating = userData.criteriaDtos[j].userRating != "";
								if ( hasStartedRating) {
									idsBeingRated.push(itemId); // idsBeingRated defined in rating.js
								}
							}
							hasStartedRating = hasStartedRating || ${isCommentsEnabled} && userData["commentPostedByUser"] != "";
							
							var isDisabled = IS_DISABLED || (MAX_RATES > 0) && (countRatedItems >= MAX_RATES) && ! hasStartedRating || isMaximumRatesPerUserReached;
							
							for (j = 0; j < userData.criteriaDtos.length; j++){
								var criteriaDto = userData.criteriaDtos[j];
								var objectId = criteriaDto["ratingCriteriaId"] + "-" + itemId;
								var averageRating = criteriaDto.averageRating;
								var numberOfVotes = criteriaDto.numberOfVotes;
								var userRating = criteriaDto.userRating;
								var isCriteriaNotRatedByUser = userRating == "";
								var averageRatingDisplayed = (!isCriteriaNotRatedByUser) ? averageRating : 0;
								var ratingStarsClass = (isDisabled || !isCriteriaNotRatedByUser) ? "rating-stars-disabled" : "rating-stars";
							
								rows += '<h4>';
								rows += 	 criteriaDto.title;
								rows += '</h4>';
										
								rows += '<div class="'+ ratingStarsClass +' rating-stars-new" data-average="'+ averageRatingDisplayed +'" data-id="'+ objectId +'">';
								rows += '</div>';
									
								rows += '<div class="rating-stars-caption" id="rating-stars-caption-'+ objectId +'"';
								if (isCriteriaNotRatedByUser) {
									rows += ' style="visibility: hidden;"';	
								}
								rows += '>';
								var temp = YOUR_RATING_LABEL.replace("@1@", '<span id="user-rating-'+ objectId +'">'+ userRating + '</span>');
								temp = temp.replace("@2@", '<span id="average-rating-'+ objectId +'">'+ averageRating + '</span>');
								temp = temp.replace("@3@", '<span id="number-of-votes-'+ objectId +'">'+ numberOfVotes + '</span>');
								rows += 	temp;
								rows += '</div>';
							}
									
							rows += 	'</div>';
							rows += '</td>';
							
							if (${isCommentsEnabled}) {
								rows += '<td class="comment" id="comments-area-' + itemId + '">';
								
								var commentsCriteriaId = userData["commentsCriteriaId"];
								var commentPostedByUser = userData["commentPostedByUser"];
									
								//show all comments needs to be shown
								if (commentPostedByUser != "") {
									rows += '<div class="rating-comment">';
									rows += 	commentPostedByUser;
									rows += '</div>';
										
								//show comments textarea and a submit button
								} else if (!isDisabled) {
									rows += '<div id="add-comment-area-' + itemId + '">';											
									rows +=		'<textarea name="comment" rows="4" id="comment-textarea-'+ itemId +'" onfocus="if(this.value==this.defaultValue)this.value=\'\';" onblur="if(this.value==\'\')this.value=this.defaultValue;"><fmt:message key="label.comment.textarea.tip"/></textarea>';
									
									rows += 	'<div class="button add-comment add-comment-new" data-item-id="'+ itemId +'" data-comment-criteria-id="'+ commentsCriteriaId +'">';
									rows += 	'</div>';
									rows += '</div>';											
								}
								
								rows += '</td>';
							}
							
							rows += '</tr>';
						}
			            
						json.total = data.total_rows;
						json.rows = $(rows);
						return json;
			    	}
				}
			})
			
			// bind to pager events
			.bind('pagerInitialized pagerComplete', function(event, options){
				initializeJRating();
			});
		});
	 });
	
		function finishSession(){
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionId=${toolSessionId}"/>';
			return false;
		}

		function showResults(){
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/showResults.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
		
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
		function refresh(page){
			document.location.href='<c:url value="/learning/start.do?mode=learner&toolSessionID=${toolSessionId}"/>';
		}

		function onRatingErrorCallback() {
			alert('<fmt:message key="error.max.ratings.per.user"/>');
			refresh();
		}

    </script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${peerreview.title}">

		<c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
			<lams:Alert type="danger" id="warn-lock" close="false">
				<c:choose>
					<c:when test="${sessionMap.userFinished}">
						<fmt:message key="message.activityLocked"/>
					</c:when>
					<c:otherwise>
						<fmt:message key="message.warnLockOnFinish" ><fmt:param>${finishButtonLabel}</fmt:param></fmt:message>
					</c:otherwise>
				</c:choose>
			</lams:Alert>
		</c:if>
		
		<!-- Rating limits info -->
		<c:if test="${peerreview.minimumRates ne 0 || peerreview.maximumRates ne 0}">
		
			<lams:Alert type="info" id="rate-limits-reminder" close="false">
				<c:choose>
					<c:when test="${peerreview.minimumRates ne 0 and peerreview.maximumRates ne 0}">
						<fmt:message key="label.rate.limits.reminder">
							<fmt:param value="${peerreview.minimumRates}"/>
							<fmt:param value="${peerreview.maximumRates}"/>
						</fmt:message>
					</c:when>
					
					<c:when test="${peerreview.minimumRates ne 0 and peerreview.maximumRates eq 0}">
						<fmt:message key="label.rate.limits.reminder.min">
							<fmt:param value="${peerreview.minimumRates}"/>
						</fmt:message>
					</c:when>
					
					<c:when test="${peerreview.minimumRates eq 0 and peerreview.maximumRates ne 0}">
						<fmt:message key="label.rate.limits.reminder.max">
							<fmt:param value="${peerreview.maximumRates}"/>
						</fmt:message>
					</c:when>
				</c:choose>

				<BR/>
				<fmt:message key="label.rate.limits.topic.reminder">
					<fmt:param value="<span id='count-rated-items'>${sessionMap.countRatedItems}</span>"/>
				</fmt:message>
			</lams:Alert>
				
			
		</c:if>
				
		<c:if test="${isCommentsEnabled && sessionMap.commentsMinWordsLimit != 0}">
			<lams:Alert type="info" id="rate-limits-reminder" close="false">
				<fmt:message key="label.comment.minimum.number.words">
					<fmt:param>: ${sessionMap.commentsMinWordsLimit}</fmt:param>
				</fmt:message>
			</lams:Alert>
		</c:if>
				
		<p>
			<c:out value="${peerreview.instructions}" escapeXml="false"/>
		</p>

		<c:set var="numColumns" value="2"/>
		<c:if test="${isCommentsEnabled}">
			<c:set var="numColumns" value="3"/>
		</c:if>
	
		<lams:TSTable numColumns="${numColumns}">
			<th class="username" title="<fmt:message key='label.sort.by.user.name'/>" > 
				<fmt:message key="label.user.name" />
			</th>
			<th class="rating">  
				<fmt:message key="label.rating" />
			</th>
			<c:if test="${isCommentsEnabled}">
				<th class="comment"> 
					<fmt:message key="label.comment" />
				</th>
			</c:if>
		</lams:TSTable>
							
		<div id="no-users-info">
			<fmt:message key="label.no.users" />
		</div>
	
		<!-- Reflection -->
		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<%@ include file="notebookdisplay.jsp"%>
		</c:if>
		<!-- End Reflection -->

		<c:if test="${mode != 'teacher'}">
				<html:button property="FinishButton" styleId="finishButton" onclick="refresh()" styleClass="btn btn-default voffset5 pull-left">
					<fmt:message key="label.refresh" />
				</html:button>
				<c:choose>
					<c:when test="${peerreview.showRatingsLeftForUser}">
						<html:button property="FinishButton" styleId="finishButton" onclick="return showResults()" styleClass="btn btn-primary voffset5 pull-right">
							<fmt:message key="label.continue" />
						</html:button>
					</c:when>				
					<c:when test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<html:button property="FinishButton" onclick="return continueReflect()" styleClass="btn btn-default voffset5 pull-right">
							<fmt:message key="label.continue" />
						</html:button>
					</c:when>
					<c:otherwise>
						<html:link href="#nogo" property="FinishButton" styleId="finishButton" onclick="return finishSession()" styleClass="btn btn-primary voffset5 pull-right na">
							<fmt:message key="label.finished" />
						</html:link>
					</c:otherwise>
				</c:choose>
		</c:if>
		
	</lams:Page>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
