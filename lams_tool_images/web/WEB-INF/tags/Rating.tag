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

<%@ attribute name="ratingDtos" required="false" rtexprvalue="true" type="java.util.Collection" %>

<%-- Optional attribute --%>
<%@ attribute name="disabled" required="false" rtexprvalue="true" %>
<%@ attribute name="numberVotesLabel" required="false" rtexprvalue="true" %>

<%-- Default value for message key --%>
<c:choose>
	<c:when test='${empty disabled || disabled}'>
		<c:set var="ratingStarsClass" value="rating-stars-disabled"/>
	</c:when>
	<c:otherwise>
		<c:set var="ratingStarsClass" value="rating-stars"/>
	</c:otherwise>
</c:choose>
<c:if test="${empty disabled}">
	<c:set var="disabled" value="false" scope="request"/>
</c:if>
<c:if test="${empty numberVotesLabel}">
	<c:set var="numberVotesLabel" value="label.number.of.votes" scope="request"/>
</c:if>

<link type="text/css" href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
<script type="text/javascript">
	//var for jquery.jRating.js
	var pathToImageFolder = "${lams}images/css/";
</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>

<!-- begin tab content -->
<script type="text/javascript">
	$(document).ready(function(){
		setupJRating();
	});
	
	function setupJRating() {
		$(".rating-stars-new").filter($(".rating-stars")).jRating({
		    phpPath : "${lams}servlet/rateItem",
		    rateMax : 5,
		    decimalLength : 1,
			canRateAgain : true,
			nbRates : 1000,
			onSuccess : function(data, itemId){
				
			    $("#average-rating-" + itemId).html(data.averageRating);
			    $("#number-of-votes-" + itemId).html(data.numberOfVotes);
			    
			    //callback function
			    if (typeof onRatingSuccessCallback === "function") { 
			        // safe to use the function
			    	onRatingSuccessCallback(data.numOfRatings);
			    }
			    
			    //disable rating feature in case maxRate limit reached
			    //if (data.noMoreRatings) {
			    	//$(".rating-stars").each(function() {
			    		//$(this).jRating('readOnly');
			    	//});
			    //}
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
</script>

<div class="rating-stars-holder">
	<c:forEach var="ratingDto" items="${ratingDtos}" varStatus="status">
		<c:set var="objectId" value="${ratingDto.ratingCriteria.ratingCriteriaId}-${ratingDto.itemId}"/>
		
		<h4>
			${ratingDto.ratingCriteria.title}
		</h4>
	
		<div class="${ratingStarsClass} rating-stars-new" data-average="${ratingDto.averageRating}" data-id="${objectId}">
		</div>
			
		<div class="rating-stars-caption">
			<fmt:message key="${numberVotesLabel}" >
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
	
	</c:forEach>

</div>
<!-- end tab content -->