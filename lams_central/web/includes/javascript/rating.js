
//Please, set up LAMS_URL, COUNT_RATED_ITEMS, COMMENTS_MIN_WORDS_LIMIT, MAX_RATES and MIN_RATES, MAX_RATINGS_FOR_ITEM,
//COMMENT_TEXTAREA_TIP_LABEL, WARN_COMMENTS_IS_BLANK_LABEL, WARN_MIN_NUMBER_WORDS_LABEL, SESSION_ID constants in parent document

//constant indicating there is rating limits set up
var HAS_RATING_LIMITS;

// Which ones are being rated by the current user? Needed to control what is disabled when the max number of ratings is reached
// when there is a "set" of criteria for each user e.g. one or more criteria and/or a comment.
// Only needed when MAX_RATES > 0
var idsBeingRated = []; 	

$(document).ready(function(){
	HAS_RATING_LIMITS = MAX_RATES!=0 || MIN_RATES!=0;

	initializeStarability();
	
	//check minimum rates limit initially
	if (MIN_RATES != 0) {
		checkMinimumRatesLimit(COUNT_RATED_ITEMS);
	}
});

//initialize starability and post comment button. Note: we need the quotes around undefined for the typeof !
function initializeStarability() {
	const maxRatingsForItem = (typeof MAX_RATINGS_FOR_ITEM === "undefined" || MAX_RATINGS_FOR_ITEM === undefined) ? "" : MAX_RATINGS_FOR_ITEM;
	const ratingLimitsByCriteria = (typeof LIMIT_BY_CRITERIA === "undefined" || LIMIT_BY_CRITERIA === undefined) ? false : LIMIT_BY_CRITERIA;
	
	// if SESSION_ID is not defined do not allow them to update ratings as the servlet will fail.
	// But in monitoring we will do initializeStarability to display the ratings properly so then SESSION_ID is undefined.
	const SERVLET_PATH = (typeof SESSION_ID === "undefined" || SESSION_ID === undefined) ? "" :
		LAMS_URL + "servlet/rateItem?hasRatingLimits=" + HAS_RATING_LIMITS + "&ratingLimitsByCriteria=" + ratingLimitsByCriteria
		+ "&maxRatingsForItem=" + maxRatingsForItem + "&toolSessionId=" + SESSION_ID; 

	$(".starability-new").each(function() {
		const id = $(this).data('id'); // get the id of the box 

		if (!$(this).hasClass('starability-disabled')) {
			$("input[type=radio][name=" + id + "]").change(function() {
				let element = this;
				const rate = $(this).val();

				$.post(
					SERVLET_PATH,
					{
						idBox: id,
						rate: rate
					},
					function(data) {
						if (!data.error) {
							$("#user-rating-" + id).html(data.userRating);
							$("#average-rating-" + id).html(data.averageRating);
							$("#number-of-votes-" + id).html(data.numberOfVotes);
							$("#starability-caption-" + id).css("visibility", "visible");
							var parts = id.split('-');
							$("#comment-tick-" + parts[1]).css("visibility", "visible");
							$("#add-comment-area-" + parts[1]).css("visibility", "visible");

							//handle rating limits if available
							handleRatingLimits(data.countRatedItems, id);

						} else {
							handleError(element, rate);
						}
					},
					'json'
				);
			});
		}
	});

	// LDEV-4495 Add an already rated on to stash of already rated ids. Then they won't be disabled when the user has 
	// already rated as many as they can but they return to the screen to reeerate
    if (HAS_RATING_LIMITS && MAX_RATES != 0) {
		$(".starability-new").each(function() {
			var average = $(this).data("average"); // unrated have average 0 to start
			if ( average > 0 ) {
				var newItemId = getItemIdFromObjectId($(this).data("id"));
				if ( idsBeingRated.indexOf(newItemId) == -1 ) 
					idsBeingRated.push(newItemId)
			}
		});
    }
		
	$(".starability-new").removeClass("starability-new");
	
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
    				
    				if (refreshOnSubmit) {
	    				// LDEV-5052 Refresh page and scroll to the given ID on comment submit
 
    					// setting href does not navigate if url contains #, we still need a reload
    					location.hash = '#' + refreshOnSubmit;
    					location.reload();
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

function createStarability(isDisplayOnly, objectId, averageRating, numberOfVotes, userRating, isWidgetDisabled, criteriaTitle) { 
	let isCriteriaRatedByUser = userRating != "",
		result = '',	
		legend = '';
		
	if (criteriaTitle) {
		legend = '<legend class="text-muted fw-bold">' +
					criteriaTitle +
				 '</legend>';
	}

	let dataRating;
	if (isDisplayOnly || isCriteriaRatedByUser) {
		dataRating = Math.floor(averageRating);
		//half-round when widget is disabled
		if (isWidgetDisabled && (averageRating % 1 >= 0.5)) {
			dataRating += 0.5;
		}

	} else {
		dataRating = 0;
	}

	if (isWidgetDisabled) {
		result += 
			legend +
			'<div class="starability starability-result" data-rating="' + dataRating + '">' +
				'Rated: ' + dataRating + ' stars' +
			'</div>';

	} else {
		result +=
			'<fieldset class="starability starability-grow starability-new" data-average="' + dataRating + '" data-id="' + objectId + '">' +
				legend +

				'<input type="radio" id="' + objectId + '-0" class="input-no-rate" name="' + objectId + '" value="0" aria-label="No rating."' +
					(dataRating == 0 ? 'checked' : '') + '/>' +
	
				'<input type="radio" id="' + objectId + '-1" name="' + objectId + '" value="1"' +
					(dataRating == 1 ? 'checked' : '') + '/>' +
				'<label for="' + objectId + '-1" title="Terrible">1 star</label>' +
	
				'<input type="radio" id="' + objectId + '-2" name="' + objectId + '" value="2"' +
					(dataRating == 2 ? 'checked' : '') + '/>' +
				'<label for="' + objectId + '-2" title="Not good">2 stars</label>' +
	
				'<input type="radio" id="' + objectId + '-3" name="' + objectId + '" value="3"' +
					(dataRating == 3 ? 'checked' : '') + '/>' +
				'<label for="' + objectId + '-3" title="Average">3 stars</label>' +
	
				'<input type="radio" id="' + objectId + '-4" name="' + objectId + '" value="4"' +
				(	dataRating == 4 ? 'checked' : '') + '/>' +
				'<label for="' + objectId + '-4" title="Very good">4 stars</label>' +
	
				'<input type="radio" id="' + objectId + '-5" name="' + objectId + '" value="5"' +
					(dataRating == 5 ? 'checked' : '') + '/>' +
				'<label for="' + objectId + '-5" title="Amazing">5 stars</label>' +
	
				'<span class="starability-focus-ring"></span>' +
			'</fieldset>';
	}

	if (isDisplayOnly) {
		result += '<div class="starability-caption">' +
			AVG_RATING_LABEL.replace("@1@", averageRating).replace("@2@", numberOfVotes) +
			'</div>';

	} else {
		result += '<div class="starability-caption" id="starability-caption-' + objectId + '"';
		if (!isCriteriaRatedByUser) {
			result += ' style="visibility: hidden;"';
		}
		result += '>';
		var temp = YOUR_RATING_LABEL.replace("@1@", '<span id="user-rating-' + objectId + '">' + userRating + '</span>');
		temp = temp.replace("@2@", '<span id="average-rating-' + objectId + '">' + averageRating + '</span>');
		temp = temp.replace("@3@", '<span id="number-of-votes-' + objectId + '">' + numberOfVotes + '</span>');
		result += temp;
		result += '</div>';
	}
	
	return result;
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
		    	$(".starability").each(function() {
		    		if (!$(this).hasClass('starability-result') && !$(this).hasClass('starability-disabled') ) {
						const dataId = $(this).data('id'),
		    				  itemId = getItemIdFromObjectId(dataId);
		    			if ( idsBeingRated.indexOf(itemId) == -1 ) {
			        		$(this).addClass('starability-disabled');
			        		$('input[type=radio][name=' + dataId +']').attr('disabled', 'disabled');//disable all internal radio buttons
			        		
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
