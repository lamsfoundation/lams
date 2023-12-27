<%@ include file="/common/taglibs.jsp"%>

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.survey.options">
	
	<div class="checkbox">
		<label for="showOnePage">
		<form:checkbox path="survey.showOnePage" id="showOnePage"/>
			<fmt:message key="label.authoring.advance.show.on.one.page" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="showOtherUsersAnswers">
		<form:checkbox path="survey.showOtherUsersAnswers" id="showOtherUsersAnswers"/>
			<fmt:message key="label.show.answers.from.other.users" />
		</label>
	</div>

</lams:SimplePanel>

<lams:SimplePanel titleKey="label.notifications">

	<div class="checkbox">
		<label for="notifyTeachersOnAnswerSumbit">
		<form:checkbox path="survey.notifyTeachersOnAnswerSumbit" id="notifyTeachersOnAnswerSumbit"/>
			<fmt:message key="label.authoring.advanced.notify.onanswersubmit" />
		</label>
	</div>

</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${authoringForm.survey.contentId}" />

<lams:SimplePanel titleKey="label.activity.completion">
	<div class="checkbox">
		<label for="lockWhenFinished">
		<form:checkbox path="survey.lockWhenFinished" id="lockWhenFinished"/>
			<fmt:message key="label.authoring.advance.lock.on.finished" />
		</label>
	</div>
</lams:SimplePanel>

