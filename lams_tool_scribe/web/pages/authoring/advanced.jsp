<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<p class="small-space-top">
	<html:checkbox property="showAggregatedReports" value="1"
		styleClass="noBorder" styleId="showAggregatedReports"></html:checkbox>

	<label for="showAggregatedReports">
		<fmt:message key="advanced.showAggregatedReports" />
	</label>
</p>

<p class="small-space-top">
	<html:checkbox property="reflectOnActivity" value="1"
		styleClass="noBorder" styleId="reflectOnActivity"></html:checkbox>

	<label for="reflectOnActivity">
		<fmt:message key="advanced.reflectOnActivity" />
	</label>
</p>
<p>
	<html:textarea property="reflectInstructions" cols="30" rows="3" styleId="reflectInstructions"/>
</p>

<h2>
	<fmt:message key="advanced.selectScribe" />
</h2>

<p>
	<html:radio property="autoSelectScribe" value="true" styleId="autoSelectScribe1" styleClass="noBorder">
	</html:radio>
	<label for="autoSelectScribe1">
		<fmt:message key="advanced.firstLearner" />
	</label>
</p>
<p>
	<html:radio property="autoSelectScribe" value="false" styleId="autoSelectScribe2" styleClass="noBorder">
	</html:radio>
	<label for="autoSelectScribe2">
		<fmt:message key="advanced.selectInMonitor" />
	</label>
</p>

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
//-->
</script>
