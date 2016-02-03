<h1 style="padding-bottom: 10px;">
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.th.advancedSettings" />
	</a>
</h1>
<br />

<div class="monitoring-advanced" id="advancedDiv" style="display:none">
<table class="alternative-color">

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
	
	<tr>
		<td>
			<fmt:message key="label.monitoring.summary.notebook.reflection" ><fmt:param> </fmt:param></fmt:message>
		</td>
		<td>
			<c:choose>
				<c:when test="${taskList.reflectOnActivity}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
</table>
</div>