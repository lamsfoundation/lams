<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[authoringForm.httpSessionID]}" />

<script type="text/javascript">
	
	$(document).ready(function() {
		
		$("#useSelectLeaderToolOuput").click(function() {
			if (document.forms.authoringForm.useSelectLeaderToolOuput.checked) {
				//uncheck checkboxes
				document.forms.authoringForm.showOtherAnswers.checked = false;
				//disable checkboxes
				document.forms.authoringForm.showOtherAnswers.disabled = true;
				
				$("#show-other-answers-options").hide('slow');
			} else {
				//enable checkboxes
				document.forms.authoringForm.showOtherAnswers.disabled = false;
			}	
		});
		
		$("#showOtherAnswers").click(function() {
			$("#show-other-answers-options").toggle('slow');
			if ($(this).is(':checked')) {
				$("#usernameVisible").prop('checked', true);
			}
		});
		
		 $('#allowRateAnswers').click(function() {
		    	$("#criterias-holder").toggle("slow");
		    });
		
		 $('#reflect').click(function() {
		    	$("#reflectionInstructions").toggle("slow");
		    });
		 
		//initial state
		
		if (${authoringForm.showOtherAnswers == 0}) {
			$("#show-other-answers-options").hide();
		}
		
		if (${authoringForm.reflect == 0}) {
			$("#reflectionInstructions").hide();
		}
		
	    $('#no-reedit-allowed').click(function() {
	    	if ($(this).is(':checked')) {
	    		$("#lockWhenFinished").prop('checked', true);
	    	}
	        $("#lockWhenFinished").prop("disabled", $(this).is(':checked'));
	    });
	    
	    $('#reflectionSubject').keyup(function(){
	    	$('#reflect').prop('checked', !isEmpty($(this).val()));
	    });
	});
	
	
</script>

<lams:SimplePanel titleKey="label.select.leader">
	<div class="checkbox">
		<label for="useSelectLeaderToolOuput">
			<form:checkbox path="useSelectLeaderToolOuput" id="useSelectLeaderToolOuput" value="1" />
			<fmt:message key="label.use.select.leader.tool.output" />
		</label>
	</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.question.options">

	<div class="checkbox">
		<label for="questionsSequenced">
			<form:checkbox path="questionsSequenced" id="questionsSequenced" value="1" />
			<fmt:message key="radiobox.questionsSequenced" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="allowRichEditor">
			<form:checkbox path="allowRichEditor" id="allowRichEditor" value="1" />
			<fmt:message key="label.allowRichEditor" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="noReeditAllowed">
			<form:checkbox path="noReeditAllowed" id="noReeditAllowed" value="1" />
			<fmt:message key="label.no.reedit.allowed" />
		</label>
	</div>

	<div class="checkbox">
		<label for="showOtherAnswers">
			<form:checkbox path="showOtherAnswers" id="showOtherAnswers" value="1" disabled="${authoringForm.useSelectLeaderToolOuput}"/>
			<fmt:message key="label.learner.answer" />
		</label>
	</div>

	<div class="loffset20" id="show-other-answers-options">
		<div class="checkbox">
			<label for="usernameVisible">
				<form:checkbox path="usernameVisible" id="usernameVisible" value="1" />
				<fmt:message key="label.show.names" />
			</label>
		</div>
		
		<div class="checkbox">
			<label for="allowRateAnswers">
				<form:checkbox path="allowRateAnswers" id="allowRateAnswers" value="1" />
				<fmt:message key="label.authoring.allow.rate.answers" />
			</label>
		</div>
	
		<div id="criterias-holder" <c:if test="${authoringForm.allowRateAnswers == 0}"> style="display:none;"</c:if>>
		<lams:AuthoringRatingCriteria criterias="${sessionMap.ratingCriterias}" hasRatingLimits="true"
			upLabel="label.authoring.up" downLabel="label.authoring.down" />
		</div>
		
	</div>
	
</lams:SimplePanel>
	
<lams:SimplePanel titleKey="label.notifications">

	<div class="checkbox">
		<label for="notifyTeachersOnResponseSubmit">
			<form:checkbox path="notifyTeachersOnResponseSubmit" id="notifyTeachersOnResponseSubmit" value="1" />
			<fmt:message key="label.notify.teachers.on.response.submit" />
		</label>
	</div>
	
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${authoringForm.toolContentID}" />

<lams:SimplePanel titleKey="label.activity.completion">
	<div class="checkbox">
		<label for="lockWhenFinished">
			<form:checkbox path="lockWhenFinished" id="lockWhenFinished" value="1" disabled="${authoringForm.noReeditAllowed == 1}"/>
			<fmt:message key="label.lockWhenFinished" />
		</label>
	</div>

	<div class="checkbox">
		<label for="reflect">
			<form:checkbox path="reflect" id="reflect" value="1" />
			<fmt:message key="label.reflect" />
		</label>
	</div>
	
	<div id="reflectionInstructions" class="form-group">
		<form:textarea path="reflectionSubject" id="reflectionSubject" cssClass="form-control" cols="30" rows="3"	/>
	</div>
</lams:SimplePanel>
