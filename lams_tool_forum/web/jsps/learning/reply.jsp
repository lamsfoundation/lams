<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.jRating.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="<lams:WebAppURL />includes/javascript/message.js"></script>

<script type="text/javascript">
	// The treetable code uses the clicks to expand and collapse the replies but then 
	// the buttons will not work. So stop the event propogating up the event chain. 
	$("#replyForm").click(function (e) {
		e.stopPropagation();
	});
	$("#replyForm").keydown(function (e) {
    	e.stopPropagation();
	});
	

	function submitReply(){

		disableSubmitButton();
		if ( validateForm() ) {

			if ( typeof CKEDITOR !== 'undefined' ) {
				for ( instance in CKEDITOR.instances )
    				CKEDITOR.instances[instance].updateElement();
    		}
			
			var replyForm = $("#replyForm");
 			if(typeof FormData == "undefined"){

 				if ( $("#attachmentFile").val() ) {
					alert("Your browser is missing the required FormData component. Files cannot be uploaded.");
 				}

				$.ajax({ // create an AJAX call...
			        data: replyForm.serialize(), 
			        type: replyForm.attr('method'), // GET or POST
			        url: replyForm.attr('action'), // the file to call
			        success: function(response) {
			        	submitSuccess(response);
			        }
			    });

			} else {

			    $.ajax({ // create an AJAX call...
			        data:  new FormData(replyForm[0]),
			        processData: false,
			   		contentType: false,
			        type: replyForm.attr('method'), // GET or POST
			        url: replyForm.attr('action'), // the file to call
			        success: function(response) {
			        	submitSuccess(response);
			        }
			    });
			}
			
		} // end validateForm()
		else {
			enableSubmitButton();
		}
		return false;
	}

	function submitSuccess(response) {
		var threadDiv = document.getElementById('thread'+response.threadUid);
		var threadUid = response.threadUid;
		var messageUid = response.messageUid;
		var rootUid = response.rootUid;
		
		if ( rootUid ) {
			if ( ! threadDiv) {
				// must have replied to the top level, so show the posting at the top.
				var threadDiv = document.getElementById('reply');
				threadDiv.id = 'thread'+messageUid;
			} else {
				// make sure the old reply form is gone, so if something goes wrong 
				// the user won't try to submit it again
				$('#reply').remove();
			}
		
			if ( ! threadDiv) {
				alert('<fmt:message key="error.cannot.redisplay.please.refresh"/>');
			} else {
				var loadString = '<html:rewrite page="/learning/viewTopicThread.do?topicID="/>' + rootUid + "&sessionMapID=" + response.sessionMapID + "&threadUid=" + threadUid+"&messageUid="+messageUid;
				$(threadDiv).load(loadString, function() {
					// expand up to the reply - in case it is buried down in a lot of replies
					// don't need to do this if we have started a new thread.
					if ( threadUid != messageUid ) {
						$('#tree' + threadUid).treetable("reveal",messageUid);
						$('#msg'+messageUid).focus();
					}
					setupJRating("<c:url value='/learning/rateMessage.do'/>?toolSessionID=${sessionMap.toolSessionID}&sessionMapID=${sessionMapID}");
					highlightMessage();
				});
			}

     		if ( response.noMorePosts ) {
				$('.replybutton').hide();
			}
		
		} else {
			// No new id? Validation failed! 
			$('#reply').html(response);
		} 
	}
	
	function cancelReply() {
		$('#reply').remove();
	}
	
</script>

<html:form action="/learning/replyTopicInline.do"
		focus="message.subject" enctype="multipart/form-data" styleId="replyForm" >
		
	<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	<c:set var="sessionMapID" value="${formBean.sessionMapID}"/>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
	<c:set var="originalMessage" value="${sessionMap.originalMessage}"/>

	<html:hidden property="sessionMapID"/>
	
	<div id="content">
	
		<h2>
			<fmt:message key="title.message.reply" />
		</h2>
		
		<html:errors property="error" />
				
		<%@ include file="/jsps/learning/message/topicreplyform.jsp"%>
	
	</div>
</html:form>
