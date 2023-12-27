<%@ include file="/common/taglibs.jsp"%>


<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.task.options">
	
	<div class="checkbox">
		<label for="sequentialOrder">
		<form:checkbox path="taskList.sequentialOrder" id="sequentialOrder"/>
		<fmt:message key="label.authoring.advance.run.content.sequentialOrder" />
		</label>
	</div>
	
	<div class="form-group">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	
		<form:select path="taskList.minimumNumberTasks"	id="minimumNumberTasks" cssClass="form-control form-control-inline input-sm">
			<c:forEach begin="1" end="${fn:length(sessionMap.taskListList)}" varStatus="status">
				<c:choose>
					<c:when	test="${taskListForm.taskList.minimumNumberTasks == status.index}">
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
	
		<label for="minimumNumberTasks">
			<fmt:message key="label.authoring.advance.minimum.number.tasks" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="allowContributeTasks">
		<form:checkbox path="taskList.allowContributeTasks" id="allowContributeTasks" />
		<fmt:message key="label.authoring.advance.allow.contribute.tasks" />
		</label>
	</div>
		
	<div class="checkbox">
		<label for="monitorVerificationRequired">
		<form:checkbox path="taskList.monitorVerificationRequired" id="monitorVerificationRequired" />
		<fmt:message key="label.authoring.advance.monitor.verification.required" />
		</label>
	</div>

</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${taskListForm.taskList.contentId}" />
	
<lams:SimplePanel titleKey="label.activity.completion">
	<div class="checkbox">
		<label for="lockWhenFinished">
		<form:checkbox path="taskList.lockWhenFinished"	id="lockWhenFinished" />
		<fmt:message key="label.authoring.advance.lock.on.finished" />
		</label>
	</div>
</lams:SimplePanel>
