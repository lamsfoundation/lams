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
<%@ attribute name="countRatedImages" required="false" rtexprvalue="true" %>
<%@ attribute name="yourRatingLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="averageRatingLabel" required="false" rtexprvalue="true" %>

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
<c:if test="${empty countRatedImages}">
	<c:set var="countRatedImages" value="0" scope="request"/>
</c:if>
<c:if test="${empty yourRatingLabel}">
	<c:set var="yourRatingLabel" value="label.your.rating" scope="request"/>
</c:if>
<c:if test="${empty averageRatingLabel}">
	<c:set var="averageRatingLabel" value="label.average.rating" scope="request"/>
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
	$(document).ready(function(){
		setupJRating();
		
		if (${minRates} != 0) {
			checkMinimumRatesLimit(${countRatedImages});
		}

    	//addNewComment button handler
	    $('#add-comment-button').click(function() {
	    	
	    	//replace special characters with HTML tags
	    	var tempTextarea = jQuery('<textarea/>');
	    	filterData(document.getElementById('comment-textarea'), tempTextarea);
			var comment = tempTextarea.value;
	    	
	    	//add new comment
	    	$.ajax({
	    		type: "POST",
	    		url: '${lams}servlet/rateItem',
	    		data: {
	    			idBox: '${commentsRatingDto.ratingCriteria.ratingCriteriaId}-${commentsRatingDto.itemId}', 
	    			comment: comment
	    		},
	    		success: function(data, itemId) {
	    				
	    			//add comment to HTML
	    			jQuery('<div/>', {
	    				'class': "rating-comment",
	    			    html: data.comment
	    			}).appendTo('#comments-area');
	    				
	    			//hide comments textarea and button
	    			$("#add-comment-area").hide();
	    				
	    		}
	    	});
	    });
		
	});
	
	function setupJRating() {
		var hasRatingLimists = ${maxRates!=0 || minRates!=0};
		
		$(".rating-stars-new").filter($(".rating-stars")).jRating({
		    phpPath : "${lams}servlet/rateItem?hasRatingLimists=" + hasRatingLimists,
		    rateMax : 5,
		    decimalLength : 1,
			onSuccess : function(data, itemId){
				
				$("#user-rating-" + itemId).html(data.userRating);
			    $("#average-rating-" + itemId).html(data.averageRating);
			    $("#number-of-votes-" + itemId).html(data.numberOfVotes);
			    $("#rating-stars-caption-" + itemId).css("visibility", "visible");
			    
			    if (hasRatingLimists) {

			    	//update info box
			    	$("#count-rated-items").html(data.countRatedItems);
			    	
				    //callback function
				    if (typeof onRatingSuccessCallback === "function") { 
				        // safe to use the function
				    	onRatingSuccessCallback(data.countRatedItems);
				    }
				    
				    //handle max rates limit
				    if (${maxRates} != 0) {
				    	
				    	//disable rating feature in case maxRates limit reached
				    	if (data.countRatedItems >= ${maxRates}) {
					    	$(".rating-stars").each(function() {
					    		$(this).unbind().css('cursor','default').addClass('jDisabled');
					    	});			    		
				    	}
				    }
				    
				    //handle min rates limit
				    if (${minRates} != 0) {
				    	checkMinimumRatesLimit(data.countRatedItems)
				    }
			    }

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
				<c:when test='${disabled || isItemAuthoredByUser || (maxRates > 0) && (countRatedImages >= maxRates) || !isCriteriaNotRatedByUser}'>
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
				
			<div class="rating-stars-caption" id="rating-stars-caption-${objectId}"
				<c:if test="${isCriteriaNotRatedByUser}">style="visibility: hidden;"</c:if>
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
			
			<c:if test="${isItemAuthoredByUser}">
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
			</c:if>
			
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
				<textarea name="comment" rows="2" id="comment-textarea" onfocus="if(this.value==this.defaultValue)this.value='';" onblur="if(this.value=='')this.value=this.defaultValue;">Please, provide some comment here...</textarea>
			
				<div class="button add-comment" id="add-comment-button">
				</div>			
			</div>
		</c:if>
				
	</c:if>
</div>

<!-- end tab content -->