<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Advance Tab Content -->

<p class="small-space-top">
	<html:checkbox property="taskList.lockWhenFinished"	styleClass="noBorder" styleId="lockWhenFinished" />
	<label for="lockWhenFinished">
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</label>
</p>

<p>
	<html:checkbox property="taskList.sequentialOrder" styleClass="noBorder" styleId="sequentialOrder"/>
	<label for="sequentialOrder">
		<fmt:message key="label.authoring.advance.run.content.sequentialOrder" />
	</label>
</p>

<p>
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<html:select property="taskList.minimumNumberTasksComplete"	styleId="minimumNumberTasksComplete" style="width:50px">
<!-- 		<c:forEach begin="1" end="${fn:length(sessionMap.taskListList)}" varStatus="status">
			<c:choose>
				<c:when	test="${formBean.taskList.minimumNumberTasksComplete == status.index}">
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

	<label for="minimumNumberTasksComplete">
		<fmt:message key="label.authoring.advance.minimum.number.tasks" />
	</label>
</p>

<p>
	<html:checkbox property="taskList.allowContributeTasks" styleClass="noBorder" styleId="allowContributeTasks" />
	<label for="allowContributeTasks">
		<fmt:message key="label.authoring.advance.allow.contribute.tasks" />
	</label>
</p>

<p>
	<html:checkbox property="taskList.monitorVerificationRequired" styleClass="noBorder" styleId="monitorVerificationRequired" />
	<label for="monitorVerificationRequired">
		<fmt:message key="label.authoring.advance.monitor.verification.required" />
	</label>
</p>

<p>
	<html:checkbox property="taskList.reflectOnActivity" styleClass="noBorder" styleId="reflectOn" />
	<label for="reflectOn">
		<fmt:message key="label.authoring.advanced.reflectOnActivity" />
	</label>
</p>

<p>
	<html:textarea property="taskList.reflectInstructions"	styleId="reflectInstructions" cols="30" rows="3" />
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
