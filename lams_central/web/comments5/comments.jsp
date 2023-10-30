<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="loading_animation" value="${lams}images/ajax-loader.gif"/>
<c:set var="loading_words"><fmt:message key="msg.loading" /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<%@ page import="org.lamsfoundation.lams.comments.CommentConstants"%>

<!-- ********************  CSS ********************** -->
<lams:css suffix="treetable" />

<!-- ********************  javascript ********************** -->
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.jscroll.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.treetable.js"></script>
<lams:JSImport src="includes/javascript/comments.js" />
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		scrollDoneCallback();

		$('#sortMenu').change(function() {
			var url = "<lams:LAMSURL />comments/viewTopic.do?newUI=true&sessionMapID=${sessionMapID}&sticky=true&sortBy="
					+ $(this).find("option:selected").attr('value');
			reloadDivs(url);
		});

		$('#newForm').submit(function() { // catch the form's submit event
			newCommentSubmit();
		});
	});

	function refreshComments() {
		var reqIDVar = new Date();
		reloadDivs('<lams:LAMSURL />comments/viewTopic.do?newUI=true&sessionMapID=${sessionMapID}&sticky=true&reqUid='
				+ reqIDVar.getTime());
	}

	function reloadDivs(url) {
		$('.scroll').data('jscroll', null);
		$.ajaxSetup({
			cache : true
		});
		$('#commentDiv').load(url);
	}

	function scrollDoneCallback() {
	}

	var btnName = "newCommentSubmitButton";

	function newCommentSubmit() {
		if ( isDisabled(btnName) ) {
			return false;
		}
		
		var theForm = $(newForm);
		disableButton(btnName);

		if (validateBodyText(
			$('#newFormBody').val(),
			<%=CommentConstants.MAX_BODY_LENGTH%>,
			"<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.comment.body.validation" /></spring:escapeBody>"
		)) {
			$.ajax({ // create an AJAX call...
				data : theForm.serialize(),
				processData : false, // tell jQuery not to process the data
				contentType : false, // tell jQuery not to set contentType
				type : theForm.attr('method'), // GET or POST
				url : theForm.attr('action'), // the file to call  
				
			}).done(function(response) {
								var threadUid = response.threadUid;
								var commentUid = response.commentUid;
								var sessionMapID = response.sessionMapID;

								// have just added it as a standalone comment, so show the posting at the top. 
								var newCommentDiv = document.getElementById('newcomments');
								var newThreadDiv = document.createElement("div");
								newThreadDiv.id = 'thread' + commentUid;
								newCommentDiv.insertBefore(newThreadDiv, newCommentDiv.firstChild);

								if (!newCommentDiv || !newThreadDiv) {
									alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.cannot.redisplay.please.refresh"/></spring:escapeBody>');
								} else if (commentUid) {
									var loadString = '<lams:LAMSURL />comments/viewTopicThread.do?newUI=true&sessionMapID='
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
									alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="error.please.refresh"/></spring:escapeBody>');
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

<div class="mt-4">
	<c:if test='${not sessionMap.readOnly}'>
		<div class="comments mb-2">
			<div class="comment-entry">
				<form id="newForm" method="GET" action="<lams:LAMSURL />comments/newComment.do">
					<div class="mb-2 float-end">
						<select id="sortMenu" name="sortMenu" class="form-control float-end w-auto">
							<option value='0'
								<c:if test='${sessionMap.sortBy == 0}'>selected</c:if>>
								<fmt:message key="label.newest.first" />
							</option>
							<option value='1'
								<c:if test='${sessionMap.sortBy == 1}'>selected</c:if>>
								<fmt:message key="label.top.comments" />
							</option>
						</select>
					</div>
		
					<textarea class="form-control" placeholder="..." id="newFormBody"
						maxlength="<%=CommentConstants.MAX_BODY_LENGTH + 2%>" name="body" class="comment" aria-label="comment"
					></textarea>
		
					<input type="hidden" id="sessionMapID" name="sessionMapID" value="${sessionMapID}" />
					<input type="hidden" name="newUI" value="true" />
		
					<div class="clearfix my-2">
						<c:if test="${sessionMap.anonymous}">
							<div class="float-start">
								<c:set var="anonymousCheckboxChecked" value="" />
								<c:set var="anonymousCheckboxName" value="commentAnonymousNew" />
								<%@include file="anonymouscheckbox.jsp"%>
							</div>
						</c:if>
			
						<button type="button" onclick="javascript:newCommentSubmit();"
							class="btn btn-secondary btn-sm float-end" id="newCommentSubmitButton"
						>
							<fmt:message key="label.post" />
							<i class="fa-regular fa-paper-plane ms-1"></i>
						</button>
						<button type="button" onclick="javascript:clearNewForm();"
							class="btn btn-secondary btn-sm float-end me-2"
						>
							<i class="fa fa-cancel me-1"></i>
							<fmt:message key="label.cancel" />
						</button>
					</div>
					
					<div class="text-center">
						<button type="button" onclick="javascript:refreshComments();" class="btn btn-secondary btn-sm btn-icon-refresh"> 
							<fmt:message key="label.refresh"/>
						</button>
					</div>
				</form>
			</div>
		</div>
	</c:if>

	<div id="commentDiv" class="mt-1">
		<%@ include file="allview.jsp"%>
	</div>
</div>