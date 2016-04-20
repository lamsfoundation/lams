<%@ include file="/includes/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<lams:CommentsAuthor/>

<div class="form-group">
    <html:checkbox property="reflectOnActivity" value="1"
		styleClass="noBorder" styleId="reflectOnActivity"></html:checkbox>
	<label for="reflectOnActivity">&nbsp;
		<fmt:message key="advanced.reflectOnActivity" />
	</label>
	<BR/>
	<html:textarea property="reflectInstructions" cols="60" rows="3"  styleId="reflectInstructions"/>
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
