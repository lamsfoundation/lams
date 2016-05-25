<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	
	$(document).ready(function() {
		$("#useSelectLeaderToolOuput").click(function() {
			if (document.QaAuthoringForm.useSelectLeaderToolOuput.checked) {
				//uncheck checkboxes
				document.QaAuthoringForm.showOtherAnswers.checked = false;
				//disable checkboxes
				document.QaAuthoringForm.showOtherAnswers.disabled = true;
				
				$("#show-other-answers-options").hide('slow');
			} else {
				//enable checkboxes
				document.QaAuthoringForm.showOtherAnswers.disabled = false;
			}	
		});
		
		$("#showOtherAnswers").click(function() {
			$("#show-other-answers-options").toggle('slow');
			if ($(this).is(':checked')) {
				$("#usernameVisible").prop('checked', true);
			}
		});
		
		//initial state
		if (${formBean.showOtherAnswers == 0}) {
			$("#show-other-answers-options").hide();
		}
		
	    $('#allow-rate-answers').click(function() {
	    	$(".rating-criteria-tag").toggle("slow");
	    });
	    
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

<div class="checkbox">
	<label for="useSelectLeaderToolOuput">
		<html:checkbox property="useSelectLeaderToolOuput" styleId="useSelectLeaderToolOuput" value="1" />
		<fmt:message key="label.use.select.leader.tool.output" />
	</label>
</div>

<div class="checkbox">
	<label for="showOtherAnswers">
		<html:checkbox property="showOtherAnswers" styleId="showOtherAnswers" value="1" disabled="${formBean.useSelectLeaderToolOuput}"/>
		<fmt:message key="label.learner.answer" />
	</label>
</div>



<div class="loffset20" id="show-other-answers-options">
	<div class="checkbox">
		<label for="usernameVisible">
			<html:checkbox property="usernameVisible" styleId="usernameVisible" value="1" />
			<fmt:message key="label.show.names" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="allowRateAnswers">
			<html:checkbox property="allowRateAnswers" styleId="allowRateAnswers" value="1" />
			<fmt:message key="label.authoring.allow.rate.answers" />
		</label>
	</div>

	<div class="loffset20">
		<lams:AuthoringRatingCriteria criterias="${sessionMap.ratingCriterias}" hasRatingLimits="true"
			upLabel="label.authoring.up" downLabel="label.authoring.down" />
	</div>
</div>

<div class="checkbox">
	<label for="notifyTeachersOnResponseSubmit">
		<html:checkbox property="notifyTeachersOnResponseSubmit" styleId="notifyTeachersOnResponseSubmit" value="1" />
		<fmt:message key="label.notify.teachers.on.response.submit" />
	</label>
</div>

<div class="checkbox">
	<label for="reflect">
		<html:checkbox property="reflect" styleId="reflect" value="1" />
		<fmt:message key="label.reflect" />
	</label>
</div>

<div class="form-group">
	<html:textarea property="reflectionSubject" styleId="reflectionSubject" styleClass="form-control" cols="30" rows="3"	/>
</div>

<div class="checkbox">
	<label for="questionsSequenced">
		<html:checkbox property="questionsSequenced" styleId="questionsSequenced" value="1" />
		<fmt:message key="radiobox.questionsSequenced" />
	</label>
</div>

<div class="checkbox">
	<label for="noReeditAllowed">
		<html:checkbox property="noReeditAllowed" styleId="noReeditAllowed" value="1" />
		<fmt:message key="label.no.reedit.allowed" />
	</label>
</div>

<div class="checkbox">
	<label for="lockWhenFinished">
		<html:checkbox property="lockWhenFinished" styleId="lockWhenFinished" value="1" disabled="${formBean.noReeditAllowed == 1}"/>
		<fmt:message key="label.lockWhenFinished" />
	</label>
</div>

<div class="checkbox">
	<label for="allowRichEditor">
		<html:checkbox property="allowRichEditor" styleId="allowRichEditor" value="1" />
		<fmt:message key="label.allowRichEditor" />
	</label>
</div>