<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->
	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		 $.ajaxSetup({ cache: true });
		 $("#messageArea").load(url, function() {
			 $(this).show();
			 $('#saveCancelButtons').hide();
		 });
		location.hash = "messageArea";
	}
	function hideMessage(){
		 $("#messageArea").hide();
		 $('#saveCancelButtons').show();
	}
	
	function removeQuestion(questionIndex){
		document.QaAuthoringForm.questionIndex.value=questionIndex;
        submitMethod('removeQuestion');
	}

	function submitMessage(){
		if ( typeof CKEDITOR !== 'undefined' ) {
			for ( instance in CKEDITOR.instances )
				CKEDITOR.instances[instance].updateElement();
		}

		var formData = new FormData(document.getElementById("newQuestionForm"));
		
	    $.ajax({ // create an AJAX call...
			data: formData, 
	        processData: false, // tell jQuery not to process the data
	        contentType: false, // tell jQuery not to set contentType
           	type: $("#newQuestionForm").attr('method'),
			url: $("#newQuestionForm").attr('action'),
			success: function(data) {
				$('#resourceListArea').html($(data).find('#itemList'));
				hideMessage();
			}
	    });
	}
</script>

<html:hidden property="questionIndex" />

<div class="form-group">
    <label for="title"><fmt:message key="label.authoring.title.col"/></label>
    <html:text property="title" styleClass="form-control"></html:text>
</div>
<div class="form-group">
    <label for="instructions"><fmt:message key="label.authoring.instructions.col" /></label>
    <lams:CKEditor id="instructions" value="${formBean.instructions}"
    			   contentFolderID="${formBean.contentFolderID}">
    </lams:CKEditor>
</div>


<div id="resourceListArea">
	<%@ include file="/authoring/itemlist.jsp"%>
</div>

<div class="form-inline">
	<a href="javascript:showMessage('<html:rewrite page="/authoring.do?dispatch=newQuestionBox&contentFolderID=${formBean.contentFolderID}&httpSessionID=${formBean.httpSessionID}&toolContentID=${formBean.toolContentID}&usernameVisible=${formBean.usernameVisible}&showOtherAnswers=${formBean.showOtherAnswers}&lockWhenFinished=${formBean.lockWhenFinished}&questionsSequenced=${formBean.questionsSequenced}"/>');"
		id="addTopic" class="btn btn-default btn-sm"><i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.add.new.question" /> 
	</a>
</div>

<div id="messageArea" class="voffset10"></div>