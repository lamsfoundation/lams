<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<lams:SimplePanel titleKey="label.mindmap.options">

	<div class="checkbox">
		<label for="multiUserMode">
			<form:checkbox path="multiUserMode" value="1" id="multiUserMode"/>
			<fmt:message key="advanced.multiUserMode" />
		</label>
	</div>
	
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${sessionMap.toolContentID}" />

<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox">
		<label for="lock-when-finished">
			<form:checkbox path="lockOnFinished" id="lock-when-finished"/>
			<fmt:message key="advanced.lockOnFinished" />
		</label>
	</div>
	
	<!-- Reflection -->
	<div class="checkbox">
		<label for="reflect-on">
			<form:checkbox path="reflectOnActivity" id="reflect-on"/>
			<fmt:message key="advanced.reflectOnActivity" />
		</label>
	</div>
	
	<div class="form-group">
		<textarea name="reflectInstructions" cols="60" rows="3" id="reflect-instructions" class="form-control">${authoringForm.reflectInstructions}</textarea>
	</div>

</lams:SimplePanel>

<script type="text/javascript">
//automatically turn on refect option if there are text input in refect instruction area
	var ra = document.getElementById("reflect-instructions");
	var rao = document.getElementById("reflect-on");

	function turnOnRefect() {
		if(isEmpty(ra.value)) {
		//turn off	
			rao.checked = false;
		} else {
		//turn on
			rao.checked = true;
		}
	}

	ra.onkeyup = turnOnRefect;
</script>