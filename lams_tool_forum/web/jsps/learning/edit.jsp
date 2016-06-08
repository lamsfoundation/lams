<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.jRating.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery-ui.js"></script>
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
	
	function submitEdit(){

		disableSubmitButton();
		if ( validateForm() ) {

			if ( typeof CKEDITOR !== 'undefined' ) {
				for ( instance in CKEDITOR.instances )
    				CKEDITOR.instances[instance].updateElement();
    		}
    		
			var replyForm = $("#editForm");
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

	function cancelEdit() {
		$('#edit').remove();
	}

	function submitSuccess(response) {
		var messageUid = response.messageUid;
		if ( messageUid ) {
			var rootUid = response.rootUid;
			var messDiv = document.getElementById('msg'+messageUid);
			if ( ! messDiv) {
				alert('<fmt:message key="error.cannot.redisplay.please.refresh"/>');
			} else {
				// make sure the old edit form is gone, so if something goes wrong 
				// the user won't try to submit it again
				$('#edit').remove();
				var loadString = '<html:rewrite page="/learning/viewMessage.do?topicID="/>' + rootUid + "&sessionMapID=" + response.sessionMapID + "&messageUid="+messageUid;
				$(messDiv).load(loadString, function() {
					$('#msg'+messageUid).focus();
					setupJRating("<c:url value='/learning/rateMessage.do'/>?toolSessionID=${sessionMap.toolSessionID}&sessionMapID=${sessionMapID}");
					highlightMessage();
				});
			}

		} else {
			// No valid id? Validation failed! Assume it is the form coming back.
			$('#edit').html(response);
		} 
	} 
</script>

<html:form action="/learning/updateTopicInline.do"
		focus="message.subject" enctype="multipart/form-data" styleId="editForm">
		
	<html:hidden property="sessionMapID"/>	
	<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	<c:set var="sessionMapID" value="${formBean.sessionMapID}"/>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

	<div id="content">

		<h1>
			<c:out value="${sessionMap.title}" escapeXml="true" />
		</h1>

		<h2>
			<fmt:message key="title.message.edit" />
		</h2>

		<html:errors property="error" />

		<%@ include file="/jsps/learning/message/topiceditform.jsp"%>

	</div>

</html:form>




