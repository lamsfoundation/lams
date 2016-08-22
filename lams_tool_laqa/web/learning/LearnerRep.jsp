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

<lams:html>
<lams:head>
	<html:base />
	<title><fmt:message key="activity.title" /></title>
	<link type="text/css" href="${lams}css/jquery.jRating.css" rel="stylesheet" />
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme-blue.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
	<link rel="stylesheet" href="<html:rewrite page='/includes/qalearning.css'/>">
	<lams:css />

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
		WARN_MIN_NUMBER_WORDS_LABEL = "<fmt:message key="warning.minimum.number.words"><fmt:param value="${sessionMap.commentsMinWordsLimit}"/></fmt:message>";
	</script>
	<script src="${lams}includes/javascript/jquery.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.jRating.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-pager.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.timeago.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/common.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/rating.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/bootstrap.min.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-widgets.js" type="text/javascript"></script>


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
					// set to false otherwise it remembers setting from other jsFiddle demos
					savePages: false,
				      // use this format: "http:/mydatabase.com?page={page}&size={size}&{sortList:col}"
				      // where {page} is replaced by the page number (or use {page+1} to get a one-based index),
				      // {size} is replaced by the number of records to show,
				      // {sortList:col} adds the sortList to the url into a "col" array, and {filterList:fcol} adds
				      // the filterList to the url into an "fcol" array.
				      // So a sortList = [[2,0],[3,0]] becomes "&col[2]=0&col[3]=0" in the url
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

								if (${generalLearnerFlowDTO.allowRateAnswers == 'true'}) {
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
											var ratingStarsClass = (IS_DISABLED || isItemAuthoredByUser || (MAX_RATES > 0) && (countRatedItems >= MAX_RATES)  && (!hasStartedRating) || !isCriteriaNotRatedByUser) ? "rating-stars-disabled" : "rating-stars";
								
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
											rows +=		'<textarea class="form-control" name="comment" rows="4" id="comment-textarea-'+ itemId +'" onfocus="if(this.value==this.defaultValue)this.value=\'\';" onblur="if(this.value==\'\')this.value=this.defaultValue;"><fmt:message key="label.comment.textarea.tip"/></textarea>';
											
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
	
		function submitLearningMethod(actionMethod) {	
			if (actionMethod == 'endLearning') {
				$("#finishButton").attr("disabled", true);
			}
			document.QaLearningForm.method.value=actionMethod; 
			document.QaLearningForm.submit();
		}
		
		function submitMethod(actionMethod) {
			submitLearningMethod(actionMethod);
		}
	</script>
</lams:head>

<body class="stripes">

	<!-- form needs to be outside page so that the form bean can be picked up by Page tag. -->
	<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">

	<lams:Page type="learner" title="${qaContent.title}">

		<!-- Announcements and advanced settings -->

		<c:if test="${not empty sessionMap.submissionDeadline}">
			<lams:Alert id="submission-deadline" type="danger" close="false">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${sessionMap.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert>
		</c:if>

		<!-- Rating limits info -->
		<c:if test="${qaContent.allowRateAnswers && (qaContent.minimumRates ne 0 || qaContent.maximumRates ne 0)}">

			<lams:Alert id="rating-info" type="info" close="true">
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
		<!-- End Announcements and advanced settings -->

		<c:if test="${isLeadershipEnabled}">
			<c:set var="fullName">
				<c:out value="${sessionMap.groupLeader.fullname}" escapeXml="true" />
			</c:set>
			<h4>
				<fmt:message key="label.group.leader">
					<fmt:param>${fullName}</fmt:param>
				</fmt:message>
			</h4>
		</c:if>

		<h4>
			<fmt:message key="label.learnerReport" />
		</h4>

		<!-- Questions and answers -->
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
								<c:if test="${qaContent.allowRateAnswers}">
									<c:set var="splitRow" value="col-xs-12 col-sm-9 col-md-10 col-lg-10" />
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
								<c:if test="${qaContent.allowRateAnswers}">
									<div class="col-xs-12 col-sm-3 col-md-2 col-lg-2">
										<h4 class="text-center">
											<fmt:message key="label.learning.rating" />
										</h4>
										<lams:Rating itemRatingDto="${userResponse.itemRatingDto}" disabled="true" isItemAuthoredByUser="true"
											maxRates="${qaContent.maximumRates}" />
									</div>
								</c:if>
							</div>
						</div>
					</div>
				</div>
			</div>
		</c:forEach>
		<!-- End questions and answers -->

		<!-- Others questions -->

		<c:if test="${generalLearnerFlowDTO.existMultipleUserResponses == 'true'}">
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
					<lams:Alert id="rating-info" type="info" close="false">
						<fmt:message key="label.comment.minimum.number.words">
							<fmt:param>: ${sessionMap.commentsMinWordsLimit}</fmt:param>
						</fmt:message>
					</lams:Alert>
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
							<c:if test="${generalLearnerFlowDTO.allowRateAnswers == 'true'}">
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

		<!-- End of others questions -->

		<!-- buttons -->
			<html:hidden property="toolSessionID" styleId="toolSessionID" />
			<html:hidden property="userID" styleId="userID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="totalQuestionCount" />

			<c:choose>
				<c:when test="${generalLearnerFlowDTO.requestLearningReportProgress != 'true'}">
					<c:choose>
						<c:when test="${(generalLearnerFlowDTO.reflection != 'true') || !hasEditRight}">
							<html:hidden property="method" value="endLearning" />
						</c:when>
						<c:otherwise>
							<html:hidden property="method" value="forwardtoReflection" />
						</c:otherwise>
					</c:choose>
				</c:when>

				<c:otherwise>
					<html:hidden property="method" />
				</c:otherwise>
			</c:choose>

			<c:if test="${generalLearnerFlowDTO.requestLearningReportViewOnly != 'true' }">
				<c:if test="${generalLearnerFlowDTO.teacherViewOnly != 'true' }">

					<div class="right-buttons voffset5" align="right" id="learner-submit">
						<html:button property="refreshAnswers" styleClass="btn btn-default voffset5 roffset5 pull-left"
							onclick="submitMethod('refreshAllResults');">
							<fmt:message key="label.refresh" />
						</html:button>

						<c:if test="${(generalLearnerFlowDTO.lockWhenFinished != 'true') && hasEditRight}">
							<html:button property="redoQuestions" styleClass="btn btn-default voffset5  pull-left"
								onclick="submitMethod('redoQuestions');">
								<fmt:message key="label.redo" />
							</html:button>
						</c:if>


						<c:if test="${(generalLearnerFlowDTO.reflection != 'true') || !hasEditRight}">
							<html:link href="#nogo" property="endLearning" styleId="finishButton"
								onclick="javascript:submitMethod('endLearning'); return false;" styleClass="btn btn-primary pull-right na">
								<c:choose>
									<c:when test="${sessionMap.activityPosition.last}">
										<fmt:message key="button.submit" />
									</c:when>
									<c:otherwise>
										<fmt:message key="button.endLearning" />
									</c:otherwise>
								</c:choose>
							</html:link>
						</c:if>

						<c:if test="${(generalLearnerFlowDTO.reflection == 'true') && hasEditRight}">
							<html:button property="forwardtoReflection" onclick="javascript:submitMethod('forwardtoReflection');"
								styleClass="btn btn-default">
								<fmt:message key="label.continue" />
							</html:button>
						</c:if>
					</div>
				</c:if>
			</c:if>



		<div id="footer"></div>



	</lams:Page>

	</html:form>

</body>
</lams:html>
