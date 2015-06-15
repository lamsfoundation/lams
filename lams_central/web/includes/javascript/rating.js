
//Please, set up LAMS_URL, COUNT_RATED_ITEMS, COMMENTS_MIN_WORDS_LIMIT, MAX_RATES and MIN_RATES, 
//COMMENT_TEXTAREA_TIP_LABEL, WARN_COMMENTS_IS_BLANK_LABEL, WARN_MIN_NUMBER_WORDS_LABEL constants in parent document

//constant indicating there is rting limits set up
var HAS_RATING_LIMITS;

$(document).ready(function(){
	HAS_RATING_LIMITS = MAX_RATES!=0 || MIN_RATES!=0;
	
	initializeJRating();
	
	//check minimum rates limit initially
	if (MIN_RATES != 0) {
		checkMinimumRatesLimit(COUNT_RATED_ITEMS);
	}
});

//initialize jRating and post comment button
function initializeJRating() {
		
	$(".rating-stars-new").filter($(".rating-stars")).jRating({
		phpPath : LAMS_URL + "servlet/rateItem?hasRatingLimits=" + HAS_RATING_LIMITS,
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
    			hasRatingLimits: HAS_RATING_LIMITS
    		},
    		success: function(data, textStatus) {
    				
    			//add comment to HTML
    			jQuery('<div/>', {
    				'class': "rating-comment",
    			    html: data.comment
    			}).appendTo('#comments-area-' + itemId);
    				
    			//hide comments textarea and button
    			$("#add-comment-area-" + itemId).hide();
    			
    			//handle rating limits if available
    			if (HAS_RATING_LIMITS) {
    				handleRatingLimits(data.countRatedItems);
    			}
    				
    		}
    	});
    	//apply only once
    }).removeClass("add-comment-new");
}

function handleRatingLimits(countRatedItems) {
		
    if (HAS_RATING_LIMITS) {

    	//update info box
    	$("#count-rated-items").html(countRatedItems);
	    	
	    //callback function
	    if (typeof onRatingSuccessCallback === "function") { 
	        // safe to use the function
	    	onRatingSuccessCallback(countRatedItems);
	    }
		    
	    //handle max rates limit
	    if (MAX_RATES != 0) {
		    	
	    	//disable rating feature in case MAX_RATES limit reached
	    	if (countRatedItems >= MAX_RATES) {
		    	$(".rating-stars").each(function() {
		    		$(this).unbind().css('cursor','default').addClass('jDisabled');
		    	});			    		
	    	}
	    }
		    
	    //handle min rates limit
	    if (MIN_RATES != 0) {
	    	checkMinimumRatesLimit(countRatedItems)
	    }
    }
}
	
function checkMinimumRatesLimit(countRatedItems) {
	$( "#learner-submit" ).toggle( countRatedItems >= MIN_RATES );
}