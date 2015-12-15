	function resizeIframe() {
	//	parent.resizeCommentFrame(document.body.scrollHeight); Don't use at present - breaks the scrolling
	}						

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

	function reloadThread(response) {
	    var threadDiv = document.getElementById('thread'+response.threadUid);
       	var threadUid = response.threadUid;
       	var commentUid = response.commentUid;
					
       	if ( commentUid ) {
        	if ( ! threadDiv) {
        		alert('<fmt:message key="error.cannot.redisplay.please.refresh"/>');
       		} else {
    			var loadString = "viewTopicThread.do?&sessionMapID=" + response.sessionMapID + "&threadUid=" + threadUid+"&commentUid="+commentUid;
				$(threadDiv).load(loadString, function() {
					$('#msg'+commentUid).focus();
					highlightMessage();
				});
				resizeIframe();
			}
    	} else if ( response.errMessage ) {
	    	// No valid id? Something failed. Assume it is a response message coming back.
			alert(response.errMessage);
		} else {
    		alert('<fmt:message key="error.please.refresh"/>');
    	} 
	}
