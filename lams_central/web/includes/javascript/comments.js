	function highlightMessage() {
		$('.highlight').filter($('table')).css('background','none');
		$('.highlight').filter($('div')).switchClass('highlight', '', 6000);
		$('.highlight').removeClass('highlight');
	}

	function validateBodyText(bodyText, maxLength, validationMessage) {
 		bodyText = bodyText.trim();
 		if ( bodyText.length==0 || bodyText.length>maxLength ) {
 			alert(validationMessage);
 			return false;
 		} else { 
			return true;
		}
	}

	
	function reloadThread(response, lamsUrl, savedCannotRedisplayMessage, errorCannotRedisplayMessage) {
	    var threadDiv = document.getElementById('thread'+response.threadUid);
       	var threadUid = response.threadUid;
       	var commentUid = response.commentUid;
					
       	if ( commentUid ) {
        	if ( ! threadDiv) {
        		alert(savedCannotRedisplayMessage);
       		} else {
    			var loadString = lamsUrl+"comments/viewTopicThread.do?sessionMapID=" + response.sessionMapID + "&threadUid=" + threadUid+"&commentUid="+commentUid;
    			$.ajaxSetup({ cache: true });
				$(threadDiv).load(loadString, function() {
					// expand up to the reply - in case it is buried down in a lot of replies
					// don't need to do this if we have started a new thread.
					if ( threadUid != commentUid ) {
						$('#tree' + threadUid).treetable("reveal",commentUid);
						$('#msg'+commentUid).focus();
					}
					highlightMessage();
				});
			}
    	} else if ( response.errMessage ) {
	    	// No valid id? Something failed. Assume it is a response message coming back.
			alert(response.errMessage);
		} else {
    		alert(errorCannotRedisplayMessage);
    	} 
	}

	/** Disable the submit button when pressed to stop double submission. disabled = true doesn't stop the link working it just
	 * greys it out. So we need to test with isDisabled() in the submit methods too! Messy. */
	function disableButton(buttonId) {
		$("#"+buttonId).attr("disabled", true); 
	}
	function isDisabled(buttonId) {
		return $("#"+buttonId).attr("disabled");
	}
	function enableButton(buttonId) {
		$("#"+buttonId).removeAttr("disabled");
	}
	
