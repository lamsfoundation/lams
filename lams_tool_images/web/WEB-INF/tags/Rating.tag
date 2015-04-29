<% 
 /**
  * Rating.tag
  *	Author: Andrey Balan
  *	Description: Shows rating stars widget
  */
 %>
<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>
<c:set var="lams"><lams:LAMSURL/></c:set>

<%@ attribute name="ratingDtos" required="true" rtexprvalue="true" type="java.util.Collection" %>

<%-- Optional attribute --%>
<%@ attribute name="disabled" required="false" rtexprvalue="true" %>
<%@ attribute name="isItemAuthoredByUser" required="false" rtexprvalue="true" %>
<%@ attribute name="maxRates" required="false" rtexprvalue="true" %>
<%@ attribute name="minRates" required="false" rtexprvalue="true" %>
<%@ attribute name="countRatedItems" required="false" rtexprvalue="true" %>
<%@ attribute name="yourRatingLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="averageRatingLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="warnCommentIsBlankLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="minNumberWordsLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="warnMinNumberWordsLabel" required="false" rtexprvalue="true" %>

<%-- Default value for message key --%>
<c:if test="${empty disabled}">
	<c:set var="disabled" value="false" scope="request"/>
</c:if>
<c:if test="${empty isItemAuthoredByUser}">
	<c:set var="isItemAuthoredByUser" value="false" scope="request"/>
</c:if>
<c:if test="${empty maxRates}">
	<c:set var="maxRates" value="0" scope="request"/>
</c:if>
<c:if test="${empty minRates}">
	<c:set var="minRates" value="0" scope="request"/>
</c:if>
<c:if test="${empty countRatedItems}">
	<c:set var="countRatedItems" value="0" scope="request"/>
</c:if>
<c:if test="${empty yourRatingLabel}">
	<c:set var="yourRatingLabel" value="label.your.rating" scope="request"/>
</c:if>
<c:if test="${empty averageRatingLabel}">
	<c:set var="averageRatingLabel" value="label.average.rating" scope="request"/>
</c:if>
<c:if test="${empty warnCommentIsBlankLabel}">
	<c:set var="warnCommentIsBlankLabel" value="warn.comment.blank" scope="request"/>
</c:if>
<c:if test="${empty minNumberWordsLabel}">
	<c:set var="minNumberWordsLabel" value="label.minimum.number.words" scope="request"/>
</c:if>
<c:if test="${empty warnMinNumberWordsLabel}">
	<c:set var="warnMinNumberWordsLabel" value="warning.minimum.number.words" scope="request"/>
</c:if>

<%--Find commentsRatingDto--%>
<c:forEach var="ratingDto" items="${ratingDtos}" varStatus="status">
	<c:if test="${ratingDto.ratingCriteria.commentsEnabled}">
		<c:set var="commentsRatingDto" value="${ratingDto}"/>
	</c:if>
</c:forEach>
<c:set var="isCommentsEnabled" value="${not empty commentsRatingDto}"/>

<link type="text/css" href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
<script type="text/javascript">
	//var for jquery.jRating.js
	var pathToImageFolder = "${lams}images/css/";
</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/common.js"></script> 

