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
		<lams:Popover>
			<fmt:message key="label.show.answers.from.other.users.tip.1" /><br>
		</lams:Popover>
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
		<lams:Popover>
			<fmt:message key="label.advanced.lockOnFinished.tip.1" /><br>
		 </lams:Popover>
	</div>
	
	<div class="checkbox">
		<label for="reflectOn">
		<form:checkbox path="survey.reflectOnActivity" id="reflectOn"/>
			<fmt:message key="label.authoring.advanced.reflectOnActivity" />
		</label>
		<lams:Popover>
			<fmt:message key="label.advanced.reflectOnActivity.tip.1" /><br>
			<fmt:message key="label.advanced.reflectOnActivity.tip.2" />
		</lams:Popover>
	</div>
	
	<div class="form-group">
		<form:textarea path="survey.reflectInstructions" id="reflectInstructions" rows="3" cssClass="form-control"/>
	</div>

</lams:SimplePanel>

<script type="text/javascript">
<!--
//automatically turn on refect option if there are text input in refect instruction area
	var ra = document.getElementById("reflectInstructions");
	var rao = document.getElementById("reflectOn");
	function turnOnRefect(){
		if(isEmpty(ra.value)){
		//turn off	
			rao.checked = false;
		}else{
		//turn on
			rao.checked = true;		
		}
	}

	ra.onkeyup=turnOnRefect;
//-->
</script>
