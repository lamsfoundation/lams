<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean"
	value="<%=request
									.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Advance Tab Content -->

<p class="small-space-top">
	<html:checkbox property="resource.lockWhenFinished"
		styleClass="noBorder" styleId="lockWhenFinished">
	</html:checkbox>

	<label for="lockWhenFinished">
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</label>
</p>

<p>
	<html:checkbox property="resource.runAuto" styleClass="noBorder" styleId="runAuto">
	</html:checkbox>
	<label for="runAuto">
		<fmt:message key="label.authoring.advance.run.content.auto" />
	</label>
</p>

<p>
	<html:select property="resource.miniViewResourceNumber"
		styleId="viewNumList" style="width:50px">
		<c:forEach begin="1" end="${fn:length(resourceList)}"
			varStatus="status">
			<c:choose>
				<c:when
					test="${formBean.resource.miniViewResourceNumber == status.index}">
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

	<fmt:message key="label.authoring.advance.mini.number.resources.view" />
</p>

<p>
	<html:checkbox property="resource.allowAddUrls" styleClass="noBorder" styleId="allowAddUrls">
	</html:checkbox>
	<label for="allowAddUrls">
		<fmt:message key="label.authoring.advance.allow.learner.add.urls" />
	</label>
</p>

<p>
	<html:checkbox property="resource.allowAddFiles" styleClass="noBorder" styleId="allowAddFiles">
	</html:checkbox>
	<label for="allowAddFiles">
		<fmt:message key="label.authoring.advance.allow.learner.add.files" />
	</label>
</p>

<p>
	<html:checkbox property="resource.notifyTeachersOnAssigmentSumbit"
		styleClass="noBorder" styleId="notifyTeachersOnAssigmentSumbit">
	</html:checkbox>
	<label for="notifyTeachersOnAssigmentSumbit">
		<fmt:message key="label.authoring.advanced.notify.onassigmentsubmit" />
	</label>
</p>

<p>
	<html:checkbox property="resource.notifyTeachersOnFileUpload"
		styleClass="noBorder" styleId="notifyTeachersOnFileUpload">
	</html:checkbox>
	<label for="notifyTeachersOnFileUpload">
		<fmt:message key="label.authoring.advanced.notify.onfileupload" />
	</label>
</p>

<p>
	<html:checkbox property="resource.reflectOnActivity"
		styleClass="noBorder" styleId="reflectOn">
	</html:checkbox>
	<label for="reflectOn">
		<fmt:message key="label.authoring.advanced.reflectOnActivity" />
	</label>
</p>

<p>
	<html:textarea property="resource.reflectInstructions"
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
