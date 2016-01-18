	function highlightMessage() {
		$('.highlight').filter($('table')).css('background','none');
		$('.highlight').filter($('div')).effect('highlight', {color: "#fcf0ad"}, 6000);
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
    			var loadString = lamsUrl+"comments/viewTopicThread.do?&sessionMapID=" + response.sessionMapID + "&threadUid=" + threadUid+"&commentUid="+commentUid;
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
