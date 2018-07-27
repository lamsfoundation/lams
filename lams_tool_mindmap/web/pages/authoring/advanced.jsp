<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<lams:SimplePanel titleKey="label.mindmap.options">

	<div class="checkbox">
		<label for="multiUserMode">
			<html:checkbox property="multiUserMode" value="1" styleId="multiUserMode"/>
			<fmt:message key="advanced.multiUserMode" />
		</label>
	</div>
	
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox">
		<label for="lock-when-finished">
			<html:checkbox property="lockOnFinished" styleId="lock-when-finished"/>
			<fmt:message key="advanced.lockOnFinished" />
		</label>
	</div>
	
	<!-- Reflection -->
	<div class="checkbox">
		<label for="reflect-on">
			<html:checkbox property="reflectOnActivity" styleId="reflect-on"/>
			<fmt:message key="advanced.reflectOnActivity" />
		</label>
	</div>
	
	<div class="form-group">
		<html:textarea property="reflectInstructions" cols="60" rows="3" styleId="reflect-instructions" styleClass="form-control"/>
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