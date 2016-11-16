
//Please, set up LAMS_URL, COUNT_RATED_ITEMS, COMMENTS_MIN_WORDS_LIMIT, MAX_RATES and MIN_RATES, MAX_RATINGS_FOR_ITEM,
//COMMENT_TEXTAREA_TIP_LABEL, WARN_COMMENTS_IS_BLANK_LABEL, WARN_MIN_NUMBER_WORDS_LABEL constants in parent document

//constant indicating there is rting limits set up
var HAS_RATING_LIMITS;

// Which ones are being rated by the current user? Needed to control what is disabled when the max number of ratings is reached
// when there is a "set" of criteria for each user e.g. one or more criteria and/or a comment.
// Only needed when MAX_RATES > 0
var idsBeingRated = []; 	

$(document).ready(function(){
	HAS_RATING_LIMITS = MAX_RATES!=0 || MIN_RATES!=0;

	initializeJRating();
	
	//check minimum rates limit initially
	if (MIN_RATES != 0) {
		checkMinimumRatesLimit(COUNT_RATED_ITEMS);
	}
	
});

//initialize jRating and post comment button. Note: we need the quotes around undefined for the typeof !
function initializeJRating() {

	var maxRatingsForItem;
	if ( typeof MAX_RATINGS_FOR_ITEM === "undefined" || MAX_RATINGS_FOR_ITEM === undefined )
		maxRatingsForItem = "";
	else 
		maxRatingsForItem = MAX_RATINGS_FOR_ITEM;
	
	var ratingLimitsByCriteria;
	if ( typeof LIMIT_BY_CRITERIA === "undefined" || LIMIT_BY_CRITERIA === undefined )
		ratingLimitsByCriteria = false;
	else 
		ratingLimitsByCriteria = LIMIT_BY_CRITERIA;

	$(".rating-stars-new").filter($(".rating-stars")).jRating({
		phpPath : LAMS_URL + "servlet/rateItem?hasRatingLimits=" + HAS_RATING_LIMITS + "&ratingLimitsByCriteria=" + ratingLimitsByCriteria + "&maxRatingsForItem=" + maxRatingsForItem,
		rateMax : 5,
		decimalLength : 1,
		onSuccess : function(data, itemId){
			$("#user-rating-" + itemId).html(data.userRating);
			$("#average-rating-" + itemId).html(data.averageRating);
			$("#number-of-votes-" + itemId).html(data.numberOfVotes);
			$("#rating-stars-caption-" + itemId).css("visibility", "visible");
			var parts = itemId.split('-');
			$("#comment-tick-" + parts[1]).css("visibility", "visible");	
			$("#add-comment-area-" + parts[1]).css("visibility", "visible");
			
			//handle rating limits if available
			handleRatingLimits(data.countRatedItems, itemId);
		},
		onError : function(){
 			handleError();
		}
	});
		
	$(".rating-stars-new").filter($(".rating-stars-disabled")).jRating({
		rateMax : 5,
		isDisabled : true
	});
		
	$(".rating-stars-new").removeClass("rating-stars-new");
	
	//addNewComment button handler
	$(".add-comment-new").click(function() {
    	var itemId = $(this).data("item-id");
    	var commentsCriteriaId = $(this).data("comment-criteria-id");
    	
    	//replace special characters with HTML tags
    	var tempTextarea = jQuery('<textarea/>');
    	filterData(document.getElementById('comment-textarea-' + itemId), tempTextarea);
		var comment = tempTextarea.value;

		//comment can't be blank
		if (comment == "" || comment == COMMENT_TEXTAREA_TIP_LABEL) {
			alert(WARN_COMMENTS_IS_BLANK_LABEL);
			return false;
		}
    	
		//word count limit
		if (COMMENTS_MIN_WORDS_LIMIT != 0) { //removed isCommentsEnabled && --was it worth it?
			var value =  $("#comment-textarea-" + itemId).val();
			value = value.trim();
			
		    var wordCount = value ? (value.replace(/['";:,.?\-!]+/g, '').match(/\S+/g) || []).length : 0;
		    
		    if(wordCount < COMMENTS_MIN_WORDS_LIMIT){
				alert( WARN_MIN_NUMBER_WORDS_LABEL.replace("{1}", wordCount));
				return false;
			}
		}
    	
    	//add new comment
    	$.ajax({
    		type: "POST",
    		url: LAMS_URL + 'servlet/rateItem',
    		data: {
    			idBox: commentsCriteriaId + '-' + itemId, 
    			comment: comment,
    			hasRatingLimits: HAS_RATING_LIMITS,
    			ratingLimitsByCriteria: ratingLimitsByCriteria,
    			maxRatingsForItem: maxRatingsForItem
    		},
    		success: function(data, textStatus) {
    			if ( data.error ) {
    				handleError();
    			} else {
	   				//add comment to HTML
   					jQuery('<div/>', {
   						'class': "rating-comment",
   						html: data.comment
   					}).appendTo('#comments-area-' + itemId);
   					
 	 				//hide comments textarea and button
	   				$("#add-comment-area-" + itemId).hide();
   			
	   				//handle rating limits if available
	  				if (HAS_RATING_LIMITS) {
	   					handleRatingLimits(data.countRatedItems, itemId);
	   				}
	   			}
    		},
			onError : function(){
 				handleError();
 			}
    	});
    	//apply only once
    }).removeClass("add-comment-new");
}

// If the call is from a rating then it is ratingCriteriaId-itemId format string, otherwise a comment 
// only returns itemId as a number. Pull a string version of itemId of this. 
function getItemIdFromObjectId(objectId) {
	var str = String(objectId);
	var i = str.indexOf('-');
	if ( i >= 0 ) 
		return str.substr(i+1);
	else 
		return str;
}

function handleRatingLimits(countRatedItems, objectId) {
		
    if (HAS_RATING_LIMITS) {

    	//update info box
    	$("#count-rated-items").html(countRatedItems);

	    //callback function
 	    if (typeof onRatingSuccessCallback === "function") { 
	        // safe to use the function
 	    	onRatingSuccessCallback(countRatedItems, objectId);
	    }
		    
    	//handle max rates limit
	    if (MAX_RATES != 0) {
		 
			// add the latest itemId to idsBeingRated *before* doing the disabling or this one will be disabled
			var newItemId = getItemIdFromObjectId(objectId);
			if ( idsBeingRated.indexOf(newItemId) == -1 ) 
 		    	idsBeingRated.push(newItemId)
		    	
	    	//disable rating features in case MAX_RATES limit reached *except for the ones user is already rating*
	    	if (countRatedItems >= MAX_RATES) {
		    	$(".rating-stars").each(function() {
		    		if ( ! $(this).hasClass('jDisabled') ) {
		    			var itemId = getItemIdFromObjectId($(this).attr('data-id'));
		    			if ( idsBeingRated.indexOf(itemId) == -1 ) {
			        		$(this).unbind().css('cursor','default').addClass('jDisabled');
			        		$('#add-comment-area-' + itemId).children().remove();
			        	}
			        }
		    	});	
	    	}
	    }
			    
	    //handle min rates limit
	    if (MIN_RATES != 0) {
	    	checkMinimumRatesLimit(countRatedItems);
	    }
	    
    }
}
	
function checkMinimumRatesLimit(countRatedItems) {
	$( "#learner-submit" ).toggle( countRatedItems >= MIN_RATES );
}

function handleError() {
    //callback function
    if (typeof onRatingErrorCallback === "function") { 
    	onRatingErrorCallback();
    } else {
    	alert("Error saving rating. Please retry.");
	}		
}
