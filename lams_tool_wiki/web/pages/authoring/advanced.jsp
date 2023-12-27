<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<lams:SimplePanel titleKey="label.wiki.options">

	<div class="checkbox">
		<label for="allowLearnerCreatePages"> 
		<form:checkbox	path="allowLearnerCreatePages" value="1" id="allowLearnerCreatePages"/>
		<fmt:message key="advanced.allowLearnerCreatePages" /></label>
	</div>
	
	<div class="checkbox">
		<label	for="allowLearnerInsertLinks">
		<form:checkbox path="allowLearnerInsertLinks" value="1" id="allowLearnerInsertLinks"/>
		<fmt:message key="advanced.allowLearnerInsertLinks" /></label>
	</div>
	
	<div class="checkbox">
		<label for="allowLearnerAttachImages"> 
		<form:checkbox	path="allowLearnerAttachImages" value="1" id="allowLearnerAttachImages"/>
		<fmt:message key="advanced.allowLearnerAttachImages" /> </label>
	</div>
		
	<div><fmt:message key="advanced.editingLimits.prompt" /></div>
	
	<div><fmt:message key="advanced.editingLimits.minimum" />&nbsp;
	<form:select path="minimumEdits" id="minimumEdits" cssClass="form-control form-control-inline input-sm" onchange="validateEditCount(true);">
		<form:option value="0"> <fmt:message key="advanced.editingLimits.nominimum" /> </form:option>
		<form:option value="1" label="1"/>
		<form:option value="2" label="2"/>
		<form:option value="3" label="3"/>
		<form:option value="4" label="4"/>
		<form:option value="5" label="5"/>
		<form:option value="6" label="6"/>
		<form:option value="7" label="7"/>
		<form:option value="8" label="8"/>
		<form:option value="9" label="9"/>
		<form:option value="10" label="10"/>
	</form:select>&nbsp;<fmt:message key="advanced.editingLimits.maximum" />&nbsp;
	<form:select path="maximumEdits" id="maximumEdits" cssClass="form-control form-control-inline input-sm" onchange="validateEditCount(true);">
		<form:option value="0"> <fmt:message key="advanced.editingLimits.nomaximum" /> </form:option>
		<form:option value="1" label="1"/>
		<form:option value="2" label="2"/>
		<form:option value="3" label="3"/>
		<form:option value="4" label="4"/>
		<form:option value="5" label="5"/>
		<form:option value="6" label="6"/>
		<form:option value="7" label="7"/>
		<form:option value="8" label="8"/>
		<form:option value="9" label="9"/>
		<form:option value="10" label="10"/>
	</form:select></div>

</lams:SimplePanel>

<lams:SimplePanel titleKey="label.notifications">

	<div class="checkbox">
		<label	for="notifyUpdates">
		<form:checkbox path="notifyUpdates" value="1" id="notifyUpdates"/>
		<fmt:message key="advanced.notifyChange" /> </label>
	</div>
	
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${sessionMap.toolContentID}" />

<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox">
		<label for="lockOnFinished">
		<form:checkbox path="lockOnFinished" value="1" id="lockOnFinished"/>
		<fmt:message key="advanced.lockOnFinished" /></label>
	</div>
</lams:SimplePanel>	

<script type="text/javascript">
<!--
	
	function validateEditCount(isMinimunDropdownUsed) {
		var minDropDown = document.getElementById("minimumEdits");
		var min = parseInt(minimumEdits.options[minimumEdits.selectedIndex].value);
		var maxDropDown = document.getElementById("maximumEdits");
		var max = parseInt(maximumEdits.options[maximumEdits.selectedIndex].value);

		if (min > max && max > 0) {
			if (isMinimunDropdownUsed) {
				maxDropDown.selectedIndex = minDropDown.selectedIndex;
			} else {
				minDropDown.selectedIndex = maxDropDown.selectedIndex;
			} 
			alert('<fmt:message key="advanced.editingLimits.error" />');
		}
	}

//-->
</script>

