<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<lams:SimplePanel titleKey="label.wiki.options">

	<div class="checkbox">
		<label for="allowLearnerCreatePages"> 
		<html:checkbox	property="allowLearnerCreatePages" value="1" styleId="allowLearnerCreatePages"></html:checkbox> 
		<fmt:message key="advanced.allowLearnerCreatePages" /></label>
	</div>
	
	<div class="checkbox">
		<label	for="allowLearnerInsertLinks">
		<html:checkbox property="allowLearnerInsertLinks" value="1" styleId="allowLearnerInsertLinks"></html:checkbox>  
		<fmt:message key="advanced.allowLearnerInsertLinks" /></label>
	</div>
	
	<div class="checkbox">
		<label for="allowLearnerAttachImages"> 
		<html:checkbox	property="allowLearnerAttachImages" value="1" styleId="allowLearnerAttachImages"></html:checkbox> 
		<fmt:message key="advanced.allowLearnerAttachImages" /> </label>
	</div>
		
	<div><fmt:message key="advanced.editingLimits.prompt" /></div>
	
	<div><fmt:message key="advanced.editingLimits.minimum" />&nbsp;<html:select
		property="minimumEdits" styleId="minimumEdits" styleClass="form-control form-control-inline input-sm" onchange="validateEditCount(true);">
		<html:option value="0">
			<fmt:message key="advanced.editingLimits.nominimum" />
		</html:option>
		<html:option value="1">1</html:option>
		<html:option value="2">2</html:option>
		<html:option value="3">3</html:option>
		<html:option value="4">4</html:option>
		<html:option value="5">5</html:option>
		<html:option value="6">6</html:option>
		<html:option value="7">7</html:option>
		<html:option value="8">8</html:option>
		<html:option value="9">9</html:option>
		<html:option value="10">10</html:option>
	</html:select>&nbsp;<fmt:message key="advanced.editingLimits.maximum" />&nbsp;<html:select
		property="maximumEdits" styleId="maximumEdits" styleClass="form-control form-control-inline input-sm" onchange="validateEditCount(true);">
		<html:option value="0">
			<fmt:message key="advanced.editingLimits.nomaximum" />
		</html:option>
		<html:option value="1">1</html:option>
		<html:option value="2">2</html:option>
		<html:option value="3">3</html:option>
		<html:option value="4">4</html:option>
		<html:option value="5">5</html:option>
		<html:option value="6">6</html:option>
		<html:option value="7">7</html:option>
		<html:option value="8">8</html:option>
		<html:option value="9">9</html:option>
		<html:option value="10">10</html:option>
	</html:select></div>

</lams:SimplePanel>

<lams:SimplePanel titleKey="label.notifications">

	<div class="checkbox">
		<label	for="notifyUpdates">
		<html:checkbox property="notifyUpdates" value="1" styleId="notifyUpdates"></html:checkbox>
		<fmt:message key="advanced.notifyChange" /> </label>
	</div>
	
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox">
		<label for="lockOnFinished">
		<html:checkbox property="lockOnFinished" value="1" styleId="lockOnFinished"></html:checkbox>
		<fmt:message key="advanced.lockOnFinished" /></label>
	</div>
	
	<div class="checkbox">
		<label for="reflectOnActivity">
		<html:checkbox property="reflectOnActivity" value="1"
		styleId="reflectOnActivity"></html:checkbox>
		<fmt:message key="advanced.reflectOnActivity" /> </label>
	</div>
	<div class="form-group">
		<html:textarea property="reflectInstructions" rows="3" styleClass="form-control" styleId="reflectInstructions" />
	</div>
	
</lams:SimplePanel>	

<script type="text/javascript">
<!--
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

