<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.scratchie.options">
	<c:if test="${sessionMap.isEnabledExtraPointOption}">
		<div class="checkbox">
			<label for="extraPoint">
				<html:checkbox property="scratchie.extraPoint" styleId="extraPoint"/>
				<fmt:message key="label.authoring.advanced.give.extra.point" />
			</label>
		</div>
	</c:if>
	
	<div class="checkbox">
		<label for="burningQuestionsEnabled">
			<html:checkbox property="scratchie.burningQuestionsEnabled" styleId="burningQuestionsEnabled"/>
			<fmt:message key="label.authoring.advanced.burning.questions" />
		</label>
	</div>
	
	<div class="form-inline">
		<label for="time-limit">
			<fmt:message key="label.time.limit" />&nbsp;
			<html:text property="scratchie.timeLimit" size="3" styleId="time-limit" styleClass="form-control input-sm"/>
		</label>
	</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.activity.completion">
	<div class="checkbox">
		<label for="reflect-on">
			<html:checkbox property="scratchie.reflectOnActivity" styleId="reflect-on"/>
			<fmt:message key="label.authoring.advanced.reflectOnActivity" />
		</label>
	</div>
	
	<div class="form-group">
		<html:textarea property="scratchie.reflectInstructions"  styleClass="form-control" styleId="reflectInstructions" rows="3" />
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
//-->
</script>
