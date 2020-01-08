<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.comments.CommentConstants"%>
<script type="text/javascript">
	$(document).ready(function() {
		$('#newForm').submit(function() { // catch the form's submit event
			newCommentSubmit();
		});
	});

	var btnName = "newCommentSubmitButton";

	function newCommentSubmit() {

		if ( isDisabled(btnName) ) {
			return false;
		}
		
		var theForm = $(newForm);
		disableButton(btnName);

		if (validateBodyText($('#newFormBody').val(),
<%=CommentConstants.MAX_BODY_LENGTH%>,"<fmt:message key="label.comment.body.validation" />")) {

			$.ajax({ // create an AJAX call...
						data : theForm.serialize(),
						processData : false, // tell jQuery not to process the data
						contentType : false, // tell jQuery not to set contentType
						type : theForm.attr('method'), // GET or POST
						url : theForm.attr('action'), // the file to call  
					})
					.done(
							function(response) {

								var threadUid = response.threadUid;
								var commentUid = response.commentUid;
								var sessionMapID = response.sessionMapID;

								// have just added it as a standalone comment, so show the posting at the top. 
								var newCommentDiv = document
										.getElementById('newcomments');
								var newThreadDiv = document
										.createElement("div");
								newThreadDiv.id = 'thread' + commentUid;
								newCommentDiv.insertBefore(newThreadDiv,
										newCommentDiv.firstChild);

								if (!newCommentDiv || !newThreadDiv) {
									alert('<fmt:message key="error.cannot.redisplay.please.refresh"/>');
								} else if (commentUid) {
									var loadString = '<lams:LAMSURL />comments/viewTopicThread.do?sessionMapID='
											+ response.sessionMapID
											+ "&threadUid="
											+ threadUid
											+ "&commentUid=" + commentUid;
									$.ajaxSetup({ cache: true });
									$(newThreadDiv).load(loadString,
											function() {
												highlightMessage();
											});
									clearNewForm();
								} else if (response.errMessage) {
									alert(response.errMessage);
									enableButton(btnName);
								} else {
									alert('<fmt:message key="error.please.refresh"/>');
								}

							});
		} // end validateBodyText
		else {
			enableButton(btnName);
		}
		return false;
	}

	function clearNewForm() {
		$('#newFormBody').val('');
		$('#commentAnonymousNew').prop('checked',false);
		enableButton(btnName);
		return false;
	}
</script>

<%-- <div class="row no-gutter">
	<div class="col-xs-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<fmt:message key="heading.comments" />
				</h4>
			</div>
			<div class="panel-body">

 --%>				<div class="form-group">
					<div class="comment-entry">
						<form id="newForm" method="GET"	action="<lams:LAMSURL />comments/newComment.do">
							<a href="javascript:refreshComments();" style="margin: 5px" class="btn btn-primary btn-xs"> <fmt:message key="label.refresh" /> </a>

							<textarea class="form-control" placeholder="..." id="newFormBody"
								maxlength="<%=CommentConstants.MAX_BODY_LENGTH + 2%>" name="body"
								class="comment"></textarea>

							<input type="hidden" id="sessionMapID" name="sessionMapID" value="${sessionMapID}" />  

							<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
							<c:if test="${sessionMap.anonymous}">
							<div class="inline-block pull-left">
								<c:set var="anonymousCheckboxChecked" value=""/>
								<c:set var="anonymousCheckboxName" value="commentAnonymousNew"/>
								<%@include file="anonymouscheckbox.jsp" %>
							</div>
							</c:if>

							<span onclick="javascript:newCommentSubmit();"
								style="margin: 5px" class="btn btn-primary btn-xs pull-right" id="newCommentSubmitButton">
								<fmt:message key="label.post" /></span>
							<a href="#nogo" onclick="javascript:clearNewForm();"
								style="margin: 5px" class="btn btn-primary btn-xs pull-right">
								<fmt:message key="label.cancel" />
							</a>&nbsp;
						</form>
					</div>
				</div>
<!-- 
			</div>
			end panel body
		</div>
		end comment panel
	</div>
	end col-*-12
</div>
end row no-gutter
 -->

