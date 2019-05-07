<%@ include file="/common/taglibs.jsp"%>

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
		document.forms.authoringForm.questionIndex.value=questionIndex;
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
				$('#itemArea').html($(data).find('#itemList'));
				hideMessage();
				refreshThickbox();
			}
	    });
	}
</script>

<form:hidden path="questionIndex" />

<div class="form-group">
    <label for="title"><fmt:message key="label.authoring.title.col"/></label>
    <form:input path="title" cssClass="form-control"/>
</div>
<div class="form-group">
    <label for="instructions"><fmt:message key="label.authoring.instructions.col" /></label>
    <lams:CKEditor id="instructions" value="${authoringForm.instructions}"
    			   contentFolderID="${authoringForm.contentFolderID}">
    </lams:CKEditor>
</div>


<div id="itemArea">
	<%@ include file="/authoring/itemlist.jsp"%>
</div>

<div class="form-inline">
	<a href="javascript:showMessage('<lams:WebAppURL/>authoring/newQuestionBox.do?contentFolderID=${authoringForm.contentFolderID}&httpSessionID=${authoringForm.httpSessionID}&toolContentID=${authoringForm.toolContentID}&usernameVisible=${authoringForm.usernameVisible}&showOtherAnswers=${authoringForm.showOtherAnswers}&lockWhenFinished=${authoringForm.lockWhenFinished}&questionsSequenced=${authoringForm.questionsSequenced}');"
		id="addTopic" class="btn btn-default btn-sm"><i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.add.new.question" /> 
	</a>
</div>

<div id="messageArea" class="voffset10"></div>