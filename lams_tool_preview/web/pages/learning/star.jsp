<%@ include file="/common/taglibs.jsp"%>
<c:set var="maxRates" value="${rateAllUsers > 0 ? rateAllUsers : criteriaRatings.ratingCriteria.maximumRates}"/>
<c:set var="minRates" value="${rateAllUsers > 0 ? rateAllUsers : criteriaRatings.ratingCriteria.minimumRates}"/>

<!-- ********************  CSS ********************** -->
	<link href="${lams}css/rating.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager5.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap5.css">
	<link rel="stylesheet" href="${lams}css/starability-basic.css">

<!-- ********************  javascript ********************** -->	
	<script type="text/javascript">
		//vars for rating.js
		var MAX_RATES = ${maxRates},
		MIN_RATES = ${minRates},
		COMMENTS_MIN_WORDS_LIMIT = ${criteriaRatings.ratingCriteria.commentsMinWordsLimit},
		MAX_RATINGS_FOR_ITEM = ${peerreview.maximumRatesPerUser},
		LIMIT_BY_CRITERIA = "true";
		COUNT_RATED_ITEMS = ${criteriaRatings.countRatedItems},
		COMMENT_TEXTAREA_TIP_LABEL = '<spring:escapeBody javaScriptEscape="true">:message key="label.comment.textarea.tip"/></spring:escapeBody>',
		WARN_COMMENTS_IS_BLANK_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="warning.comment.blank"/></spring:escapeBody>',
		WARN_MIN_NUMBER_WORDS_LABEL = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='warning.minimum.number.words'><fmt:param value='${criteriaRatings.ratingCriteria.commentsMinWordsLimit}'/></fmt:message></spring:escapeBody>",
		SESSION_ID = ${toolSessionId};
	</script>
	<script src="${lams}includes/javascript/jquery.tablesorter.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-widgets.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-pager.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/rating.js" type="text/javascript" ></script> 	
	<script src="${lams}includes/javascript/portrait5.js" type="text/javascript" ></script>
<script type="text/javascript">	
	var YOUR_RATING_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.you.gave.rating"><fmt:param>@1@</fmt:param></fmt:message></spring:escapeBody>',
		IS_DISABLED =  ${sessionMap.isDisabled},
		commentsSaved = true;
	
	$(document).ready(function(){	
		if ( ${minRates} > 0 && ${criteriaRatings.countRatedItems} < ${minRates}) {
			hideButtons();
		}
		
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

				ajaxUrl : "<c:url value='/learning/getUsers.do'/>?page={page}&size={size}&{sortList:column}&sessionMapID=${sessionMapID}&toolContentId=${peerreview.contentId}&toolSessionId=${toolSessionId}&criteriaId=${criteriaRatings.ratingCriteria.ratingCriteriaId}&userId=<lams:user property='userID' />",
				<c:if test="${criteriaRatings.ratingCriteria.commentsEnabled}">
				customAjaxUrl: function(table, url) {
					if ( commentsSaved ) {
						return url;
					} else { 
						<!-- Save comments first - this will retrigger the page call. Have to wait for submitEntrys ajax call to complete or end up not showing the comments when the pagesize is changed -->
						submitEntry();
						return "";
					}
				}, 
				</c:if>
				ajaxProcessing: function (data) {
			    	if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};

			    		if (data.rows.length == 0) {
			    			$(".tablesorter,.pager").hide();
			    			$("#no-users-info").show();
			    		}

						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i];
							var itemId = userData["itemId"];
							
							var isMaximumRatesPerUserReached = (${peerreview.maximumRatesPerUser} != 0) && (userData.ratesPerUser >= ${peerreview.maximumRatesPerUser});
							
							rows += 
								'<tr>' +
									'<td class="username">' + 
										'<div class="float-start me-2">' +
											definePortrait( userData["itemDescription2"], itemId, 'small', true, '${lams}' ) +
									 	'</div>' +
										'<span class="portrait-sm-lineheight" id="username-' + itemId + '">' +
											userData["itemDescription"] +
										'</span>';
							if (isMaximumRatesPerUserReached) {
								rows +=	'<br/><div class="alert alert-warning"><i class="fa fa-exclamation-circle text-muted"></i> <spring:escapeBody javaScriptEscape="true"><fmt:message key="label.cant.rate" /></spring:escapeBody></div>';
							}
							rows += '</td>' +
									'<td class="rating">' +
										'<div class="starability-holder">';	

							let isDisplayOnly = false,
								objectId = "${criteriaRatings.ratingCriteria.ratingCriteriaId}-" + itemId,
								averageRating = userData["userRating"],//use userRating instead of averageRating for display only
								numberOfVotes = null,
								userRating = userData["userRating"],
								isCriteriaRatedByUser = userRating != "",
								isWidgetDisabled = (IS_DISABLED || isDisplayOnly || (MAX_RATES > 0) && (COUNT_RATED_ITEMS >= MAX_RATES) || isMaximumRatesPerUserReached) && !isCriteriaRatedByUser,
								criteriaTitle = null;
							rows +=		 	createStarability(isDisplayOnly, objectId, averageRating, numberOfVotes, userRating, isWidgetDisabled, criteriaTitle) +
										'</div>' +	
									'</td>';
							
							if (${criteriaRatings.ratingCriteria.commentsEnabled}) {
								rows += '<td class="comment" id="comments-area-' + itemId + '">';
								
								var commentPostedByUser = userData["comment"];
									
								//show all comments needs to be shown
								if (commentPostedByUser != "" && IS_DISABLED) {
									rows += '<div class="rating-comment">';
									rows += 	commentPostedByUser;
									rows += '</div>';
										
								//show comments textarea and a submit button
								} else if (!IS_DISABLED) {
									rows += '<div id="add-comment-area-' + itemId;
									if ( !isCriteriaRatedByUser )
										rows += '" style="visibility: hidden;';
									rows += '">';	
									commentPostedByUser = commentPostedByUser.replace(/<BR>/gi, '\n');
									rows +=		'<textarea name="comment-textarea-'+itemId+'" rows="4" id="comment-textarea-'+ itemId +'" onblur="onRatingSuccessCallback()" class="form-control" aria-labelledby="theader-comment username-' + itemId + '">'+commentPostedByUser+'</textarea>';
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
				commentsSaved = false;
				initializeStarability();
				onRatingSuccessCallback(); // show buttons if appropriate
			});
		});
	 });

