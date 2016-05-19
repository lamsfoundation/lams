<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<div class="checkbox">
	<label for="lockOnFinished"><html:checkbox property="lockOnFinished" value="1" styleClass="noBorder"
			styleId="lockOnFinished"></html:checkbox> <fmt:message key="advanced.lockOnFinished" /> </label>
</div>

<div class="checkbox voffset10">
	<label for="reflectOnActivity"> <html:checkbox property="reflectOnActivity" value="1" styleClass="noBorder"
			styleId="reflectOnActivity"></html:checkbox> <fmt:message key="advanced.reflectOnActivity" />
	</label>
</div>

<div class="form-group">
	<html:textarea property="reflectInstructions" styleId="reflectInstructions" styleClass="form-control" rows="3"/>
</div>

<script type="text/javascript">
<!--
	//automatically turn on refect option if there are text input in refect instruction area
	var ra = document.getElementById("reflectInstructions");
	var rao = document.getElementById("reflectOnActivity");
	function turnOnRefect() {
		if (isEmpty(ra.value)) {
			//turn off	
			rao.checked = false;
		} else {
			//turn on
			rao.checked = true;
		}
	}

	ra.onkeyup = turnOnRefect;
//-->
</script>


