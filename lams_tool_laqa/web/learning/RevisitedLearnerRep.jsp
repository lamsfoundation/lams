<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.httpSessionID]}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="qaContent" value="${sessionMap.content}" />
<c:set var="isLeadershipEnabled" value="${qaContent.useSelectLeaderToolOuput}" />
<c:set var="isCommentsEnabled" value="${sessionMap.isCommentsEnabled}" />
<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" />
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<lams:css suffix="jquery.jRating"/>
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme-blue.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
	<link rel="stylesheet" href="<html:rewrite page='/includes/css/qalearning.css'/>">
	<lams:css />

	<script src="${lams}includes/javascript/jquery.timeago.js" type="text/javascript"></script>
	<script type="text/javascript" src="${lams}includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
	<script src="${lams}includes/javascript/jquery.js" type="text/javascript"></script>
	<script type="text/javascript">
		//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/";
		
		//vars for rating.js
		var MAX_RATES = ${qaContent.maximumRates},
		MIN_RATES = ${qaContent.minimumRates},
		COMMENTS_MIN_WORDS_LIMIT = ${sessionMap.commentsMinWordsLimit},
		LAMS_URL = '${lams}',
		COUNT_RATED_ITEMS = ${sessionMap.countRatedItems},
		COMMENT_TEXTAREA_TIP_LABEL = '<fmt:message key="label.comment.textarea.tip"/>',
		WARN_COMMENTS_IS_BLANK_LABEL = '<fmt:message key="warning.comment.blank"/>',
		WARN_MIN_NUMBER_WORDS_LABEL = "<fmt:message key="warning.minimum.number.words"><fmt:param value="${sessionMap.commentsMinWordsLimit}"/></fmt:message>",
		ALLOW_RERATE = false,
		SESSION_ID = ${toolSessionID};
	</script>
	<script src="${lams}includes/javascript/jquery.jRating.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-pager.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/common.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/rating.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-widgets.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.timeago.js" type="text/javascript"></script>
        <script type="text/javascript" src="${lams}includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>

	<script type="text/javascript">
	
		var AVG_RATING_LABEL = '<fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message>',
		YOUR_RATING_LABEL = '<fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message>',
		IS_DISABLED =  ${sessionMap.isDisabled};
	
	  	$(document).ready(function(){
	  		
	  		jQuery("time.timeago").timeago();
	  		
			$(".tablesorter").tablesorter({
				theme: 'bootstrap',
				headerTemplate : '{content} {icon}',
			    widthFixed: true,
			    widgets: ['uitheme','zebra'],
		        headers: { 
		            2: {
		                sorter: false 
		            } 
		        } 
			});
			
			$(".tablesorter").each(function() {
				$(this).tablesorterPager({	
					savePages: false,
					ajaxUrl : "<c:url value='/learning.do'/>?method=getResponses&page={page}&size={size}&{sortList:column}&isAllowRateAnswers=${qaContent.allowRateAnswers}&isAllowRichEditor=${qaContent.allowRichEditor}&qaContentId=${qaContent.qaContentId}&qaSessionId=" + $("#toolSessionID").val() + "&questionUid=" + $(this).attr('data-question-uid') + "&userId=" + $("#userID").val() + "&reqID=" + (new Date()).getTime(),
					ajaxProcessing: function (data) {
				    	if (data && data.hasOwnProperty('rows')) {
				    		var rows = [],
				            json = {},
							countRatedItems = data.countRatedItems;
				    		
							for (i = 0; i < data.rows.length; i++){
								var userData = data.rows[i];
								var itemId = userData["responseUid"];
								var isItemAuthoredByUser = userData["isItemAuthoredByUser"];
								
								rows += '<tr>';
								rows += '<td>';
								
								if (${generalLearnerFlowDTO.userNameVisible == 'true'}) {
									rows += '<div class="sbox-heading bg-warning">';
									rows += 	'<div class="user">';
									rows += 		userData["userName"];
									rows += 	'</div> - ';
									rows += '<time class="timeago" title="';
									rows += userData["attemptTime"];
									rows += '" datetime="';
									rows += 	userData["timeAgo"];
									rows += '"></time></div>';
								}
								
								rows += 	'<div class="user-answer">';
								if (userData["visible"] == 'true') {
									rows += 	userData["answer"];
								} else {
									rows += 	'<i><fmt:message key="label.hidden"/></i>';
								}
								rows += 	'</div>';
								
								rows += '</td>';
								
								var usesRatings = false;
								var hasStartedRating = false;

	
								if (${generalLearnerFlowDTO.allowRateAnswers}) {
									rows += '<td style="width:150px;">';
									
									if (userData["visible"] == 'true') {
										rows += '<div class="rating-stars-holder">';
										
										// if the user has left a comment or done a rating in a batch of ratings, we need to keep all related ratings open.
										usesRatings = true;
										for (j = 0; !hasStartedRating && j < userData.criteriaDtos.length; j++){
											hasStartedRating = userData.criteriaDtos[j].userRating != "";
											if ( hasStartedRating) {
												idsBeingRated.push(itemId); // idsBeingRated defined in rating.js
											}
										}
										hasStartedRating = hasStartedRating || ${isCommentsEnabled} && userData["commentPostedByUser"] != "";
										
										for (j = 0; j < userData.criteriaDtos.length; j++){
											var criteriaDto = userData.criteriaDtos[j];
											var objectId = criteriaDto["ratingCriteriaId"] + "-" + itemId;
											var averageRating = criteriaDto.averageRating;
											var numberOfVotes = criteriaDto.numberOfVotes;
											var userRating = criteriaDto.userRating;
											var isCriteriaNotRatedByUser = userRating == "";
											var averageRatingDisplayed = (isItemAuthoredByUser || !isCriteriaNotRatedByUser) ? averageRating : 0;
											var ratingStarsClass = (IS_DISABLED ||isItemAuthoredByUser || (MAX_RATES > 0) && (countRatedItems >= MAX_RATES) && ! hasStartedRating || !isCriteriaNotRatedByUser) ? "rating-stars-disabled" : "rating-stars";
								
											rows += '<strong>';
											rows += 	 criteriaDto.title;
											rows += '</strong>';
											
											rows += '<div class="'+ ratingStarsClass +' rating-stars-new" data-average="'+ averageRatingDisplayed +'" data-id="'+ objectId +'">';
											rows += '</div>';
											
											if (isItemAuthoredByUser) {
												rows += '<div class="rating-stars-caption">';
												rows += 	AVG_RATING_LABEL.replace("@1@", averageRating).replace("@2@", numberOfVotes);
												rows += '</div>';
												
											} else {
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
										}
										
										rows += '</div>';
									}
									
									rows += '</td>';
								}
								
								if (${isCommentsEnabled}) {
									rows += '<td style="width:30%; min-width: 250px;" id="comments-area-' + itemId + '">';
									
									if (userData["visible"] == 'true') {
										var commentsCriteriaId = userData["commentsCriteriaId"];
										var commentPostedByUser = userData["commentPostedByUser"];
										
										//show all comments needs to be shown
										if (isItemAuthoredByUser) {
											for (j = 0; i < userData.comments.length; j++){
												var comment = userData.comments[j];
												rows += '<div class="rating-comment">';
												rows += 	comment.comment;
												rows += '</div>';
											}
											
										} else if (commentPostedByUser != "") {
											rows += '<div class="rating-comment">';
											rows += 	commentPostedByUser;
											rows += '</div>';
											
										//show comments textarea and a submit button
										} else if (! (IS_DISABLED || usesRatings && MAX_RATES>0 && countRatedItems >= MAX_RATES && !hasStartedRating)) {
											rows += '<div id="add-comment-area-' + itemId + '">';											
											rows +=		'<textarea name="comment" rows="4" id="comment-textarea-'+ itemId +'" onfocus="if(this.value==this.defaultValue)this.value=\'\';" onblur="if(this.value==\'\')this.value=this.defaultValue;"><fmt:message key="label.comment.textarea.tip"/></textarea>';
											
											rows += 	'<div class="button add-comment add-comment-new" data-item-id="'+ itemId +'" data-comment-criteria-id="'+ commentsCriteriaId +'">';
											rows += 	'</div>';
											rows += '</div>';											
										}
									}
									
									rows += '</td>';
								}
								
								rows += '</tr>';
							}
				            
							json.total = data.total_rows; // only allow 100 rows in total
							//json.filteredRows = 100; // no filtering
							json.rows = $(rows);
							return json;
				            
				    	}
					},
				    container: $(this).find(".ts-pager"),
				    output: '{startRow} to {endRow} ({totalRows})',
				  	cssPageDisplay: '.pagedisplay',
						cssPageSize: '.pagesize',
						cssDisabled: 'disabled'
				})
				
				// bind to pager events
				.bind('pagerInitialized pagerComplete', function(event, options){
					initializeJRating();
					jQuery("time.timeago").timeago();
				});
			});
		 });	
	
		function submitMethod(actionMethod) {
			$('.btn').prop('disabled', true);
			document.QaLearningForm.method.value=actionMethod; 
			document.QaLearningForm.submit();
		}
	</script>
</lams:head>

<body class="stripes">

	<lams:Page type="learner" title="${generalLearnerFlowDTO.activityTitle}">

		<!--  Announcements -->
		<c:if test="${not empty sessionMap.submissionDeadline}">
			<lams:Alert id="submission-deadline" type="info" close="true">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${sessionMap.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert>
		</c:if>

		<!-- Rating limits info -->
		<c:if test="${generalLearnerFlowDTO.allowRateAnswers && (qaContent.minimumRates ne 0 || qaContent.maximumRates ne 0)}">

			<lams:Alert id="ratings-info" type="info" close="true">
				<c:choose>
					<c:when test="${qaContent.minimumRates ne 0 and qaContent.maximumRates ne 0}">
						<fmt:message key="label.rate.limits.reminder">
							<fmt:param value="${qaContent.minimumRates}" />
							<fmt:param value="${qaContent.maximumRates}" />
						</fmt:message>
					</c:when>

					<c:when test="${qaContent.minimumRates ne 0 and qaContent.maximumRates eq 0}">
						<fmt:message key="label.rate.limits.reminder.min">
							<fmt:param value="${qaContent.minimumRates}" />
						</fmt:message>
					</c:when>

					<c:when test="${qaContent.minimumRates eq 0 and qaContent.maximumRates ne 0}">
						<fmt:message key="label.rate.limits.reminder.max">
							<fmt:param value="${qaContent.maximumRates}" />
						</fmt:message>
					</c:when>
				</c:choose>
				<br>

				<fmt:message key="label.rate.limits.topic.reminder">
					<fmt:param value="<span id='count-rated-items'>${sessionMap.countRatedItems}</span>" />
				</fmt:message>
			</lams:Alert>

		</c:if>

		<c:if test="${isLeadershipEnabled}">
			<lams:Alert id="ratings-info" type="info" close="true">
				<fmt:message key="label.group.leader">
					<fmt:param>${sessionMap.groupLeader.fullname}</fmt:param>
				</fmt:message>

			</lams:Alert>
		</c:if>

		<h4>
			<fmt:message key="label.learnerReport" />
		</h4>

		<c:forEach var="userResponse" items="${generalLearnerFlowDTO.userResponses}" varStatus="status">
			<div class="row no-gutter">
				<div class="col-xs-12">
					<div class="panel panel-default">
						<div class="panel-heading panel-title">
							<fmt:message key="label.question" />
							${status.count}:
						</div>
						<div class="panel-body">
							<div class="panel">
								<c:out value="${userResponse.qaQuestion.question}" escapeXml="false" />
							</div>

							<div class="row no-gutter">
								<!-- split if ratings are on -->
								<c:set var="splitRow" value="col-xs-12" />
								<c:if test="${generalLearnerFlowDTO.allowRateAnswers}">
									<c:set var="splitRow" value="col-xs-12 col-sm-9 col-md-9 col-lg-10" />
								</c:if>
								<div class="${splitRow}">
									<div class="sbox">
										<div class="sbox-heading bg-warning">
											<div class="user">
												<c:out value="${userResponse.qaQueUser.fullname}" />
											</div>
											-
											<lams:Date value="${userResponse.attemptTime}" timeago="true"/>
										</div>
										<div class="sbox-body">
											<c:out value="${userResponse.answer}" escapeXml="false" />
										</div>
									</div>
								</div>


								<%--Rating area---------------------------------------%>
								<c:if test="${generalLearnerFlowDTO.allowRateAnswers}">
									<div class="col-xs-12 col-sm-3 col-md-3 col-lg-2">
										<h4 class="text-center">
											<fmt:message key="label.learning.rating" />
										</h4>
										<lams:Rating itemRatingDto="${userResponse.itemRatingDto}" disabled="true" isItemAuthoredByUser="true"
											maxRates="${qaContent.maximumRates}" />
									</div>
								</c:if>
							</div>
						</div>

						<!--  end panel-body -->
					</div>
				</div>
			</div>
		</c:forEach>

		<c:if test="${generalLearnerFlowDTO.teacherViewOnly != 'true' 
				&& (generalLearnerFlowDTO.lockWhenFinished != 'true') && hasEditRight}">
			<div style="overflow: hidden;">
				<html:button property="redoQuestions" styleClass="btn btn-default pull-left"
					onclick="submitMethod('redoQuestions');">
					<fmt:message key="label.redo" />
				</html:button>
			</div>
		</c:if>

		<!-- Others questions -->

		<c:if test="${generalLearnerFlowDTO.showOtherAnswers}">
			<h4>
				<fmt:message key="label.other.answers" />
			</h4>

			<c:forEach var="question" items="${generalLearnerFlowDTO.questions}" varStatus="status">

				<p>
					<strong> <fmt:message key="label.question" /> ${status.count}:
					</strong>
					<c:out value="${question.question}" escapeXml="false" />
				</p>

				<c:if test="${isCommentsEnabled && sessionMap.commentsMinWordsLimit != 0}">
					<div class="info rating-info">
						<fmt:message key="label.comment.minimum.number.words">
							<fmt:param>: ${sessionMap.commentsMinWordsLimit}</fmt:param>
						</fmt:message>
					</div>
				</c:if>

				<c:set var="numColumns" value="1" />
				<c:choose>
					<c:when test="${isCommentsEnabled and generalLearnerFlowDTO.allowRateAnswers}">
						<c:set var="numColumns" value="3" />
					</c:when>
					<c:otherwise>
						<c:set var="numColumns" value="2" />
					</c:otherwise>
				</c:choose>
	
				<lams:TSTable numColumns="${numColumns}" dataId='data-question-uid="${question.uid}"'>
					<thead>
						<tr>
							<th title="<fmt:message key='label.sort.by.answer'/>"><fmt:message key="label.learning.answer" /></th>
							<c:if test="${generalLearnerFlowDTO.allowRateAnswers}">
								<th title="<fmt:message key='label.sort.by.rating'/>"><fmt:message key="label.learning.rating" /></th>
							</c:if>
							<c:if test="${isCommentsEnabled}">
								<th><fmt:message key="label.comment" /></th>
							</c:if>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</lams:TSTable>

			</c:forEach>
		</c:if>

		<!-- reflections -->
		<c:if test="${generalLearnerFlowDTO.reflection == 'true' }">
			<div class="row no-gutter">
				<div class="col-xs-12">
					<div class="panel panel-default voffset10">
						<div class="panel-heading panel-title">
							<fmt:message key="label.reflection" />
						</div>
						<div class="panel-body">
							<div class="reflectionInstructions">
								<lams:out value="${generalLearnerFlowDTO.reflectionSubject}" escapeHtml="true" />
							</div>
							<div class="panel">
								<lams:out value="${QaLearningForm.entryText}" escapeHtml="true" />
							</div>

							<c:if test="${hasEditRight}">
								<html:button property="forwardtoReflection" styleClass="btn btn-default pull-left"
									onclick="submitMethod('forwardtoReflection');">
									<fmt:message key="label.edit" />
								</html:button>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</c:if>


		<c:if test="${generalLearnerFlowDTO.teacherViewOnly != 'true' }">
			<div class="row no-gutter">
				<div class="col-xs-12">
					<button type="submit" id="finishButton" onclick="javascript:submitMethod('endLearning');"
						class="btn btn-primary pull-right na">
						<c:choose>
							<c:when test="${sessionMap.activityPosition.last}">
								<fmt:message key="button.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.endLearning" />
							</c:otherwise>
						</c:choose>
					</button>

				</div>
			</div>
		</c:if>

		<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">
			<html:hidden property="method" />
			<html:hidden property="toolSessionID" styleId="toolSessionID" />
			<html:hidden property="userID" styleId="userID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="totalQuestionCount" />
		</html:form>


		<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>
