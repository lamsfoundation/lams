<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<%@ page import="org.lamsfoundation.lams.comments.CommentConstants"%>

<script type="text/javascript">
	// The treetable code uses the clicks to expand and collapse the replies but then 
	// the buttons will not work. So stop the event propogating up the event chain. 
	$("#replyForm").click(function (e) {
		e.stopPropagation();
	});
	$("#replyForm").keydown(function (e) {
    	e.stopPropagation();
	});
	$(".button").click(function (e) {
    	e.stopPropagation();
	});
	$(".comment").click(function (e) {
		e.stopPropagation();
	});
	
	$('#replyForm').submit(function() { // catch the form's submit event
		replyFormSubmit();
	});
	
	function replyFormSubmit() {
		var theForm = $(replyForm);
		if ( validateBodyText($('#replyFormBody').val(), <%=CommentConstants.MAX_BODY_LENGTH%>, "<fmt:message key="label.comment.body.validation" />") ) {

		    $.ajax({ // create an AJAX call...
	    	    data: theForm.serialize(), 
	        	processData: false, // tell jQuery not to process the data
	        	contentType: false, // tell jQuery not to set contentType
	        	type: theForm.attr('method'), // GET or POST
	        	url: theForm.attr('action'), // the file to call  
	    	})
			.done(function (response) {
		
        		var commentUid = response.commentUid;
        		if ( commentUid ) {
				// make sure the old reply form is gone, so if something goes wrong 
        		// the user won't try to submit it again
				$('#reply').remove();
				}
			reloadThread(response);
			});
		} // end validateBodyText
	return false;
	}
	
	function cancelReply() {
		$('#reply').remove();
	}

</script>

<div class="comment-entry">
<form id="replyForm" method="GET" action="<html:rewrite page="/comments/replyTopicInline.do"/>">
	<textarea rows="3" cols="80" id="replyFormBody" maxlength="<%=CommentConstants.MAX_BODY_LENGTH+2%>" name="body" class="comment"></textarea>
	<input type="hidden" id="sessionMapID" name="sessionMapID" value="${sessionMapID}"/>
	<input type="hidden" id="parentUid" name="parentUid" value="${parentUid}"/>

	<div class="space-bottom">
	<div class="right-buttons">
		<a href="#" onclick="javascript:cancelReply();" class="button">
			<fmt:message key="label.cancel" />
		</a>&nbsp;
		<a href="#" onclick="javascript:replyFormSubmit();" class="button">
			<fmt:message key="label.post" />
		</a>
	</div>
	</div>
</div>
</form>
