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
	
	<div class="checkbox">
		<label for="allowMultipleLeaders">
			<html:checkbox property="dokumaran.allowMultipleLeaders" value="1" styleId="allowMultipleLeaders"
				disabled="${!formBean.dokumaran.useSelectLeaderToolOuput}"/>
			<fmt:message key="label.allow.multiple.leaders" />
		</label>
	</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.resource.options">

	<div class="form-inline">
		<label for="timeLimit">
			<fmt:message key="label.time.limit" />&nbsp;
			<html:text property="dokumaran.timeLimit" size="3" styleId="timeLimit" styleClass="form-control input-sm"/>
		</label>
	</div>
	
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
	
	<div class="checkbox">
		<label for="shared-pad-id-on">
			<input type="checkbox" id="shared-pad-id-on" 
				<c:if test="${not empty formBean.dokumaran.sharedPadId}">checked="checked"</c:if>/>
			<fmt:message key="label.shared.pad.id" />
		</label>
	</div>
	
	<div class="form-group">
		<html:text property="dokumaran.sharedPadId" styleClass="form-control" styleId="shared-pad-id" />
	</div>

</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${formBean.dokumaran.contentId}" />

<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox">
		<label for="lockWhenFinished">
			<html:checkbox property="dokumaran.lockWhenFinished" styleId="lockWhenFinished" />
			<fmt:message key="label.authoring.advance.lock.on.finished" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="reflect-on">
			<html:checkbox property="dokumaran.reflectOnActivity" styleId="reflect-on"/>
			<fmt:message key="label.authoring.advanced.reflectOnActivity" />
		</label>
	</div>
	
	<div class="form-group">
		<html:textarea property="dokumaran.reflectInstructions" styleClass="form-control" styleId="reflect-instructions" rows="3" />
	</div>
	
</lams:SimplePanel>

<script type="text/javascript">
	//automatically turn on refect option if there are text input in refect instruction area
	$('#reflect-instructions').keyup(function(){
		$('#reflect-on').prop('checked', !isEmpty($(this).val()));
	});
	
	//automatically turn on shared-pad-id-on option if there are text input in shared-pad-id area
	$('#shared-pad-id').keyup(function(){
		$('#shared-pad-id-on').prop('checked', !isEmpty($(this).val()));
	});
	
	//automatically turn on shared-pad-id-on option if there are text input in shared-pad-id area
	$('#useSelectLeaderToolOuput').change(function(){
		$('#allowMultipleLeaders').prop('disabled', !$('#allowMultipleLeaders').prop('disabled'));
	});
</script>
