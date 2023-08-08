<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.comments.CommentConstants"%>

<script type="text/javascript">
	// The treetable code uses the clicks to expand and collapse the replies but then 
	// the buttons will not work. So stop the event propogating up the event chain. 
	$("#replyForm").click(function(e) {
		e.stopPropagation();
	});
	$("#replyForm").keydown(function(e) {
		e.stopPropagation();
	});
	$(".button").click(function(e) {
		e.stopPropagation();
	});
	$(".comment").click(function(e) {
		e.stopPropagation();
	});

	$('#replyForm').submit(function() { // catch the form's submit event
		replyFormSubmit();
	});

	function replyFormSubmit() {
		var btnName = "replyCommentSubmitButton";
		if ( isDisabled(btnName) ) {
			return false;
		}

		disableButton(btnName);
		
		var theForm = $(replyForm);
		if (validateBodyText($('#replyFormBody').val(), <%=CommentConstants.MAX_BODY_LENGTH%>,
				"<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.comment.body.validation" /></spring:escapeBody>")) {

			$.ajax({ // create an AJAX call...
				data : theForm.serialize(),
				processData : false, // tell jQuery not to process the data
				contentType : false, // tell jQuery not to set contentType
				type : theForm.attr('method'), // GET or POST
				url : theForm.attr('action'), // the file to call  
			})
			.done(function(response) {
				var commentUid = response.commentUid;
				if (commentUid) {
					// make sure the old reply form is gone, so if something goes wrong 
					// the user won't try to submit it again
					$('#reply').remove();
				}
				
				reloadThread(
					response,
					'<lams:LAMSURL />',
					'<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.cannot.redisplay.please.refresh"/></spring:escapeBody>',
					'<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.please.refresh"/></spring:escapeBody>'
				);
			});
		} // end validateBodyText
		else {
			enableButton(btnName);
		}
		return false;
	}

	function cancelReply() {
		$('#reply').remove();
	}
</script>

<div class="comment-entry form-group voffset5">
	<form id="replyForm" method="GET"
		action="<lams:LAMSURL />comments/replyTopicInline.do">
		<textarea class="form-control" id="replyFormBody"
			maxlength="<%=CommentConstants.MAX_BODY_LENGTH + 2%>" name="body"
			class="comment"></textarea>
		<input type="hidden" id="sessionMapID" name="sessionMapID"
			value="${sessionMapID}" /> <input type="hidden" id="parentUid"
			name="parentUid" value="${parentUid}" />

		<div class="row voffset5">
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

		<c:choose>
	 	<c:when test="${sessionMap.anonymous}">
	 	<%-- Post Anonymously? --%>
		<div class="col-xs-12 col-sm-6">
			<c:set var="anonymousCheckboxChecked" value=""/>
			<c:set var="anonymousCheckboxName" value="commentAnonymousReply"/>
			<%@include file="anonymouscheckbox.jsp" %>
	 	</div>
	
		<%-- Cancel / Edit Buttons --%>
		<div class="col-xs-12 col-sm-6">
		</c:when>
		<c:otherwise>
		<div class="col-xs-12">
		</c:otherwise>
		</c:choose>
		<a href="#nogo" onclick="javascript:replyFormSubmit();"
			class="btn btn-xs btn-primary pull-right" id="replyCommentSubmitButton"> <fmt:message
				key="label.post" />
		</a>&nbsp; <a href="#nogo" onclick="javascript:cancelReply();"
			class="btn btn-xs btn-primary pull-right roffset5"> <fmt:message
				key="label.cancel" />
		</a>
		</div>
	
	</form>
</div>
