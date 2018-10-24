<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<lams:SimplePanel titleKey="label.image.options">
	<div class="checkbox">
		<label for="allowViewOthersImages">
			<form:checkbox path="allowViewOthersImages" value="1" id="allowViewOthersImages"/>
			<fmt:message key="advanced.allowViewOthersImages" />
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
		<label for="reflect-on-activity">
			<form:checkbox path="reflectOnActivity" id="reflect-on-activity"/>
			<fmt:message key="advanced.reflectOnActivity" />
		</label>
	</div>
	
	<div class="form-group">
		<form:textarea path="reflectInstructions" cols="60" rows="3" id="reflect-instructions" class="form-control"/>
	</div>
</lams:SimplePanel>
	
<script type="text/javascript">
	//automatically turn on refect option if there are text input in refect instruction area
	var ra = document.getElementById("reflect-instructions");
	var rao = document.getElementById("reflect-on-activity");
	function turnOnRefect(){
		if (isEmpty(ra.value)) {
		//turn off	
			rao.checked = false;
		} else {
		//turn on
			rao.checked = true;		
		}
	}

	ra.onkeyup=turnOnRefect;
</script>

