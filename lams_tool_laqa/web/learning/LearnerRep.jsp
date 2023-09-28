<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.sessionMapID]}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="qaContent" value="${sessionMap.content}" />
<c:set var="isLeadershipEnabled" value="${qaContent.useSelectLeaderToolOuput}" />
<c:set var="isCommentsEnabled" value="${sessionMap.isCommentsEnabled}" />
<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" />
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>

<lams:PageLearner title="${generalLearnerFlowDTO.activityTitle}" toolSessionID="${sessionMap.toolSessionID}" >

	<lams:css suffix="jquery.jRating"/>

	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager5.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap5.css">
	<link rel="stylesheet" href="<lams:WebAppURL/>includes/css/qalearning.css">
	<style>
		#rating-comment-info {
			margin: 10px 0;
			padding: 5px;
		}

		.other-users-responses {
			margin-bottom: -15px;
		}

		.add-comment {
			margin-left: 10px;
			float: left !important;
		}

		.comment-textarea {
			float: left;
			width: 85%;
		}

		.other-users-responses .table>thead>tr>th {
			line-height: 0.2;
		}

		.rate-answers-area {
			min-height: 120px;
			margin-bottom: 20px;
		}
	</style>

	<!-- form needs to be outside page so that the form bean can be picked up by Page tag. -->
	<form:form action="/lams/tool/laqa11/learning/learning.do"  modelAttribute="qaLearningForm" method="POST" target="_self">
		<form:hidden path="toolSessionID" id="toolSessionID" />
		<form:hidden path="userID" id="userID" />
		<form:hidden path="sessionMapID" />
		<form:hidden path="totalQuestionCount" />
		<form:hidden path="refreshAnswers" />

		<!-- Announcements -->
		<c:if test="${not empty sessionMap.submissionDeadline}">
			<lams:Alert5 id="submission-deadline" type="danger" close="false">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${sessionMap.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert5>
		</c:if>

		<!-- Rating limits info -->
		<c:if test="${generalLearnerFlowDTO.allowRateAnswers && (qaContent.minimumRates ne 0 || qaContent.maximumRates ne 0)}">
			<lams:Alert5 id="rating-info" type="info" close="true">
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
			</lams:Alert5>
		</c:if>

		<c:if test="${isLeadershipEnabled}">
			<lams:LeaderDisplay idName="leader-enabled" username="${sessionMap.groupLeader.fullname}" userId="${sessionMap.groupLeader.queUsrId}"/>
		</c:if>

		<!-- Questions and answers -->
		<c:forEach var="question" items="${generalLearnerFlowDTO.questions}" varStatus="status">

			<div class="card lcard">
				<div class="card-header text-bg-secondary">
					<c:if test="${generalLearnerFlowDTO.questions.size() != 1}">${status.count}.&nbsp;</c:if>
					<c:out value="${question.qbQuestion.name}" escapeXml="false" />
					
					<div class="font-size-init">
						<c:out value="${question.qbQuestion.description}" escapeXml="false" />
					</div>
				</div>

				<div class="card-body">
						
					<%--User own responses---------------------------------------%>
					<c:set var="userResponse" value="${question.userResponse}"/>
					<c:if test="${userResponse != null}">
						<div class="row g-0">
							<!-- split if ratings are on -->
							<c:set var="splitRow" value="col-12" />
							<c:if test="${generalLearnerFlowDTO.allowRateAnswers}">
								<c:set var="splitRow" value="col-12 col-sm-9 col-md-10 col-lg-10" />
							</c:if>
							
							<div class="${splitRow}">
								<div class="sbox">
									<div class="sbox-heading bg-warning bg-opacity-50">
										<div class="float-start me-2">
											<lams:Portrait userId="${userResponse.qaQueUser.queUsrId}"/>
										</div>
										<span><c:out value="${userResponse.qaQueUser.fullname}" /></span>
										<br>
										<span style="font-size: smaller">
											<lams:Date value="${userResponse.attemptTime}" timeago="true"/>
										</span>
									</div>
									<div class="sbox-body">
										<c:out value="${userResponse.answer}" escapeXml="false" />
									</div>
								</div>
							</div>
							
							<c:if test="${generalLearnerFlowDTO.allowRateAnswers}">
								<div class="col-12 col-sm-3 col-md-2 col-lg-2 rate-answers-area">
									<div class="text-center fs-4">
										<fmt:message key="label.learning.rating" />
									</div>
									<lams:Rating5 itemRatingDto="${userResponse.itemRatingDto}" disabled="true" isItemAuthoredByUser="true"
												 maxRates="${qaContent.maximumRates}" />
								</div>
							</c:if>
						</div>
					</c:if>

					<!-- Others users' responses -->
					<c:if test="${generalLearnerFlowDTO.showOtherAnswers}">
						<c:if test="${isCommentsEnabled && sessionMap.commentsMinWordsLimit != 0}">
							<lams:Alert5 id="rating-comment-info-${status.count}" type="info" close="false" >
								<fmt:message key="label.comment.minimum.number.words">
									<fmt:param>: ${sessionMap.commentsMinWordsLimit}</fmt:param>
								</fmt:message>
							</lams:Alert5>
						</c:if>

						<c:choose>
							<c:when test="${isCommentsEnabled}">
								<c:set var="numColumns" value="3" />
							</c:when>
							<c:when test="${!isCommentsEnabled and generalLearnerFlowDTO.allowRateAnswers}">
								<c:set var="numColumns" value="2" />
							</c:when>
							<c:otherwise>
								<c:set var="numColumns" value="1" />
							</c:otherwise>
						</c:choose>

						<div class="other-users-responses mt-4">
							<div class="fst-italic mb-2">
								<fmt:message key="label.other.learners.answers" />
							</div>
						
							<lams:TSTable5 numColumns="${numColumns}" dataId='data-question-uid="${question.uid}"' tableClass="tablesorter jRating">
								<th title="<fmt:message key='label.sort.by.answer'/>">
									<fmt:message key="label.learning.answer" />
								</th>
									
								<c:choose>
									<c:when test="${generalLearnerFlowDTO.allowRateAnswers}">
										<th title="<fmt:message key='label.sort.by.rating'/>">
											<fmt:message key="label.learning.rating" />
										</th>
									</c:when>
									<c:otherwise>
										<!-- <th style="display:none;"></th> -->
									</c:otherwise>
								</c:choose>
									
								<c:if test="${isCommentsEnabled}">
									<th>
										<fmt:message key="label.comment" />
									</th>
								</c:if>
							</lams:TSTable5>
						</div>
					</c:if>

				</div>
			</div>

		</c:forEach>
		<!-- End questions and answers -->

		<!-- reflections -->
		<c:if test="${generalLearnerFlowDTO.reflection == 'true' && generalLearnerFlowDTO.isLearnerFinished}">
			<lams:NotebookReedit
				reflectInstructions="${generalLearnerFlowDTO.reflectionSubject}"
				reflectEntry="${qaLearningForm.entryText}"
				isEditButtonEnabled="${hasEditRight && mode != 'teacher'}"
				notebookHeaderLabelKey="label.reflection"/>
		</c:if>

		<!-- buttons -->
		<c:if test="${mode != 'teacher'}">
			<div class="activity-bottom-buttons" id="learner-submit">
				<c:choose>
					<c:when test="${(generalLearnerFlowDTO.reflection == 'true') && hasEditRight && !generalLearnerFlowDTO.isLearnerFinished}">
						<button type="button" name="forwardtoReflection" onclick="javascript:submitMethod('forwardtoReflection');"
								class="btn btn-primary na">
							<fmt:message key="label.continue" />
						</button>
					</c:when>

					<c:when test="${(generalLearnerFlowDTO.reflection != 'true') || !hasEditRight || generalLearnerFlowDTO.isLearnerFinished}">
						<button type="button" id="finishButton" class="btn btn-primary na">
							<c:choose>
								<c:when test="${sessionMap.isLastActivity}">
									<fmt:message key="button.submit" />
								</c:when>
								<c:otherwise>
									<fmt:message key="button.endLearning" />
								</c:otherwise>
							</c:choose>
						</button>
					</c:when>
				</c:choose>

				<c:if test="${(generalLearnerFlowDTO.lockWhenFinished != 'true') && hasEditRight && !generalLearnerFlowDTO.noReeditAllowed}">
					<button name="redoQuestions" type="button" class="btn btn-secondary btn-icon-return me-2"
							onclick="submitMethod('redoQuestions');">
						<fmt:message key="label.redo" />
					</button>
				</c:if>
				
				<c:if test="${!generalLearnerFlowDTO.isLearnerFinished}">
					<button type="button" class="btn btn-secondary btn-icon-refresh me-2"
							onclick="refreshPage('${qaLearningForm.refreshAnswers}');">
						<fmt:message key="label.refresh" />
					</button>
				</c:if>
			</div>
		</c:if>
	</form:form>

	<script type="text/javascript">
		//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/";

		//vars for rating.js
		var MAX_RATES = ${qaContent.maximumRates},
				MIN_RATES = ${qaContent.minimumRates},
				COMMENTS_MIN_WORDS_LIMIT = ${sessionMap.commentsMinWordsLimit},
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
	<script src="${lams}includes/javascript/jquery.timeago.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/common.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/rating.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-widgets.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js" type="text/javascript"></script>
	<lams:JSImport src="includes/javascript/portrait5.js" />

	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${toolSessionID}', '', function(){
			submitMethod('endLearning');
		});

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
					ajaxUrl : "<c:url value='getResponses.do'/>?page={page}&size={size}&{sortList:column}&isAllowRateAnswers=${qaContent.allowRateAnswers}&isAllowRichEditor=${qaContent.allowRichEditor}&isOnlyLeadersIncluded=${qaContent.useSelectLeaderToolOuput}&qaContentId=${qaContent.qaContentId}&qaSessionId=" + $("#toolSessionID").val() + "&questionUid=" + $(this).attr('data-question-uid') + "&userId=" + $("#userID").val() + "&reqID=" + (new Date()).getTime(),
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
								rows += '<td style="vertical-align:top;">';

								if (${generalLearnerFlowDTO.userNameVisible}) {
									rows += definePortraitMiniHeader(userData["portraitId"], userData["userID"], '${lams}',
											${qaContent.useSelectLeaderToolOuput} ? userData["sessionName"] : userData["userName"],
											'<time class="timeago" title="' + userData["attemptTime"] + '" datetime="' + userData["timeAgo"] + '"></time>',
											false, "sbox-heading bg-warning bg-opacity-50")
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
									rows += '<td style="width:150px;vertical-align:top;">';

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
								} else {
									// need a column or sorting is confused
									rows += '<td style="display:none;"></td>';
								}

								if (${isCommentsEnabled}) {
									rows += '<td style="width:30%; min-width: 250px; vertical-align:top;" id="comments-area-' + itemId + '">';

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
											rows += '<div id="add-comment-area-' + itemId + '">' +
														'<textarea class="form-control comment-textarea" name="comment" rows="4" id="comment-textarea-'+ itemId +'" onfocus="if(this.value==this.defaultValue)this.value=\'\';" onblur="if(this.value==\'\')this.value=this.defaultValue;" placeholder="<fmt:message key="label.comment.textarea.tip"/>" aria-label="<fmt:message key="label.comment.textarea.tip"/>"></textarea>' +
													 	'<button type="button" class="btn btn-success btn-sm add-comment add-comment-new" data-item-id="'+ itemId +'" data-comment-criteria-id="'+ commentsCriteriaId +'" aria-label="submit">' +
														 	'<i class="fa fa-check fa-lg"></i>' + 
														 '</button>' +
													'</div>';
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

		function refreshPage(reload) {
			if ( "reload" == reload ) {
				location.href = "<lams:WebAppURL/>learning/learning.do?mode=learner&toolSessionID="+SESSION_ID;
			} else {
				submitMethod('refreshAllResults');
			}
		}
		
		function continueReflect() {
			submitMethod('forwardtoReflection');
		}
		
		function submitMethod(actionMethod) {
			document.forms.qaLearningForm.action=actionMethod+".do";
			document.forms.qaLearningForm.submit();
		}
	</script>
</lams:PageLearner>