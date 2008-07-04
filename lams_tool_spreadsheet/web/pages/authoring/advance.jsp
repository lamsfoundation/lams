<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Advance Tab Content -->

<p class="small-space-top">
	<html:checkbox property="spreadsheet.lockWhenFinished"
		styleClass="noBorder" styleId="lockWhenFinished">
	</html:checkbox>

	<label for="lockWhenFinished">
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</label>
</p>

<p class="small-space-top">
	<input type="radio" name="spreadsheet.learnerAllowedToSave" value="${false}" styleId="learnerNotAllowedToSave"
		<c:if test="${not formBean.spreadsheet.learnerAllowedToSave}">checked="checked"</c:if> 
		onclick="document.spreadsheetForm.isMarkingEnabled.disabled = true;
				 document.spreadsheetForm.isMarkingEnabled.checked = false;"
	/>
	<label for="learnerNotAllowedToSave">
		<fmt:message key="label.authoring.advanced.play.only" />
	</label>
	<br><br>

	<input type="radio" name="spreadsheet.learnerAllowedToSave" value="${true}" styleId="learnerAllowedToSave"
		<c:if test="${formBean.spreadsheet.learnerAllowedToSave}">checked="checked"</c:if> 
		onclick="document.spreadsheetForm.isMarkingEnabled.disabled = false;"
	/>
	<label for="learnerAllowedToSave">
		<fmt:message key="label.authoring.advanced.learners.are.allowed.to.save" />
	</label>
	<br><br>

	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<html:checkbox property="spreadsheet.markingEnabled" styleClass="noBorder" styleId="isMarkingEnabled"
		disabled="${not formBean.spreadsheet.learnerAllowedToSave}" >
	</html:checkbox>
	<label for="isMarkingEnabled">
		<fmt:message key="label.authoring.advanced.enable.spreadsheet.marking" />
	</label>
</p>

<p>
	<html:checkbox property="spreadsheet.reflectOnActivity"
		styleClass="noBorder" styleId="reflectOn">
	</html:checkbox>
	<label for="reflectOn">
		<fmt:message key="label.authoring.advanced.reflectOnActivity" />
	</label>
</p>

<p>
	<html:textarea property="spreadsheet.reflectInstructions" styleId="reflectInstructions" cols="30" rows="3" />
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
-->
</script>
