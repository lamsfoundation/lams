<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.comments.CommentConstants"%>

<script type="text/javascript">
	// The treetable code uses the clicks to expand and collapse the replies but then 
	// the buttons will not work. So stop the event propogating up the event chain. 
	$("#editForm").click(function (e) {
    	e.stopPropagation();
	});
	$("#editForm").keydown(function (e) {
    	e.stopPropagation();
	});
	$(".button").click(function (e) {
    	e.stopPropagation();
	});
	$(".comment").click(function (e) {
		e.stopPropagation();
	});

	// The treetable code uses the clicks to expand and collapse the replies but then 
	// the buttons will not work. So stop the event propogating up the event chain. 
	$('#editForm').submit(function() { // catch the form's submit event
		editCommentSubmit()
	} );
		 	
	function editCommentSubmit() {

		var btnName = "editCommentSubmitButton";
		if ( isDisabled(btnName) ) {
			return false;
		}

		disableButton(btnName);
		
		var theForm = $(editForm);			

		if ( validateBodyText($('#editFormBody').val(), <%=CommentConstants.MAX_BODY_LENGTH%>, "<fmt:message key="label.comment.body.validation" />") ) {
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
					// make sure the old edit form is gone, so the user won't try to submit it again
					$('#edit').remove();
				}
				reloadThread(response,'<lams:LAMSURL />','<fmt:message key="error.cannot.redisplay.please.refresh"/>','<fmt:message key="error.please.refresh"/>');
    		});
		} // end validateBodyText
		else {
			enableButton(btnName);
		}
		return false;
	};

	function cancelEdit() {
		$('#edit').remove();
	}
	
</script>

<div class="comment-entry form-group voffset5">
<form id="editForm" method="GET" action="<lams:LAMSURL />comments/updateTopicInline.do">
	<textarea class="form-control"  id="editFormBody" maxlength="<%=CommentConstants.MAX_BODY_LENGTH+2%>" name="body" class="comment">${comment.comment.body}</textarea>
	<input type="hidden" id="sessionMapID" name="sessionMapID" value="${sessionMapID}"/>
	<input type="hidden" id="commentUid" name="commentUid" value="${commentUid}"/>
	
	<div class="row voffset5">
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

 	<c:choose>
 	<c:when test="${sessionMap.anonymous}">
 	<%-- Post Anonymously? --%>
	<div class="col-12 col-md-6">
		<c:set var="anonymousCheckboxChecked" value="${comment.comment.anonymous}"/>
		<c:set var="anonymousCheckboxName" value="commentAnonymousEdit"/>
		<%@include file="anonymouscheckbox.jsp" %>
 	</div>

	<%-- Cancel / Edit Buttons --%>
	<div class="col-12 col-md-6">
	</c:when>
	<c:otherwise>
	<div class="col-12">
	</c:otherwise>
	</c:choose>
	<a href="#nogo" onclick="javascript:editCommentSubmit();" class="btn btn-xs btn-primary pull-right" id="editCommentSubmitButton">
		<fmt:message key="label.post" />
	</a>&nbsp;
	<a href="#nogo" onclick="javascript:cancelEdit();" class="btn btn-xs btn-primary pull-right roffset5">
		<fmt:message key="label.cancel" />
	</a>
	</div>

</form>
</div>



