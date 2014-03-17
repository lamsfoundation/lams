<%@ include file="/common/taglibs.jsp"%>

<h1>
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
			<fmt:message key="label.authoring.advance.lock.on.finished" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.resource.lockWhenFinished == true}">
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
			<fmt:message key="label.authoring.advance.run.content.auto" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.resource.runAuto == true}">
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
			<fmt:message key="label.authoring.advance.mini.number.resources.view" />
		</td>
		
		<td>
			${sessionMap.resource.miniViewResourceNumber}
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.authoring.advance.allow.learner.add.urls" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.resource.allowAddUrls == true}">
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
			<fmt:message key="label.authoring.advance.allow.learner.add.files" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.resource.allowAddFiles == true}">
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
			<fmt:message key="label.authoring.advanced.notify.onassigmentsubmit" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.resource.notifyTeachersOnAssigmentSumbit == true}">
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
			<fmt:message key="monitor.summary.td.addNotebook" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.resource.reflectOnActivity == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${sessionMap.resource.reflectOnActivity == true}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>
				<td>
					${sessionMap.resource.reflectInstructions}	
				</td>
			</tr>
		</c:when>
	</c:choose>
</table>
</div>