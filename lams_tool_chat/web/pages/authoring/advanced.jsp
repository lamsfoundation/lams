<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->
<lams:SimplePanel titleKey="label.chat.options">
<div class="checkbox">
	<label for="filteringEnabled">
	<html:checkbox property="filteringEnabled" value="1" styleId="filteringEnabled"></html:checkbox>
	<fmt:message key="advanced.filteringEnabled" />
	</label>
</div>
<div class="form-group">
	<html:textarea property="filterKeywords" rows="3" styleClass="form-control"/>
</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.activity.completion">
<div class="checkbox">
	<label for="lockOnFinished">
	<html:checkbox property="lockOnFinished" value="1"	styleId="lockOnFinished">	</html:checkbox>
	<fmt:message key="advanced.lockOnFinished" />
	</label>
</div>

<div class="checkbox">
	<label for="reflectOnActivity">
	<html:checkbox property="reflectOnActivity" value="1" styleId="reflectOnActivity"></html:checkbox>
	<fmt:message key="advanced.reflectOnActivity" />
	</label>
</div>

<div class="form-group">
	<html:textarea property="reflectInstructions" rows="3" styleId="reflectInstructions" styleClass="form-control"/>
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
//-->
</script>

