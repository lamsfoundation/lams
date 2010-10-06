<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<p class="small-space-top"><html:checkbox property="lockOnFinished"
	value="1" styleClass="noBorder" styleId="lockOnFinished"></html:checkbox>
<label for="lockOnFinished"> <fmt:message
	key="advanced.lockOnFinished" /> </label></p>

<p class="small-space-top"><html:checkbox
	property="allowLearnerCreatePages" value="1" styleClass="noBorder"
	styleId="allowLearnerCreatePages"></html:checkbox> <label
	for="allowLearnerCreatePages"> <fmt:message
	key="advanced.allowLearnerCreatePages" /> </label></p>

<p class="small-space-top"><html:checkbox
	property="allowLearnerInsertLinks" value="1" styleClass="noBorder"
	styleId="allowLearnerInsertLinks"></html:checkbox> <label
	for="allowLearnerInsertLinks"> <fmt:message
	key="advanced.allowLearnerInsertLinks" /> </label></p>

<p class="small-space-top"><html:checkbox
	property="allowLearnerAttachImages" value="1" styleClass="noBorder"
	styleId="allowLearnerAttachImages"></html:checkbox> <label
	for="allowLearnerAttachImages"> <fmt:message
	key="advanced.allowLearnerAttachImages" /> </label></p>
	
<p class="small-space-top"><html:checkbox
	property="notifyUpdates" value="1" styleClass="noBorder"
	styleId="notifyUpdates"></html:checkbox> <label
	for="notifyUpdates"> <fmt:message
	key="advanced.notifyChange" /> </label></p>

<p><html:checkbox property="reflectOnActivity" value="1"
	styleClass="noBorder" styleId="reflectOnActivity"></html:checkbox> <label
	for="reflectOnActivity"> <fmt:message
	key="advanced.reflectOnActivity" /> </label></p>
<p><html:textarea property="reflectInstructions" cols="30" rows="3"
	styleId="reflectInstructions" /></p>


<h2><fmt:message key="advanced.editingLimits" /></h2>

<p><fmt:message key="advanced.editingLimits.prompt" /></p>

<p><fmt:message key="advanced.editingLimits.minimum" /> <html:select
	property="minimumEdits" styleId="minimumEdits">
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
</html:select> <fmt:message key="advanced.editingLimits.maximum" /> <html:select
	property="maximumEdits" styleId="maximumEdits">
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
</html:select></p>


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
	
	var minMenu = document.getElementById("minimumEdits");
	var maxMenu = document.getElementById("maximumEdits");
	minMenu.onchange = updateMinimumField
	
	function updateMinimumField()
	{
		var min = document.getElementById("minimumEdits").value;
		var max = document.getElementById("maximumEdits").value;
		
		if (max > 0 && min > max)
		{
			alert("<fmt:message key='advanced.editingLimits.error' />");
			minMenu.selectedIndex = max;
		}
	}
//-->
</script>

