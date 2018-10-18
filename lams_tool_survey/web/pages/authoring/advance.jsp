<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.survey.options">
	
	<div class="checkbox">
		<label for="showOnePage">
		<html:checkbox property="survey.showOnePage" styleId="showOnePage"/>
			<fmt:message key="label.authoring.advance.show.on.one.page" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="showOtherUsersAnswers">
		<html:checkbox property="survey.showOtherUsersAnswers" styleId="showOtherUsersAnswers"/>
			<fmt:message key="label.show.answers.from.other.users" />
		</label>
	</div>

</lams:SimplePanel>

<lams:SimplePanel titleKey="label.notifications">

	<div class="checkbox">
		<label for="notifyTeachersOnAnswerSumbit">
		<html:checkbox property="survey.notifyTeachersOnAnswerSumbit" styleId="notifyTeachersOnAnswerSumbit"/>
			<fmt:message key="label.authoring.advanced.notify.onanswersubmit" />
		</label>
	</div>

</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${formBean.survey.contentId}" />

<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox">
		<label for="lockWhenFinished">
		<html:checkbox property="survey.lockWhenFinished" styleId="lockWhenFinished"/>
			<fmt:message key="label.authoring.advance.lock.on.finished" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="reflectOn">
		<html:checkbox property="survey.reflectOnActivity" styleId="reflectOn"/>
			<fmt:message key="label.authoring.advanced.reflectOnActivity" />
		</label>
	</div>
	
	<div class="form-group">
		<html:textarea property="survey.reflectInstructions" styleId="reflectInstructions" rows="3" styleClass="form-control"/>
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
