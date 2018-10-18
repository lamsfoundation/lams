<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.resource.options">

<div class="checkbox">
	<label for="run-auto">
		<html:checkbox property="commonCartridge.runAuto" styleId="run-auto"/>
		<fmt:message key="label.authoring.advance.run.content.auto" />
	</label>
</div>

<div class="select">
	<html:select property="commonCartridge.miniViewCommonCartridgeNumber"
			styleId="viewNumList" styleClass="form-control input-sm form-control-inline roffset5">
		<c:forEach begin="1" end="${fn:length(commonCartridgeList)}" varStatus="status">
			<c:choose>
				<c:when test="${formBean.commonCartridge.miniViewCommonCartridgeNumber == status.index}">
					<option value="${status.index}" selected="true">
						${status.index}
					</option>
				</c:when>
				<c:otherwise>
					<option value="${status.index}">
						${status.index}
					</option>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</html:select>

	<label for="viewNumList">
	<fmt:message key="label.authoring.advance.mini.number.resources.view" />
	</label>
</div>

</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${formBean.commonCartridge.contentId}" />

<lams:SimplePanel titleKey="label.activity.completion">

<div class="checkbox">
	<label for="lock-when-finished">
		<html:checkbox property="commonCartridge.lockWhenFinished" styleId="lock-when-finished"/>
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</label>
</div>

<div class="checkbox">
	<label for="reflect-on-activity">
		<html:checkbox property="commonCartridge.reflectOnActivity" styleId="reflect-on-activity"/>
		<fmt:message key="label.authoring.advanced.reflectOnActivity" />
	</label>
</div>

<div class="form-group">
	<html:textarea property="commonCartridge.reflectInstructions" rows="3" styleId="reflect-instructions" styleClass="form-control"/>
</div>

</lams:SimplePanel>

<script type="text/javascript">
	//automatically turn on refect option if there are text input in refect instruction area
	var ra = document.getElementById("reflect-instructions");
	var rao = document.getElementById("reflect-on-activity");
	function turnOnRefect() {
		if (isEmpty(ra.value)) {
		//turn off	
			rao.checked = false;
		} else {
		//turn on
			rao.checked = true;		
		}
	}

	ra.onkeyup=turnOnRefect;
</script>
