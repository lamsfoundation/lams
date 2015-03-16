<%@ include file="/common/taglibs.jsp"%>

<%-- If you change this file, remember to update the copy made for CNG-28 --%>

<script type="text/javascript">
	// The treetable code uses the clicks to expand and collapse the replies but then 
	// the buttons will not work. So stop the event propogating up the event chain. 
	$("#replyForm").click(function (e) {
		e.stopPropagation();
	});

	$('#replyForm').submit(function() { // catch the form's submit event

		if ( validateForm() ) {

			if ( typeof CKEDITOR !== 'undefined' ) {
				for ( instance in CKEDITOR.instances )
    				CKEDITOR.instances[instance].updateElement();
    		}
    		
			var formData = new FormData(this);
			
		    $.ajax({ // create an AJAX call...
		        data: formData, 
		        processData: false, // tell jQuery not to process the data
		        contentType: false, // tell jQuery not to set contentType
		        type: $(this).attr('method'), // GET or POST
		        url: $(this).attr('action'), // the file to call
		        success: function (response) {
        			var threadDiv = document.getElementById('thread'+response.threadUid);
        			var threadUid = response.threadUid;
        			var messageUid = response.messageUid;
        			var rootUid = response.rootUid;
					
        			if ( rootUid ) {
	        			if ( ! threadDiv) {
    	    				// must have replied to the top level, so show the posting at the top.
        					// even if it was an actual error, the top is as good a place as any to show it!
							var threadDiv = document.getElementById('reply');
							threadDiv.id = 'thread'+messageUid;
        				} else {
        					// make sure the old reply form is gone, so if something goes wrong 
        					// the user won't try to submit it again
							$('#reply').remove();
						}
        			
	        			if ( ! threadDiv) {
// I18N
    	    				alert("Your reply is saved but we cannot redisplay it. Please select refresh to reload the forum messages.");
        				} else {
	        				var loadString = '<html:rewrite page="/learning/viewTopicThread.do?topicID="/>' + rootUid + "&sessionMapID=" + response.sessionMapID + "&threadUid=" + threadUid+"&messageUid="+messageUid;
							$(threadDiv).load(loadString, function() {
								// expand up to the reply - in case it is buried down in a lot of replies
								// don't need to do this if we have started a new thread.
								if ( threadUid != messageUid ) {
									$('#tree' + threadUid).treetable("reveal",messageUid);
									$('#msg'+messageUid).focus();
								}
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
		    });
		} // end validateForm()
		return false;
	});

	function cancelReply() {
		$('.replydiv').remove();
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
