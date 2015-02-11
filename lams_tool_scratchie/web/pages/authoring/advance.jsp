<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<!-- Advance Tab Content -->

<c:if test="${sessionMap.isEnabledExtraPointOption}">
	<p>
		<html:checkbox property="scratchie.extraPoint" styleClass="noBorder" styleId="extraPoint"/>
		<label for="extraPoint">
			<fmt:message key="label.authoring.advanced.give.extra.point" />
		</label>
	</p>
</c:if>

<p>
	<html:checkbox property="scratchie.burningQuestionsEnabled" styleClass="noBorder" styleId="burningQuestionsEnabled"/>
	<label for="burningQuestionsEnabled">
		<fmt:message key="label.authoring.advanced.burning.questions" />
	</label>
</p>

<p>
	<html:checkbox property="scratchie.reflectOnActivity" styleClass="noBorder" styleId="reflectOn"/>
	<label for="reflectOn">
		<fmt:message key="label.authoring.advanced.reflectOnActivity" />
	</label>
</p>

<p>
	<html:textarea property="scratchie.reflectInstructions" styleId="reflectInstructions" cols="30" rows="3" />
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
