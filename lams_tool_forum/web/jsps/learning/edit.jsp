<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript" src="<lams:WebAppURL />includes/javascript/message.js"></script>

<script type="text/javascript">
	// The treetable code uses the clicks to expand and collapse the replies but then 
	// the buttons will not work. So stop the event propogating up the event chain. 
	$("#editForm").click(function (e) {
    	e.stopPropagation();
	});
	$("#editForm").keydown(function (e) {
    	e.stopPropagation();
	});
	
	$('#editForm').submit(function() { // catch the form's submit event
		disableSubmitButton();
		if ( validateForm() ) {

			if ( typeof CKEDITOR !== 'undefined' ) {
				for ( instance in CKEDITOR.instances )
    				CKEDITOR.instances[instance].updateElement();
    		}
    		
			showBusy("itemAttachmentArea");
			var formData = new FormData(this);
			
		    $.ajax({ // create an AJAX call...
		        data: formData, 
		        processData: false, // tell jQuery not to process the data
		        contentType: false, // tell jQuery not to set contentType
		        type: $(this).attr('method'), // GET or POST
		        url: $(this).attr('action'), // the file to call
		        success: function (response) {
        			var messageUid = response.messageUid;
					if ( messageUid ) {
	        			var rootUid = response.rootUid;
	        			var messDiv = document.getElementById('outermsg'+messageUid);
	        			if ( ! messDiv) {
	        				alert('<fmt:message key="error.cannot.redisplay.please.refresh"/>');
        				} else {
        					// make sure the old edit form is gone, so if something goes wrong 
        					// the user won't try to submit it again
							$('#edit').remove();
	        				var loadString = '<html:rewrite page="/learning/viewMessage.do?topicID="/>' + rootUid + "&sessionMapID=" + response.sessionMapID + "&messageUid="+messageUid;
	        				$.ajaxSetup({ cache: true });
							$(messDiv).load(loadString, function() {
								$('#pb-msg'+messageUid).focus();
								setupJRating("<c:url value='/learning/rateMessage.do'/>?toolSessionID=${sessionMap.toolSessionID}&sessionMapID=${sessionMapID}");
								highlightMessage();
							});
						}

		    		} else {
		    			// No valid id? Validation failed! Assume it is the form coming back.
						$('#edit').html(response);
		    		} 
					hideBusy("itemAttachmentArea");
		    	} 
		    });
		} // end validateForm()
		else {
			enableSubmitButton();
		}
		return false;
	});

	function cancelEdit() {
		$('#edit').remove();
	}
	
</script>

<html:form action="/learning/updateTopicInline.do"
		focus="message.subject" enctype="multipart/form-data" styleId="editForm">
		
	<html:hidden property="sessionMapID"/>	
	<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	<c:set var="sessionMapID" value="${formBean.sessionMapID}"/>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

	<div class="container-fluid">
	<div class="panel panel-default">
		<div class="panel-heading panel-title">
			<fmt:message key="title.message.edit" />
		</div>
		<div class="panel-body">
			<html:errors property="error" />
			<%@ include file="/jsps/learning/message/topiceditform.jsp"%>
		</div>
	</div>
	</div>

</html:form>




