<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<div class="checkbox">
	<label for="showAggregatedReports">
	<html:checkbox property="showAggregatedReports" value="1" styleId="showAggregatedReports"></html:checkbox>
	<fmt:message key="advanced.showAggregatedReports" />
	</label>
</div>

<div class="checkbox">
	<label for="reflectOnActivity">
	<html:checkbox property="reflectOnActivity" value="1" styleId="reflectOnActivity"></html:checkbox>
	<fmt:message key="advanced.reflectOnActivity" />
	</label>
</p>
<p>
	<html:textarea property="reflectInstructions" rows="3" styleId="reflectInstructions" styleClass="form-control"/>
</p>

<h4>
	<fmt:message key="advanced.selectScribe" />
</h4>

<div class="form-group">
	<html:radio property="autoSelectScribe" value="true" styleId="autoSelectScribe1">
	</html:radio>
	<label for="autoSelectScribe1">
		<fmt:message key="advanced.firstLearner" />
	</label>
</div>
<div class="form-group">
	<html:radio property="autoSelectScribe" value="false" styleId="autoSelectScribe2">
	</html:radio>
	<label for="autoSelectScribe2">
		<fmt:message key="advanced.selectInMonitor" />
	</label>
</div>

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
