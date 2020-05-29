
//Please, set up LAMS_URL, COUNT_RATED_ITEMS, COMMENTS_MIN_WORDS_LIMIT, MAX_RATES and MIN_RATES, MAX_RATINGS_FOR_ITEM,
//COMMENT_TEXTAREA_TIP_LABEL, WARN_COMMENTS_IS_BLANK_LABEL, WARN_MIN_NUMBER_WORDS_LABEL, ALLOW_RERATE, RATING_STEP, SESSION_ID constants in parent document

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

	var canRateAgain; 
	if ( typeof ALLOW_RERATE === "undefined" || ALLOW_RERATE === undefined ) {
		canRateAgain = false; 
	} else {
		canRateAgain = ALLOW_RERATE;
	}
	
	var step; 
	if ( typeof RATING_STEP === "undefined" || RATING_STEP === undefined ) {
		step = false; 
	} else {
		step = RATING_STEP;
	}
	
	// if SESSION_ID is not defined do not allow them to update ratings as the servlet will fail.
	// But in monitoring we will do initializeJRating to display the ratings properly so then SESSION_ID is undefined.
	var phpPathValue;
	if ( typeof SESSION_ID === "undefined" || SESSION_ID === undefined )
		phpPathValue = "";
	else 
		phpPathValue = LAMS_URL + "servlet/rateItem?hasRatingLimits=" + HAS_RATING_LIMITS + "&ratingLimitsByCriteria=" + ratingLimitsByCriteria 
			+ "&maxRatingsForItem=" + maxRatingsForItem + "&toolSessionId=" + SESSION_ID;

	$(".rating-stars-new").filter($(".rating-stars")).jRating({
		phpPath : phpPathValue,
		rateMax : 5,
        decimalLength : 1,
        step: step,
		canRateAgain : canRateAgain,
        nbRates : canRateAgain ? 100 : 0,
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

	// LDEV-4495 Add an already rated on to stash of already rated ids. Then they won't be disabled when the user has 
	// already rated as many as they can but they return to the screen to reeerate
    if (HAS_RATING_LIMITS && MAX_RATES != 0) {
		$(".rating-stars-new").filter($(".rating-stars")).each(function() {
			var average = $(this).data("average"); // unrated have average 0 to start
			if ( average > 0 ) {
				var newItemId = getItemIdFromObjectId($(this).data("id"));
				if ( idsBeingRated.indexOf(newItemId) == -1 ) 
					idsBeingRated.push(newItemId)
			}
		});
    }

	$(".rating-stars-new").filter($(".rating-stars-disabled")).jRating({
		rateMax : 5,
		isDisabled : true
	});
		
	$(".rating-stars-new").removeClass("rating-stars-new");
	
	//addNewComment button handler
	$(".add-comment-new").click(function() {
    	var itemId = $(this).data("item-id");
    	var commentsCriteriaId = $(this).data("comment-criteria-id");
    	var showAllComments = $(this).data('show-all-comments');
    	var refreshOnSubmit = $(this).data('refresh-on-submit');
    	
		var comment = validComment("comment-textarea-" + itemId, false, false);
		if ( ! comment )
			return false;

		if ( ! ( typeof SESSION_ID === "undefined" || SESSION_ID === undefined ) ) {
			//add new comment
	    	$.ajax({
	    		type: "POST",
	    		url: LAMS_URL + 'servlet/rateItem',
	    		data: {
	    			idBox: commentsCriteriaId + '-' + itemId, 
	    			comment: comment,
	    			hasRatingLimits: HAS_RATING_LIMITS,
	    			ratingLimitsByCriteria: ratingLimitsByCriteria,
	    			maxRatingsForItem: maxRatingsForItem,
	    			showAllComments : showAllComments,
	    			toolSessionId: SESSION_ID
	    		},
	    		success: function(data, textStatus) {
	    			if (data.error) {
	    				handleError();
	    				return;
	    			}
	    			
		  			// LDEV-4480 Acknowledgement when submitting a comment for a Q&A response 
    				alert("Submitted the comment successfully.");
    				
    				if (refreshOnSubmit) {
	    				// LDEV-5052 Refresh page and scroll to the given ID on comment submit
    					var url = location.href,
    						anchorIndex = url.lastIndexOf('#');
    					if (anchorIndex > 0) {
    						url = url.substring(0, anchorIndex);
    					}
    					url += '#' + refreshOnSubmit;
    					if (url == location.href) {
    						location.reload(true);
    						return false;
    					}
    					location.href = url;
    					return false;
    				}
    				
    				
    				var commentsArea = $('#comments-area-' + itemId);
	   				
    				if (data.allComments) {
    					// add all users' comments to HTML
    					jQuery.each(data.allComments, function(){
		   					jQuery('<div/>', {
		   						'class': "rating-comment",
		   						html: this
		   					}).appendTo(commentsArea);
    					});
    				} else {
    					//add this user's comment to HTML
	   					jQuery('<div/>', {
	   						'class': "rating-comment",
	   						html: data.comment
	   					}).appendTo(commentsArea);
    				}
   					
 	 				//hide comments textarea and button
	   				$("#add-comment-area-" + itemId, commentsArea).hide();
   			
	   				//handle rating limits if available
	  				if (HAS_RATING_LIMITS) {
	   					handleRatingLimits(data.countRatedItems, itemId);
	   				}
	    		},
				onError : function(){
	 				handleError();
	 			}
	    	});
		}
    	//apply only once
    }).removeClass("add-comment-new");
}

// allowBlankComment is needed for Peer Review, where rating related comments are always checked even if minWords = 0
// skipMinWordCheckOnBlank is used for the explicit comment type fields may be blank even when minWord > 0
function validComment(textAreaId, allowBlankComment, skipMinWordCheckOnBlank) {

	
	//replace special characters with HTML tags
    var tempTextarea = jQuery('<textarea/>');
    filterData(document.getElementById(textAreaId), tempTextarea);
	var comment = tempTextarea.value;

	if ( ! allowBlankComment && ( comment == "" || comment == COMMENT_TEXTAREA_TIP_LABEL ) ) {
		alert(WARN_COMMENTS_IS_BLANK_LABEL);
		return;
	}
    	
	if ( skipMinWordCheckOnBlank && comment == "" ) {
		return comment;
	}
	
	//word count limit
	if (COMMENTS_MIN_WORDS_LIMIT != 0) { //removed isCommentsEnabled && --was it worth it?
		var value =  $("#" + textAreaId).val();
		value = value.trim();
		
	    var wordCount = value ? (value.replace(/['";:,.?\-!]+/g, '').match(/\S+/g) || []).length : 0;
		    
	    if(wordCount < COMMENTS_MIN_WORDS_LIMIT){
			alert( WARN_MIN_NUMBER_WORDS_LABEL.replace("{1}", wordCount));
			return;
		}
	}
	
	return comment;
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
