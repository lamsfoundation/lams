<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<%@ page import="org.lamsfoundation.lams.comments.util.CommentConstants"%>

<script type="text/javascript">
	$(document).ready(function(){
		$('#newForm').submit(function() { // catch the form's submit event
			newCommentSubmit();
		});
	});

	function newCommentSubmit() {
		
		var theForm = $(newForm);
		
		if ( validateBodyText($('#newFormBody').val(), <%=CommentConstants.MAX_BODY_LENGTH%>, "<fmt:message key="label.comment.body.validation" />") ) {

	    $.ajax({ // create an AJAX call...
		        data: theForm.serialize(), 
		        processData: false, // tell jQuery not to process the data
		        contentType: false, // tell jQuery not to set contentType
		        type: theForm.attr('method'), // GET or POST
		        url: theForm.attr('action'), // the file to call  
	    })
	    .done(function (response) {
	    
       			var threadUid = response.threadUid;
       			var commentUid = response.commentUid;
       			var sessionMapID = response.sessionMapID;

       			// have just added it as a standalone comment, so show the posting at the top. 
				var newCommentDiv = document.getElementById('newcomments'); 
       			var newThreadDiv = document.createElement("div");
       			newThreadDiv.id = 'thread'+commentUid;
       			newCommentDiv.insertBefore(newThreadDiv,newCommentDiv.firstChild);
        			
       			if ( ! newCommentDiv || ! newThreadDiv ) {
       				alert('<fmt:message key="error.cannot.redisplay.please.refresh"/>');
   				} else if ( commentUid ) {
       				var loadString = '<html:rewrite page="/comments/viewTopicThread.do?sessionMapID="/>' + response.sessionMapID + "&threadUid=" + threadUid+"&commentUid="+commentUid;
					$(newThreadDiv).load(loadString, function() {
						highlightMessage();
						resizeIframe();
					});
					clearNewForm();
				} else if ( response.errMessage ) {
					alert(response.errMessage);
				} else {
       				alert('<fmt:message key="error.please.refresh"/>');
				}

	    	});
		} // end validateBodyText
		return false;
	}
	
	function clearNewForm() {
		$('#newFormBody').val('');
		return false;
	}
	
	function refreshTopic(){
		var reqIDVar = new Date();
		location.href= '<html:rewrite page="/comments/comments.do?externalType=${sessionMap.externalType}&externalSig=${sessionMap.externalSig}&externalID=${sessionMap.externalID}&sessionMapID=${sessionMapID}&reqUid=" />'+reqIDVar.getTime();
	}
	
</script>

<div class="comment-entry">
<form id="newForm" method="GET" action="<html:rewrite page="/comments/newComment.do"/>">
	<textarea rows="3" cols="80" id="newFormBody" maxlength="<%=CommentConstants.MAX_BODY_LENGTH+2%>" name="body" class="comment"></textarea>
	<input type="hidden" id="sessionMapID" name="sessionMapID" value="${sessionMapID}"/>
	
	<div class="space-bottom">
	<div class="left-buttons">
			<a href="javascript:refreshTopic();" class="button"> <fmt:message key="label.refresh" /> </a>
	</div>
	<div class="right-buttons">
		<a href="#" onclick="javascript:clearNewForm();" class="button">
			<fmt:message key="label.cancel" />
		</a>&nbsp;
		<a href="#" onclick="javascript:newCommentSubmit();" class="button">
			<fmt:message key="label.post" />
		</a>
	</div>
	</div>
</form>
</div>
