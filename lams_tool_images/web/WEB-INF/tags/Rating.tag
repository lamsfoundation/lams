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
<%@ attribute name="maxRates" required="false" rtexprvalue="true" %>
<%@ attribute name="minRates" required="false" rtexprvalue="true" %>
<%@ attribute name="numberVotesLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="countRatedImages" required="false" rtexprvalue="true" %>

<%-- Default value for message key --%>
<c:if test="${empty disabled}">
	<c:set var="disabled" value="false" scope="request"/>
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
<c:if test="${empty numberVotesLabel}">
	<c:set var="numberVotesLabel" value="label.number.of.votes" scope="request"/>
</c:if>

<c:choose>
	<c:when test='${disabled || maxRates > 0 && countRatedImages >= maxRates}'>
		<c:set var="ratingStarsClass" value="rating-stars-disabled"/>
	</c:when>
	<c:otherwise>
		<c:set var="ratingStarsClass" value="rating-stars"/>
	</c:otherwise>
</c:choose>

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
		
		if (${minRates} != 0) {
			checkMinimumRatesLimit(${countRatedImages});
		}
	});
	
	function setupJRating() {
		var isCountRatedItemsRequested = ${maxRates!=0 || minRates!=0};
		
		$(".rating-stars-new").filter($(".rating-stars")).jRating({
		    phpPath : "${lams}servlet/rateItem?isCountRatedItemsRequested=" + isCountRatedItemsRequested,
		    rateMax : 5,
		    decimalLength : 1,
			canRateAgain : true,
			nbRates : 1000,
			onSuccess : function(data, itemId){
				
			    $("#average-rating-" + itemId).html(data.averageRating);
			    $("#number-of-votes-" + itemId).html(data.numberOfVotes);
			    
			    if (isCountRatedItemsRequested) {

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
			//$('#yourID').css('display') == 'none'

		$( "#learner-submit" ).toggle( countRatedItems >= ${minRates} );

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