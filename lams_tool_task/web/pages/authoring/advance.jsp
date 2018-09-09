<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.task.options">
	
	<div class="checkbox">
		<label for="sequentialOrder">
		<html:checkbox property="taskList.sequentialOrder" styleId="sequentialOrder"/>
		<fmt:message key="label.authoring.advance.run.content.sequentialOrder" />
		</label>
	</div>
	
	<div class="form-group">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	
		<html:select property="taskList.minimumNumberTasks"	styleId="minimumNumberTasks" styleClass="form-control form-control-inline input-sm">
	<!-- 		<c:forEach begin="1" end="${fn:length(sessionMap.taskListList)}" varStatus="status">
				<c:choose>
					<c:when	test="${formBean.taskList.minimumNumberTasks == status.index}">
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
	 -->		
		</html:select>
	
		<label for="minimumNumberTasks">
			<fmt:message key="label.authoring.advance.minimum.number.tasks" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="allowContributeTasks">
		<html:checkbox property="taskList.allowContributeTasks" styleId="allowContributeTasks" />
		<fmt:message key="label.authoring.advance.allow.contribute.tasks" />
		</label>
	</div>
		
	<div class="checkbox">
		<label for="monitorVerificationRequired">
		<html:checkbox property="taskList.monitorVerificationRequired" styleId="monitorVerificationRequired" />
		<fmt:message key="label.authoring.advance.monitor.verification.required" />
		</label>
	</div>

</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${formBean.taskList.contentId}" />
	
<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox">
		<label for="lockWhenFinished">
		<html:checkbox property="taskList.lockWhenFinished"	styleId="lockWhenFinished" />
		<fmt:message key="label.authoring.advance.lock.on.finished" />
		</label>
	</div>

	<div class="checkbox">
		<label for="reflectOn">
		<html:checkbox property="taskList.reflectOnActivity" styleId="reflectOn" />
		<fmt:message key="label.authoring.advanced.reflectOnActivity" />
		</label>
	</div>
	
	<div class="form-group">
		<html:textarea property="taskList.reflectInstructions"	styleId="reflectInstructions" styleClass="form-control" rows="3" />
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