<!-- begin tab content -->
<script type="text/javascript">

	//constant indicating there is rting limits set up
	var hasRatingLimits = ${maxRates!=0 || minRates!=0};

	$(document).ready(function(){
		setupJRating();
		
		if (${minRates} != 0) {
			checkMinimumRatesLimit(${countRatedItems});
		}

    	//addNewComment button handler
	    $('#add-comment-button').click(function() {
	    	
	    	//replace special characters with HTML tags
	    	var tempTextarea = jQuery('<textarea/>');
	    	filterData(document.getElementById('comment-textarea'), tempTextarea);
			var comment = tempTextarea.value;
			
			//comment can't be blank
			if (comment == "") {
				alert("${warnCommentIsBlankLabel}");
				return false;
			}
	    	
			//word count limit
			if (${isCommentsEnabled && commentsRatingDto.commentsMinWordsLimit ne 0}) {
				var value =  $("#comment-textarea").val();
				value = value.trim();
				
			    var wordCount = value ? (value.replace(/['";:,.?\-!]+/g, '').match(/\S+/g) || []).length : 0;
			    
			    if(wordCount < ${commentsRatingDto.commentsMinWordsLimit}){
					alert('<fmt:message key="${warnMinNumberWordsLabel}"><fmt:param value="${commentsRatingDto.commentsMinWordsLimit}"/><fmt:param value="' + wordCount + '"/></fmt:message>');
					return false;
				}
			}
	    	
	    	//add new comment
	    	$.ajax({
	    		type: "POST",
	    		url: '${lams}servlet/rateItem',
	    		data: {
	    			idBox: '${commentsRatingDto.ratingCriteria.ratingCriteriaId}-${commentsRatingDto.itemId}', 
	    			comment: comment,
	    			hasRatingLimits: hasRatingLimits
	    		},
	    		success: function(data, itemId) {
	    				
	    			//add comment to HTML
	    			jQuery('<div/>', {
	    				'class': "rating-comment",
	    			    html: data.comment
	    			}).appendTo('#comments-area');
	    				
	    			//hide comments textarea and button
	    			$("#add-comment-area").hide();
	    			
	    			//handle rating limits if available
	    			if (hasRatingLimits) {
	    				handleRatingLimits(data.countRatedItems);
	    			}
	    				
	    		}
	    	});
	    });
		
	});
	
	//initialize jRating
	function setupJRating() {
		
		$(".rating-stars-new").filter($(".rating-stars")).jRating({
		    phpPath : "${lams}servlet/rateItem?hasRatingLimits=" + hasRatingLimits,
		    rateMax : 5,
		    decimalLength : 1,
			onSuccess : function(data, itemId){
				
				$("#user-rating-" + itemId).html(data.userRating);
			    $("#average-rating-" + itemId).html(data.averageRating);
			    $("#number-of-votes-" + itemId).html(data.numberOfVotes);
			    $("#rating-stars-caption-" + itemId).css("visibility", "visible");
			    
			    //handle rating limits if available
			    handleRatingLimits(data.countRatedItems);

			},
			onError : function(){
			    jError('Error. Please, retry');
			}
		});
		
		$(".rating-stars-new").filter($(".rating-stars-disabled")).jRating({
	    	rateMax : 5,
	    	isDisabled : true
		});
		
		$(".rating-stars-new").removeClass("rating-stars-new");
	}
	
	function handleRatingLimits(countRatedItems) {
		
	    if (hasRatingLimits) {

	    	//update info box
	    	$("#count-rated-items").html(countRatedItems);
	    	
		    //callback function
		    if (typeof onRatingSuccessCallback === "function") { 
		        // safe to use the function
		    	onRatingSuccessCallback(countRatedItems);
		    }
		    
		    //handle max rates limit
		    if (${maxRates} != 0) {
		    	
		    	//disable rating feature in case maxRates limit reached
		    	if (countRatedItems >= ${maxRates}) {
			    	$(".rating-stars").each(function() {
			    		$(this).unbind().css('cursor','default').addClass('jDisabled');
			    	});			    		
		    	}
		    }
		    
		    //handle min rates limit
		    if (${minRates} != 0) {
		    	checkMinimumRatesLimit(countRatedItems)
		    }
	    }
	}
	
	function checkMinimumRatesLimit(countRatedItems) {
		$( "#learner-submit" ).toggle( countRatedItems >= ${minRates} );
	}
	
</script>


<%--Rating stars area---------------------------------------%>

<div class="extra-controls-inner">
<div class="rating-stars-holder">
	<c:forEach var="ratingDto" items="${ratingDtos}" varStatus="status">
		<c:if test="${!ratingDto.ratingCriteria.commentsEnabled}">
			<c:set var="objectId" value="${ratingDto.ratingCriteria.ratingCriteriaId}-${ratingDto.itemId}"/>
			<c:set var="isCriteriaNotRatedByUser" value='${ratingDto.userRating == ""}'/>
	
			<c:choose>
				<c:when test='${disabled || isItemAuthoredByUser || (maxRates > 0) && (countRatedItems >= maxRates) || !isCriteriaNotRatedByUser}'>
					<c:set var="ratingStarsClass" value="rating-stars-disabled"/>
				</c:when>
				<c:otherwise>
					<c:set var="ratingStarsClass" value="rating-stars"/>
				</c:otherwise>
			</c:choose>
			
			<h4>
				${ratingDto.ratingCriteria.title}
			</h4>
			
			<c:choose>
				<c:when test='${isItemAuthoredByUser || not isCriteriaNotRatedByUser}'>
					<c:set var="ratingDataAverage" value="${ratingDto.averageRating}"/>
				</c:when>
				<c:otherwise>
					<c:set var="ratingDataAverage" value="0"/>
				</c:otherwise>
			</c:choose>
		
			<div class="${ratingStarsClass} rating-stars-new" data-average="${ratingDataAverage}" data-id="${objectId}">
			</div>
			
			<c:choose>
				<c:when test="${isItemAuthoredByUser}">
					<div class="rating-stars-caption">
						<fmt:message key="${averageRatingLabel}" >
							<fmt:param>
								<fmt:formatNumber value="${ratingDto.averageRating}" type="number" maxFractionDigits="1" />
							</fmt:param>
							<fmt:param>
								${ratingDto.numberOfVotes}
							</fmt:param>
						</fmt:message>
					</div>
				</c:when>
				
				<c:otherwise>
					<div class="rating-stars-caption" id="rating-stars-caption-${objectId}"
						<c:if test="${isItemAuthoredByUser || isCriteriaNotRatedByUser}">style="visibility: hidden;"</c:if>
					>
						<fmt:message key="${yourRatingLabel}" >
							<fmt:param>
								<span id="user-rating-${objectId}">
									<fmt:formatNumber value="${ratingDto.userRating}" type="number" maxFractionDigits="1" />
								</span>
							</fmt:param>			
							<fmt:param>
								<span id="average-rating-${objectId}">
									<fmt:formatNumber value="${ratingDto.averageRating}" type="number" maxFractionDigits="1" />
								</span>
							</fmt:param>
							<fmt:param>
								<span id="number-of-votes-${objectId}">${ratingDto.numberOfVotes}</span>
							</fmt:param>
						</fmt:message>
					</div>
				
				</c:otherwise>
			</c:choose>
			
		</c:if>
	</c:forEach>

</div>

</div>

<%--Comments area---------------------------------------%>
<div id="comments-area">
	<c:if test="${isCommentsEnabled}">
	
		<c:set var="userId"><lams:user property="userID" /></c:set>
		<c:forEach var="comment" items="${commentsRatingDto.ratingComments}">
			<c:if test="${comment.learner.userId == userId}">
				<c:set var="commentLeftByUser" value="${comment}"/>
			</c:if>
		</c:forEach>
		
		<c:choose>
			<c:when test='${isItemAuthoredByUser}'>
				<c:forEach var="comment" items="${commentsRatingDto.ratingComments}">
					<div class="rating-comment">
						<c:out value="${comment.comment}" escapeXml="false" />
					</div>
				</c:forEach>
			</c:when>
			
			<c:when test='${not empty commentLeftByUser}'>
				<div class="rating-comment">
					<c:out value="${commentLeftByUser.comment}" escapeXml="false" />
				</div>
			</c:when>
		</c:choose>

		<c:if test="${not disabled && empty commentLeftByUser && not isItemAuthoredByUser}">	
		
			<div id="add-comment-area">
		
				<!-- Rating limits info -->
				<c:if test="${commentsRatingDto.commentsMinWordsLimit ne 0}">
				
					<div class="info rating-info">
						<fmt:message key="${minNumberWordsLabel}">
							<fmt:param value="${commentsRatingDto.commentsMinWordsLimit}"/>
						</fmt:message>
					</div>
				</c:if>		
			
				<textarea name="comment" rows="2" id="comment-textarea" onfocus="if(this.value==this.defaultValue)this.value='';" onblur="if(this.value=='')this.value=this.defaultValue;">Please, provide some comment here...</textarea>
			
				<div class="button add-comment" id="add-comment-button">
				</div>			
			</div>
		</c:if>
				
	</c:if>
</div>

<!-- end tab content -->