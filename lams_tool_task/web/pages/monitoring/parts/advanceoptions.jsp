<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
             
<table class="table table-striped table-condensed">
	<tr>
		<td>
			<fmt:message key="label.monitoring.summary.lock.when.finished" ><fmt:param> </fmt:param></fmt:message>
		</td>
		<td>
			<c:choose>
				<c:when test="${taskList.lockWhenFinished}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.monitoring.summary.sequential.order" ><fmt:param> </fmt:param></fmt:message>
		</td>
		<td>
			<c:choose>
				<c:when test="${taskList.sequentialOrder}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.monitoring.summary.min.number.tasks" ><fmt:param> </fmt:param></fmt:message>
		</td>
		<td>
			<c:choose>
				<c:when test="${taskList.minimumNumberTasks > 0}">
					${taskList.minimumNumberTasks}
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.monitoring.summary.allowed.contribute.tasks" ><fmt:param> </fmt:param></fmt:message>
		</td>
		<td>
			<c:choose>
				<c:when test="${taskList.allowContributeTasks}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.monitoring.summary.monitor.verification" ><fmt:param> </fmt:param></fmt:message>
		</td>
		<td>
			<c:choose>
				<c:when test="${taskList.monitorVerificationRequired}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
</table>
</lams:AdvancedAccordian>