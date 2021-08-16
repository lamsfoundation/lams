<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->
<lams:SimplePanel titleKey="label.chat.options">
<div class="checkbox">
	<label for="filteringEnabled">
		<form:checkbox path="filteringEnabled" value="1" id="filteringEnabled"/>
		<fmt:message key="advanced.filteringEnabled" />
	</label>
	<lams:Popover>
		<fmt:message key="advanced.filteringEnabled.tooltip.1" /><br>
		<fmt:message key="advanced.filteringEnabled.tooltip.2" />
	</lams:Popover>
</div>
<div class="form-group">
	<textarea name="filterKeywords" rows="3" class="form-control">${authoringForm.filterKeywords}</textarea>
</div>
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${sessionMap.toolContentID}" />

<lams:SimplePanel titleKey="label.activity.completion">
<div class="checkbox">
	<label for="lockOnFinished">
		<form:checkbox path="lockOnFinished" value="1" id="lockOnFinished"/>
		<fmt:message key="advanced.lockOnFinished" />
	</label>
	<lams:Popover>
		<fmt:message key="advanced.lockOnFinished.tooltip.1" />
	</lams:Popover>
</div>

<div class="checkbox">
	<label for="reflectOnActivity">
		<form:checkbox path="reflectOnActivity" value="1" id="reflectOnActivity"/>
		<fmt:message key="advanced.reflectOnActivity" />
	</label>
	<lams:Popover>
		<fmt:message key="advanced.reflectOnActivity.tooltip.1" /><br>
		<fmt:message key="advanced.reflectOnActivity.tooltip.2" />
	</lams:Popover>
</div>

<div class="form-group">
	<textarea name="reflectInstructions" rows="3" id="reflectInstructions" class="form-control">${authoringForm.reflectInstructions}</textarea>
</div>
</lams:SimplePanel>

<script type="text/javascript">

//automatically turn on refect option if there are text input in refect instruction area
	var ra = document.getElementById("reflectInstructions");
	var rao = document.getElementById("reflectOnActivity");
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

</script>
