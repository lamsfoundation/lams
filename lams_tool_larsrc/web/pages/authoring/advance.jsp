<%@ include file="/common/taglibs.jsp"%>

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.resource.options">

	<div class="checkbox">
	<label for="runAuto"><form:checkbox path="resource.runAuto" id="runAuto" />
	<fmt:message key="label.authoring.advance.run.content.auto" /></label>
	</div>
	
	<div class="form-group">
	<form:select path="resource.miniViewResourceNumber" cssClass="form-control form-control-inline input-sm"
		id="viewNumList" style="width:100px">
		<c:forEach begin="1" end="${fn:length(resourceList)}"
			varStatus="status">
			<c:choose>
				<c:when
					test="${authoringForm.resource.miniViewResourceNumber == status.index}">
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
	</form:select>
	<label for="miniViewResourceNumber">&nbsp;<fmt:message key="label.authoring.advance.mini.number.resources.view" /></label>
	</div>
	
	<div class="checkbox">
	<label for="allowAddUrls"><form:checkbox path="resource.allowAddUrls" id="allowAddUrls"/>
	<fmt:message key="label.authoring.advance.allow.learner.add.urls" />
	</label>
	</div>
	
	<div class="checkbox">
	<label for="allowAddFiles"><form:checkbox path="resource.allowAddFiles" id="allowAddFiles"/>
	<fmt:message key="label.authoring.advance.allow.learner.add.files" />
	</label>
	</div>

</lams:SimplePanel>

<lams:SimplePanel titleKey="label.notifications">

	<div class="checkbox">
	<label for="notifyTeachersOnAssigmentSumbit">
	<form:checkbox path="resource.notifyTeachersOnAssigmentSumbit" id="notifyTeachersOnAssigmentSumbit"/>
	<fmt:message key="label.authoring.advanced.notify.onassigmentsubmit" />
	</label>
	</div>
	
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${authoringForm.resource.contentId}" />

<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox">
	<label for="lockWhenFinished"><form:checkbox path="resource.lockWhenFinished" id="lockWhenFinished" />
	<fmt:message key="label.authoring.advance.lock.on.finished" /></label>
	</div>
	
	<div class="checkbox">
	<label for="reflectOn"><form:checkbox path="resource.reflectOnActivity" id="reflectOn"/>
	<fmt:message key="label.authoring.advanced.reflectOnActivity" />
	</label>
	</div>
	
	<div class="form-group">
	<form:textarea path="resource.reflectInstructions" cssClass="form-control" id="reflectInstructions" rows="3" />
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
