<%@ include file="/common/taglibs.jsp"%>

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="durationSet" value="${not empty formBean.duration && formBean.duration > 0}" />

<!-- ========== Advanced Tab ========== -->

<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox voffset10">
		<label for="startInMonitor">
			<html:checkbox property="startInMonitor" value="1" styleClass="noBorder" styleId="startInMonitor"></html:checkbox> 
			<fmt:message key="advanced.startInMonitor" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="durationSwitch">
			<input type="checkbox" id="durationSwitch" class="noBorder" 
				<c:if test="${durationSet}">
					checked="checked"
				</c:if>
			/>
			<fmt:message key="advanced.duration.switch" />
		</label>
	</div>
	
	<div class="form-group">
		<label for="duration">
			<input type="number" name="duration" id="duration" class="form-control"
				   max="99999" min="1" style="width: 100px; display:inline-block; margin-right: 10px; margin-left: 20px"
					<c:choose>
						<c:when test="${durationSet}">
							value="${formBean.duration}"
						</c:when>
						<c:otherwise>
							disabled="disabled"
						</c:otherwise>
					</c:choose>
			/>
			<fmt:message key="advanced.duration" />
		</label>
	</div>

	<div class="checkbox voffset10">
		<label for="reflectOnActivity"> <html:checkbox property="reflectOnActivity" value="1" styleClass="noBorder"
				styleId="reflectOnActivity"></html:checkbox> <fmt:message key="advanced.reflectOnActivity" />
		</label>
	</div>
	
	<div class="form-group">
		<html:textarea property="reflectInstructions" styleId="reflectInstructions" styleClass="form-control" rows="3"/>
	</div>

</lams:SimplePanel>

<script type="text/javascript">
	//automatically turn on refect option if there are text input in refect instruction area
	var ra = document.getElementById("reflectInstructions"),
		rao = document.getElementById("reflectOnActivity");
	function turnOnReflect() {
		if (isEmpty(ra.value)) {
			//turn off	
			rao.checked = false;
		} else {
			//turn on
			rao.checked = true;
		}
	}
	ra.onkeyup = turnOnReflect;

	$('#durationSwitch').change(function(){
		var duration = $('#duration');
		if ($(this).prop('checked')) {
			duration.prop('disabled', false).val("120");
		} else {
			duration.prop('disabled', true).val(null);
		}
	});
</script>