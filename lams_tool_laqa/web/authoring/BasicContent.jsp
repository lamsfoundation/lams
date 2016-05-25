<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->
	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
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
    <lams:CKEditor id="instructions" value="${qaGeneralAuthoringDTO.activityInstructions}"
    			   contentFolderID="${qaGeneralAuthoringDTO.contentFolderID}">
    </lams:CKEditor>
</div>


<div id="resourceListArea">
	<%@ include file="/authoring/itemlist.jsp"%>
</div>

<div class="form-inline">
	<a href="javascript:showMessage('<html:rewrite page="/authoring.do?dispatch=newQuestionBox&contentFolderID=${qaGeneralAuthoringDTO.contentFolderID}&httpSessionID=${qaGeneralAuthoringDTO.httpSessionID}&toolContentID=${qaGeneralAuthoringDTO.toolContentID}&usernameVisible=${qaGeneralAuthoringDTO.usernameVisible}&showOtherAnswers=${qaGeneralAuthoringDTO.showOtherAnswers}&lockWhenFinished=${qaGeneralAuthoringDTO.lockWhenFinished}&questionsSequenced=${qaGeneralAuthoringDTO.questionsSequenced}"/>');"
		id="addTopic" class="btn btn-default btn-sm"><i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.add.new.question" /> 
	</a>
</div>

<div id="messageArea" class="voffset10"></div>