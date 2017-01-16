<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.select.leader">
	<div class="checkbox">
		<label for="useSelectLeaderToolOuput">
			<html:checkbox property="dokumaran.useSelectLeaderToolOuput" value="1" styleId="useSelectLeaderToolOuput"/>
			<fmt:message key="label.use.select.leader.tool.output" />
		</label>
	</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.resource.options">
	
	<div class="checkbox">
		<label for="showChat">
			<html:checkbox property="dokumaran.showChat" styleId="showChat"/>
			<fmt:message key="label.show.chat" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="showLineNumbers">
			<html:checkbox property="dokumaran.showLineNumbers" styleId="showLineNumbers"/>
			<fmt:message key="label.show.line.numbers" />
		</label>
	</div>

</lams:SimplePanel>

<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox">
		<label for="lockWhenFinished">
			<html:checkbox property="dokumaran.lockWhenFinished" styleId="lockWhenFinished" />
			<fmt:message key="label.authoring.advance.lock.on.finished" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="reflectOn"><html:checkbox property="dokumaran.reflectOnActivity" styleId="reflectOn"/>
			<fmt:message key="label.authoring.advanced.reflectOnActivity" />
		</label>
	</div>
	
	<div class="form-group">
		<html:textarea property="dokumaran.reflectInstructions" styleClass="form-control" styleId="reflectInstructions" rows="3" />
	</div>
	
</lams:SimplePanel>

<script type="text/javascript">
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
</script>
