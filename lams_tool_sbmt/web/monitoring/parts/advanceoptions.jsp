<%@include file="/common/taglibs.jsp"%>
<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
             
<table class="table table-striped table-condensed">
	<tr>
		<td>
			<fmt:message key="label.use.select.leader.tool.output" />
			<lams:Popover>
				<fmt:message key="advanced.select.leader.tip.1" /><br>
			</lams:Popover>
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.useSelectLeaderToolOuput}">
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
			<fmt:message key="label.authoring.advance.lock.on.finished" />
			<lams:Popover>
	            <fmt:message key="label.advanced.lockOnFinished.tip.1" /><br>
	        </lams:Popover>
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.lockOnFinished}">
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
			<fmt:message key="label.enable.min.limit" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.minLimitUploadNumber != null}">
					<fmt:message key="label.on" />, ${content.minLimitUploadNumber}
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.limit.number.upload" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.limitUpload}">
					<fmt:message key="label.on" />, ${content.limitUploadNumber}
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.authoring.advanced.notify.mark.release" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.notifyLearnersOnMarkRelease}">
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
			<fmt:message key="label.authoring.advanced.notify.onfilesubmit" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.notifyTeachersOnFileSubmit}">
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
			<lams:Popover>
				<fmt:message key="label.advanced.reflectOnActivity.tip.1" /><br>
				<fmt:message key="label.advanced.reflectOnActivity.tip.2" />
			</lams:Popover>	
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.reflectOnActivity}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${content.reflectOnActivity}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>
				<td>
					<lams:out value="${content.reflectInstructions}" escapeHtml="true"/>	
				</td>
			</tr>
	</c:when>
	</c:choose>
</table>
</lams:AdvancedAccordian>	
