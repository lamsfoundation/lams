<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.resource.options">

	<div class="checkbox">
	<label for="runAuto"><html:checkbox property="resource.runAuto" styleId="runAuto" />
	<fmt:message key="label.authoring.advance.run.content.auto" /></label>
	</div>
	
	<div class="form-group">
	<html:select property="resource.miniViewResourceNumber" styleClass="form-control form-control-inline input-sm"
		styleId="viewNumList" style="width:100px">
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
	<label for="miniViewResourceNumber">&nbsp;<fmt:message key="label.authoring.advance.mini.number.resources.view" /></label>
	</div>
	
	<div class="checkbox">
	<label for="allowAddUrls"><html:checkbox property="resource.allowAddUrls" styleId="allowAddUrls"/>
	<fmt:message key="label.authoring.advance.allow.learner.add.urls" />
	</label>
	</div>
	
	<div class="checkbox">
	<label for="allowAddFiles"><html:checkbox property="resource.allowAddFiles" styleId="allowAddFiles"/>
	<fmt:message key="label.authoring.advance.allow.learner.add.files" />
	</label>
	</div>

</lams:SimplePanel>

<lams:SimplePanel titleKey="label.notifications">

	<div class="checkbox">
	<label for="notifyTeachersOnAssigmentSumbit">
	<html:checkbox property="resource.notifyTeachersOnAssigmentSumbit" styleId="notifyTeachersOnAssigmentSumbit"/>
	<fmt:message key="label.authoring.advanced.notify.onassigmentsubmit" />
	</label>
	</div>
	
	<div class="checkbox">
	<label for="notifyTeachersOnFileUpload">
	<html:checkbox property="resource.notifyTeachersOnFileUpload" styleId="notifyTeachersOnFileUpload"/>
	<fmt:message key="label.authoring.advanced.notify.onfileupload" />
	</label>
	</div>
	
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${formBean.resource.contentId}" />

<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox">
	<label for="lockWhenFinished"><html:checkbox property="resource.lockWhenFinished" styleId="lockWhenFinished" />
	<fmt:message key="label.authoring.advance.lock.on.finished" /></label>
	</div>
	
	<div class="checkbox">
	<label for="reflectOn"><html:checkbox property="resource.reflectOnActivity" styleId="reflectOn"/>
	<fmt:message key="label.authoring.advanced.reflectOnActivity" />
	</label>
	</div>
	
	<div class="form-group">
	<html:textarea property="resource.reflectInstructions" styleClass="form-control" styleId="reflectInstructions" rows="3" />
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