<c:choose>
<c:when test="${criteriaRatings.ratingCriteria.commentsEnabled}">
	function submitEntry(next, url){
		hideButtons();

		var validationFailed = false, 
			commentsToSave = 0,
			data = {
				sessionMapID: '${sessionMapID}', 
				toolContentId: '${peerreview.contentId}',
				criteriaId: '${criteriaRatings.ratingCriteria.ratingCriteriaId}'
			};
		
		// save the modified values
		$('textarea').each(function() {
			if ( ! ( $('#'+this.id).parent().css('visibility') == 'hidden') ) {
				var comment = validComment(this.id, true, false);
				if ( ! ( typeof comment === "undefined" )  ) {
					if (comment!=this.defaultValue) {
						data[this.id] = comment;
						commentsToSave++;
					}
				} else  {
					validationFailed = true;
					return false; // validation failed! abort!
				}
			}
		});
		if ( validationFailed )
			return false;
		
		if ( commentsToSave > 0 ) {
			$.ajax({ 
				data: data, 
		        type: 'POST', 
	 	        url: '<c:url value="/learning/submitCommentsAjax.do?"/>', 
		        success: function (response) {
	    			var countCommentsSaved = response.countCommentsSaved;
					if ( ! ( countCommentsSaved >= 0 ) ) {
	       				alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.unable.save.comments"/></spring:escapeBody>');
	       				return false;
					} else {
						return moveOn(next);
					}
				}
			});
			
		} else {
			return moveOn(next);
		}
		
		return false;
	}
	
	function moveOn(next) {
		commentsSaved = true;
		if ( ! ( typeof next === "undefined" ) ) {
			return nextprev(next);
		} else {
			$(".tablesorter").trigger('pagerUpdate');
			return true;
		}
	}
	
</c:when>
<c:otherwise>
	function submitEntry(next) {
		// stars saved when clicked so don't use next button to submit
		nextprev(next);
	}
</c:otherwise>
</c:choose>

	function onRatingSuccessCallback() {
		var numItems = $("#count-rated-items").html();
		if ( ${minRates} <= 0 || numItems >=  ${minRates} ) {
			showButtons();
		}
	}
</script>

<!-- Rating limits info -->
<c:if test="${rateAllUsers > 0 || criteriaRatings.ratingCriteria.minimumRates ne 0 || criteriaRatings.ratingCriteria.maximumRates ne 0}">
			<lams:Alert5 type="info" id="rate-limits-reminder" close="false">
				<c:choose>
					<c:when test="${rateAllUsers > 0}">
						<fmt:message key="label.rate.all.users"></fmt:message>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${criteriaRatings.ratingCriteria.minimumRates ne 0 and criteriaRatings.ratingCriteria.maximumRates ne 0}">
								<fmt:message key="label.rate.limits.reminder">
									<fmt:param value="${criteriaRatings.ratingCriteria.minimumRates}"/>
									<fmt:param value="${criteriaRatings.ratingCriteria.maximumRates}"/>
								</fmt:message>
							</c:when>
							<c:when test="${criteriaRatings.ratingCriteria.minimumRates ne 0 and criteriaRatings.ratingCriteria.maximumRates eq 0}">
								<fmt:message key="label.rate.limits.reminder.min">
									<fmt:param value="${criteriaRatings.ratingCriteria.minimumRates}"/>
								</fmt:message>
							</c:when>
							<c:when test="${criteriaRatings.ratingCriteria.minimumRates eq 0 and criteriaRatings.ratingCriteria.maximumRates ne 0}">
								<fmt:message key="label.rate.limits.reminder.max">
									<fmt:param value="${criteriaRatings.ratingCriteria.maximumRates}"/>
								</fmt:message>
							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>

				<BR/>
				<fmt:message key="label.rate.limits.topic.reminder">
					<fmt:param value="<span id='count-rated-items'>${criteriaRatings.countRatedItems}</span>"/>
				</fmt:message>
			</lams:Alert5>
</c:if>

<div class="card lcard">
	<div class="card-header">
		<c:out value="${criteriaRatings.ratingCriteria.title}" escapeXml="true" />
	</div>
	
	<c:set var="numColumns" value="2"/>
	<c:if test="${criteriaRatings.ratingCriteria.commentsEnabled}">
		<c:set var="numColumns" value="3"/>
	</c:if>
	
	<lams:TSTable5 numColumns="${numColumns}" tableClass="tablesorter">
		<th class="username" title="<fmt:message key='label.sort.by.user.name'/>"> 
			<fmt:message key="label.user.name" />
		</th>
		<th class="rating" id="theader-rating">  
			<fmt:message key="label.rating" />
		</th>
		<c:if test="${criteriaRatings.ratingCriteria.commentsEnabled}">
			<th class="comment" id="theader-comment"> 
				<fmt:message key="label.comment" />
			</th>
		</c:if>
	</lams:TSTable5>

	<div id="no-users-info">
		<fmt:message key="label.no.users" />
	</div>
</div>