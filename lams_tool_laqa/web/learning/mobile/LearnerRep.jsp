<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
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

	<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
	<link rel="stylesheet" href="${lams}css/jquery.jRating.css" />
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme-blue.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
	<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
	<link rel="stylesheet" href="<html:rewrite page='/includes/qalearning.css'/>">
	
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
	<script src="${lams}includes/javascript/jquery.js" type="text/javascript" ></script>
	<script src="${lams}includes/javascript/jquery.jRating.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter.js"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-pager.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.mobile.js" type="text/javascript" ></script>	
	<script src="${lams}includes/javascript/common.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/rating.js" type="text/javascript" ></script> 
	<script type="text/javascript">

		var AVG_RATING_LABEL = '<fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message>',
		YOUR_RATING_LABEL = '<fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message>',
		IS_DISABLED =  ${sessionMap.isDisabled};
	
	  	$(document).bind('pageinit', function(){
	  		
			$(".tablesorter").tablesorter({
				theme: 'blue',
			    widthFixed: true,
			    widgets: ['zebra'],
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
				      // and a filterList = [[2,Blue],[3,13]] becomes "&fcol[2]=Blue&fcol[3]=13" in the url
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
									rows += '<div>';
									rows += 	'<span class="field-name">';
									rows += 		userData["userName"];
									rows += 	'</span> ';
									rows += 	userData["attemptTime"];
									rows += '</div>';
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
											var ratingStarsClass = (IS_DISABLED ||isItemAuthoredByUser || (MAX_RATES > 0) && (countRatedItems >= MAX_RATES)  && ! hasStartedRating || !isCriteriaNotRatedByUser) ? "rating-stars-disabled" : "rating-stars";
								
											rows += '<h4>';
											rows += 	 criteriaDto.title;
											rows += '</h4>';
											
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
				    container: $(this).next(".pager"),
				    output: '{startRow} to {endRow} ({totalRows})',
				    // css class names of pager arrows
				    cssNext: '.tablesorter-next', // next page arrow
					cssPrev: '.tablesorter-prev', // previous page arrow
					cssFirst: '.tablesorter-first', // go to first page arrow
					cssLast: '.tablesorter-last', // go to last page arrow
					cssGoto: '.gotoPage', // select dropdown to allow choosing a page
					cssPageDisplay: '.pagedisplay', // location of where the "output" is displayed
					cssPageSize: '.pagesize', // page size selector - select dropdown that sets the "size" option
					// class added to arrows when at the extremes (i.e. prev/first arrows are "disabled" when on the first page)
					cssDisabled: 'disabled' // Note there is no period "." in front of this class name
				})
				
				// bind to pager events
				.bind('pagerInitialized pagerComplete', function(event, options){
					initializeJRating();
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

<body>
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<c:out value="${generalLearnerFlowDTO.activityTitle}" escapeXml="false" />
		</h1>
	</div><!-- /header -->

	<div data-role="content">	
	
		<c:if test="${not empty sessionMap.submissionDeadline}">
			<div class="info">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</div>
		</c:if>
		
		<!-- Rating limits info -->
		<c:if test="${generalLearnerFlowDTO.allowRateAnswers && (qaContent.minimumRates ne 0 || qaContent.maximumRates ne 0)}">
		
			<div class="info">
				<c:choose>
					<c:when test="${qaContent.minimumRates ne 0 and qaContent.maximumRates ne 0}">
						<fmt:message key="label.rate.limits.reminder">
							<fmt:param value="${qaContent.minimumRates}"/>
							<fmt:param value="${qaContent.maximumRates}"/>
						</fmt:message>
					</c:when>
					
					<c:when test="${qaContent.minimumRates ne 0 and qaContent.maximumRates eq 0}">
						<fmt:message key="label.rate.limits.reminder.min">
							<fmt:param value="${qaContent.minimumRates}"/>
						</fmt:message>
					</c:when>
					
					<c:when test="${qaContent.minimumRates eq 0 and qaContent.maximumRates ne 0}">
						<fmt:message key="label.rate.limits.reminder.max">
							<fmt:param value="${qaContent.maximumRates}"/>
						</fmt:message>
					</c:when>
				</c:choose>
				<br>
						
				<fmt:message key="label.rate.limits.topic.reminder">
					<fmt:param value="<span id='count-rated-items'>${sessionMap.countRatedItems}</span>"/>
				</fmt:message>
			</div>
			
		</c:if>
		
		<c:if test="${isLeadershipEnabled}">
			<h4>
				<fmt:message key="label.group.leader" >
					<fmt:param>${sessionMap.groupLeader.fullname}</fmt:param>
				</fmt:message>
			</h4>
		</c:if>
		
		<h2 class="space-bottom">
			<fmt:message key="label.learnerReport" />
		</h2>
		
		<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">
			<html:hidden property="toolSessionID" styleId="toolSessionID"/>
			<html:hidden property="userID" styleId="userID"/>
			<html:hidden property="httpSessionID" />
			<html:hidden property="totalQuestionCount"/>
			
			<c:if test="${generalLearnerFlowDTO.requestLearningReportProgress != 'true'}">
				<c:choose>
					<c:when test="${(generalLearnerFlowDTO.reflection != 'true') || !hasEditRight}">
						<html:hidden property="method" value="endLearning"/>
					</c:when>
					<c:otherwise>
						<html:hidden property="method" value="forwardtoReflection"/>
					</c:otherwise>
				</c:choose>
			</c:if>

			<c:if test="${generalLearnerFlowDTO.requestLearningReportProgress == 'true'}">
				<html:hidden property="method" />
			</c:if>			
		</html:form>
	
		<ul data-role="listview" data-theme="c" id="qaQuestions" style="padding-top: 0;">
			<c:forEach var="userResponse" items="${generalLearnerFlowDTO.userResponses}" varStatus="status">
				<li>
				
					<p class="space-top">
						<strong> <fmt:message key="label.question" /> ${status.count}: </strong>
						<c:out value="${userResponse.qaQuestion.question}" escapeXml="false" />
					</p>

					<p class="small-space-top">
						<span class="field-name"> <c:out value="${userResponse.qaQueUser.fullname}" /> </span> -
							<lams:Date value="${userResponse.attemptTime}" style="short"/>
					</p>
					<div class="small-space-bottom user-answer">
						<c:out value="${userResponse.answer}" escapeXml="false" />
					</div>
					
					<%--Rating area---------------------------------------%>
					<c:if test="${generalLearnerFlowDTO.allowRateAnswers}">
					<lams:Rating itemRatingDto="${userResponse.itemRatingDto}" disabled="true" isItemAuthoredByUser="true"
							maxRates="${qaContent.maximumRates}"/>
						<br>
					</c:if>
					
				</li>
			</c:forEach>	
		</ul>
		
		<c:if test="${generalLearnerFlowDTO.showOtherAnswers}">				
			<h2 style="padding-top: 20px; padding-bottom: 0;">
				<fmt:message key="label.other.answers" />
			</h2>
					
			<ul data-role="listview" data-theme="c" id="qaAnswers">
				<c:forEach var="question" items="${generalLearnerFlowDTO.questions}" varStatus="status">			
			
					<li>
						<p class="space-top">
							<strong> <fmt:message key="label.question" /> ${status.count}: </strong>
							<c:out value="${question.question}" escapeXml="false" />
						</p>
						
						<c:if test="${isCommentsEnabled && sessionMap.commentsMinWordsLimit != 0}">
							<div class="info rating-info">
								<fmt:message key="label.comment.minimum.number.words">
									<fmt:param>: ${sessionMap.commentsMinWordsLimit}</fmt:param>
								</fmt:message>
							</div>
						</c:if>
										
						<table class="tablesorter" data-question-uid="${question.uid}">
							<thead>
								<tr>
									<th title="<fmt:message key='label.sort.by.answer'/>" >
										<fmt:message key="label.learning.answer" />
									</th>
									<c:if test="${generalLearnerFlowDTO.allowRateAnswers}">
										<th title="<fmt:message key='label.sort.by.rating'/>">
											<fmt:message key="label.learning.rating" />
										</th>
									</c:if>
									<c:if test="${isCommentsEnabled}">
										<th>
											<fmt:message key="label.comment" />
										</th>
									</c:if>
								</tr>
							</thead>
							<tbody>

							</tbody>
						</table>
					
						<!-- pager -->
						<div class="pager">
							<form>
							   	<img class="tablesorter-first"/>
						    	<img class="tablesorter-prev"/>
						    	<span class="pagedisplay"></span> <!-- this can be any element, including an input -->
						   		<img class="tablesorter-next"/>
						    	<img class="tablesorter-last"/>
						    	<select class="pagesize">
						      		<option selected="selected" value="10">10</option>
						      		<option value="20">20</option>
						      		<option value="30">30</option>
						      		<option value="40">40</option>
						      		<option value="50">50</option>
						      		<option value="100">100</option>
						    	</select>
						  </form>
						</div>							
							
					</li>
				</c:forEach>
			</ul>
		</c:if>	
		
		<c:if test="${generalLearnerFlowDTO.requestLearningReportViewOnly != 'true' }">
			<c:if test="${generalLearnerFlowDTO.teacherViewOnly != 'true' }">
				<div class="button-inside">
					<button name="refreshAnswers" onclick="submitMethod('refreshAllResults');" data-icon="refresh">
						<fmt:message key="label.refresh" />
					</button>
	
					<c:if test="${(generalLearnerFlowDTO.lockWhenFinished != 'true') && hasEditRight}">
						<button name="redoQuestions" onclick="submitMethod('redoQuestions');" data-icon="back">
							<fmt:message key="label.redo" />
						</button>
					</c:if>
				</div>
			</c:if>	
		</c:if>
	</div>
	
	<div data-role="footer" data-theme="b" class="ui-bar">
		<c:if test="${generalLearnerFlowDTO.requestLearningReportViewOnly != 'true' }">
			<c:if test="${generalLearnerFlowDTO.teacherViewOnly != 'true' }">
				<span class="ui-finishbtn-right" id="learner-submit">
					<c:if test="${(generalLearnerFlowDTO.reflection != 'true') || !hasEditRight}">
						<a href="#nogo" name="endLearning" id="finishButton" onclick="javascript:submitMethod('endLearning');" data-role="button" data-icon="arrow-r" data-theme="b">
							<span class="nextActivity">
								<c:choose>
				 					<c:when test="${sessionMap.activityPosition.last}">
				 						<fmt:message key="button.submit" />
				 					</c:when>
				 					<c:otherwise>
				 		 				<fmt:message key="button.endLearning" />
				 					</c:otherwise>
				 				</c:choose>
				 			</span>
						</a>
					</c:if>

					<c:if test="${(generalLearnerFlowDTO.reflection == 'true') && hasEditRight}">
						<button name="forwardtoReflection"	onclick="javascript:submitMethod('forwardtoReflection');" data-icon="arrow-r" data-theme="b">
							<fmt:message key="label.continue" />
						</button>
					</c:if>
				</span>
			</c:if>
		</c:if>
		<c:if test="${generalLearnerFlowDTO.requestLearningReportViewOnly == 'true' }">
			<h2>&nbsp;</h2>
		</c:if>
	</div><!-- /footer -->

</div>
</body>
</lams:html>
