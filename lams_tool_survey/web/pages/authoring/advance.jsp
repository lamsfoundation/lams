<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Advance Tab Content -->

<p class="small-space-top">
	<html:checkbox property="survey.lockWhenFinished" styleClass="noBorder" styleId="lockWhenFinished"/>
	<label for="lockWhenFinished">
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</label>
</p>

<p>
	<html:checkbox property="survey.showOnePage" styleClass="noBorder" styleId="showOnePage"/>
	<label for="showOnePage">
		<fmt:message key="label.authoring.advance.show.on.one.page" />
	</label>
</p>

<p>
	<html:checkbox property="survey.showOtherUsersAnswers" styleClass="noBorder" styleId="showOtherUsersAnswers"/>
	<label for="showOtherUsersAnswers">
		<fmt:message key="label.show.answers.from.other.users" />
	</label>
</p>

<p>
	<html:checkbox property="survey.notifyTeachersOnAnswerSumbit" styleClass="noBorder" styleId="notifyTeachersOnAnswerSumbit"/>
	<label for="notifyTeachersOnAnswerSumbit">
		<fmt:message key="label.authoring.advanced.notify.onanswersubmit" />
	</label>
</p>

<p>
	<html:checkbox property="survey.reflectOnActivity" styleClass="noBorder" styleId="reflectOn"/>
	<label for="reflectOn">
		<fmt:message key="label.authoring.advanced.reflectOnActivity" />
	</label>
</p>

<p>
	<html:textarea property="survey.reflectInstructions" styleId="reflectInstructions" cols="30" rows="3" />
</p>

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
