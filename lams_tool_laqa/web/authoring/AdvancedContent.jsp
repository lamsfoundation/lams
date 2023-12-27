<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />

<script type="text/javascript">
	$(document).ready(function() {
		$("#showOtherAnswers").click(function() {
			$("#show-other-answers-options").toggle('slow');
			if ($(this).is(':checked')) {
				$("#usernameVisible").prop('checked', true);
			}
		});
		
		 $('#allowRateAnswers').click(function() {
		    	$("#criterias-holder").toggle("slow");
		    });
		 
		//initial state
		
		if (${!authoringForm.qa.showOtherAnswers}) {
			$("#show-other-answers-options").hide();
		}
		
	    $('#no-reedit-allowed').click(function() {
	    	if ($(this).is(':checked')) {
	    		$("#lockWhenFinished").prop('checked', true);
	    	}
	        $("#lockWhenFinished").prop("disabled", $(this).is(':checked'));
	    });
	});
</script>

<lams:SimplePanel titleKey="label.select.leader">
	<div class="checkbox">
		<label for="useSelectLeaderToolOuput">
			<form:checkbox path="qa.useSelectLeaderToolOuput" id="useSelectLeaderToolOuput"/>
			<fmt:message key="label.use.select.leader.tool.output" />
		</label>
	</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.question.options">

	<div class="checkbox">
		<label for="questionsSequenced">
			<form:checkbox path="qa.questionsSequenced" id="questionsSequenced"/>
			<fmt:message key="radiobox.questionsSequenced" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="allowRichEditor">
			<form:checkbox path="qa.allowRichEditor" id="allowRichEditor"/>
			<fmt:message key="label.allowRichEditor" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="noReeditAllowed">
			<form:checkbox path="qa.noReeditAllowed" id="noReeditAllowed"/>
			<fmt:message key="label.no.reedit.allowed" />
		</label>
	</div>

	<div class="checkbox">
		<label for="showOtherAnswers">
			<form:checkbox path="qa.showOtherAnswers" id="showOtherAnswers" />
			<fmt:message key="label.learner.answer" />
		</label>
	</div>

	<div class="loffset20" id="show-other-answers-options">
		<div class="checkbox">
			<label for="usernameVisible">
				<form:checkbox path="qa.usernameVisible" id="usernameVisible"/>
				<fmt:message key="label.show.names" />
			</label>
		</div>
		
		<div class="checkbox">
			<label for="allowRateAnswers">
				<form:checkbox path="qa.allowRateAnswers" id="allowRateAnswers"/>
				<fmt:message key="label.authoring.allow.rate.answers" />
			</label>
		</div>
	
		<div id="criterias-holder" <c:if test="${!authoringForm.qa.allowRateAnswers}"> style="display:none;"</c:if>>
			<lams:AuthoringRatingCriteria criterias="${sessionMap.ratingCriterias}" hasRatingLimits="true"
				upLabel="label.authoring.up" downLabel="label.authoring.down" 
				formContentPrefix="qa"/>
		</div>	
	</div>
	
</lams:SimplePanel>
	
<lams:SimplePanel titleKey="label.notifications">
	<div class="checkbox">
		<label for="notifyTeachersOnResponseSubmit">
			<form:checkbox path="qa.notifyTeachersOnResponseSubmit" id="notifyTeachersOnResponseSubmit"/>
			<fmt:message key="label.notify.teachers.on.response.submit" />
		</label>
	</div>
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${authoringForm.toolContentID}" />

<lams:SimplePanel titleKey="label.activity.completion">
	<div class="checkbox">
		<label for="lockWhenFinished">
			<form:checkbox path="qa.lockWhenFinished" id="lockWhenFinished" disabled="${authoringForm.qa.noReeditAllowed}"/>
			<fmt:message key="label.lockWhenFinished" />
		</label>
	</div>
</lams:SimplePanel>
