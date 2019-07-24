<%@ include file="/common/taglibs.jsp"%>
<c:set var="httpSessionID" value="${authoringForm.httpSessionID}" />
<c:set var="sessionMap" value="${sessionScope[httpSessionID]}" />

<style>
	#question-bank-div {
    	margin-top: 75px;
	}
	#add-question-div {
		margin-top: -5px;
	}
	#messageArea {
		margin-top: 60px;
	}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		//question bank div
		$('#question-bank-collapse').on('show.bs.collapse', function () {
			$('#question-bank-collapse.contains-nothing').load(
				"<lams:LAMSURL/>/searchQB/start.do",
				{
					returnUrl: "<c:url value='/authoring/importQbQuestion.do'/>?httpSessionID=${authoringForm.httpSessionID}",
					toolContentId: ${sessionMap.toolContentID}
				},
				function() {
					$(this).removeClass("contains-nothing");
				}
			);
		})
	});

	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		 $.ajaxSetup({ cache: true });
		 $("#messageArea").load(url, function() {
			 $(this).show("fast");
			 $('#saveCancelButtons, #question-bank-div, #addTopic').hide();
		 });
		location.hash = "messageArea";
	}
	function hideMessage(){
		 $("#messageArea").hide();
		 $('#saveCancelButtons, #question-bank-div, #addTopic').show();
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
    <lams:CKEditor id="instructions" value="${authoringForm.instructions}" contentFolderID="${authoringForm.contentFolderID}">
    </lams:CKEditor>
</div>

<div id="itemArea">
	<%@ include file="/authoring/itemlist.jsp"%>
</div>

<div id="add-question-div" class="form-inline form-group pull-right">
	<a href="javascript:showMessage('<lams:WebAppURL/>authoring/newQuestionBox.do?contentFolderID=${authoringForm.contentFolderID}&httpSessionID=${authoringForm.httpSessionID}&toolContentID=${sessionMap.toolContentID}&usernameVisible=${authoringForm.usernameVisible}&showOtherAnswers=${authoringForm.showOtherAnswers}&lockWhenFinished=${authoringForm.lockWhenFinished}&questionsSequenced=${authoringForm.questionsSequenced}');"
		id="addTopic" class="btn btn-default btn-sm">
		<i class="fa fa-lg fa-plus-circle"></i>&nbsp;<fmt:message key="label.add.new.question" /> 
	</a>
</div>

<!-- Question Bank -->
<div class="panel-group" id="question-bank-div" role="tablist" aria-multiselectable="true"> 
    <div class="panel panel-default" >
        <div class="panel-heading collapsable-icon-left" id="question-bank-heading">
        	<span class="panel-title">
		    	<a class="collapsed" role="button" data-toggle="collapse" href="#question-bank-collapse" aria-expanded="false" aria-controls="question-bank-collapse" >
	          		<fmt:message key="label.question.bank" />
	        	</a>
      		</span>
        </div>
	
		<div id="question-bank-collapse" class="panel-body panel-collapse collapse contains-nothing" role="tabpanel" aria-labelledby="question-bank-heading">
		</div>
	</div>
</div>

<div id="messageArea"></div>