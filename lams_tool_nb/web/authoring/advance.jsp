<%@ include file="/includes/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<p class="small-space-top">
	<html:checkbox property="allowComments" value="1"
		styleClass="noBorder" styleId="allowComments"></html:checkbox>
	<label for="allowComments">
		<fmt:message key="advanced.allow.comments" />
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
	<html:textarea property="reflectInstructions" cols="30" rows="3"  styleId="reflectInstructions"/>
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
