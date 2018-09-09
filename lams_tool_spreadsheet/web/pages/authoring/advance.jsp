<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Advance Tab Content -->
	
<lams:SimplePanel titleKey="label.spreadsheet.options">
	
	<div class="radio">
		<label for="learnerNotAllowedToSave">
		<input type="radio" name="spreadsheet.learnerAllowedToSave" value="${false}" styleId="learnerNotAllowedToSave"
			<c:if test="${not formBean.spreadsheet.learnerAllowedToSave}">checked="checked"</c:if> 
			onclick="document.spreadsheetForm.isMarkingEnabled.disabled = true;
					 document.spreadsheetForm.isMarkingEnabled.checked = false;"
		/>
			<fmt:message key="label.authoring.advanced.play.only" />
		</label>
	</div>
	
	<div class="radio">
		<label for="learnerAllowedToSave">
		<input type="radio" name="spreadsheet.learnerAllowedToSave" value="${true}" styleId="learnerAllowedToSave"
			<c:if test="${formBean.spreadsheet.learnerAllowedToSave}">checked="checked"</c:if> 
			onclick="document.spreadsheetForm.isMarkingEnabled.disabled = false;"
		/>
			<fmt:message key="label.authoring.advanced.learners.are.allowed.to.save" />
		</label>
	</div>
	
	<div class="checkbox loffset10">
		<label for="isMarkingEnabled">
		<html:checkbox property="spreadsheet.markingEnabled" styleId="isMarkingEnabled"
			disabled="${not formBean.spreadsheet.learnerAllowedToSave}" >
		</html:checkbox>
		<fmt:message key="label.authoring.advanced.enable.spreadsheet.marking" />
		</label>
	</div>

</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${formBean.spreadsheet.contentId}" />

<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox">
		<label for="lockWhenFinished"><html:checkbox property="spreadsheet.lockWhenFinished" styleId="lockWhenFinished" />
		<fmt:message key="label.authoring.advance.lock.on.finished" /></label>
	</div>

	<div class="checkbox">
		<label for="reflectOn"><html:checkbox property="spreadsheet.reflectOnActivity" styleId="reflectOn"/>
		<fmt:message key="label.authoring.advanced.reflectOnActivity" /></label>
		</div>
		<div class="form-group">
		<html:textarea property="spreadsheet.reflectInstructions" styleId="reflectInstructions"  styleClass="form-control" rows="3"
			onkeyup="javascript:turnOnReflect()"/>
	</div>

</lams:SimplePanel>

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
