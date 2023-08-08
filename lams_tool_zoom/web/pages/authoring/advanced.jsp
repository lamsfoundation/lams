<%@ include file="/common/taglibs.jsp"%>

<c:set var="durationSet" value="${not empty authoringForm.duration && authoringForm.duration > 0}" />

<!-- ========== Advanced Tab ========== -->

<lams:OutcomeAuthor toolContentId="${sessionMap.toolContentID}" />

<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox voffset10">
		<label for="startInMonitor">
			<form:checkbox path="startInMonitor" cssClass="noBorder" id="startInMonitor" />
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
							value="${authoringForm.duration}"
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
		<label for="reflectOnActivity"> <form:checkbox path="reflectOnActivity" cssClass="noBorder"	id="reflectOnActivity"/>
			<fmt:message key="advanced.reflectOnActivity" />
		</label>
	</div>
	
	<div class="form-group">
		<form:textarea path="reflectInstructions" id="reflectInstructions" cssClass="form-control" rows="3"/>
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
			duration.prop('disabled', false).val("40");
		} else {
			duration.prop('disabled', true).val(null);
		}
	});
</script>