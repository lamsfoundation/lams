<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean"
	value="<%=request
									.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Advance Tab Content -->

<p class="small-space-top">
	<html:checkbox property="eadventure.lockWhenFinished"
		styleClass="noBorder" styleId="lockWhenFinished">
	</html:checkbox>

	<label for="lockWhenFinished">
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</label>
</p>

<p>
	<html:checkbox property="eadventure.defineComplete" styleClass="noBorder" styleId="defineComplete">
	</html:checkbox>
	<label for="defineComplete">
		<fmt:message key="label.authoring.advance.define.completed" />  
	</label>
</p>
<!-- 
<p>
	<html:checkbox property="eadventure.defineComplete"
		styleClass="noBorder" styleId="defineComplete">
	</html:checkbox>
	<label for="defineComplete">
		If you select this option, the activity only will finish when the game assess as the game has been completed
	</label>
</p>
-->

<p>
	<html:checkbox property="eadventure.reflectOnActivity"
		styleClass="noBorder" styleId="reflectOn">
	</html:checkbox>
	<label for="reflectOn">
		Add Notebook at the end of eAdventure Game with the following instructions:
	</label>
</p>

<p>
	<html:textarea property="eadventure.reflectInstructions"
		styleId="reflectInstructions" cols="30" rows="3" />
</p>
<script type="text/javascript">
<!--
//automatically turn on refect option if there are text input in refect instruction area
	var ra = document.getElementById("reflectInstructions");
	var rao = document.getElementById("reflectOn");
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
