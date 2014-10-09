<h1>
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="label.monitoring.summary.advanced.settings"/>
	</a>
</h1>
<br />

<%-- Overall TaskList information  --%>
<div class="monitoring-advanced" id="advancedDiv" style="display:none">
	<table class="alternative-color">
		<tr>
			<td>
				<fmt:message key="label.monitoring.summary.lock.when.finished" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${spreadsheet.lockWhenFinished}">
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
				<fmt:message key="label.monitoring.summary.individual.spreadsheets" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${spreadsheet.learnerAllowedToSave}">
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
				<fmt:message key="label.monitoring.summary.marking.enabled" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${spreadsheet.markingEnabled}">
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
				<fmt:message key="label.monitoring.summary.notebook.reflection" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${spreadsheet.reflectOnActivity}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>	
		</tr>
		<c:if test="${spreadsheet.reflectOnActivity}">
		<tr>
			<td>
				<fmt:message key="label.monitoring.summary.notebook.reflection" />
			</td>
			<td>
				<lams:out value="${spreadsheet.reflectInstructions}" escapeHtml="true"/>
			</td>	
		</tr>
		</c:if>		
	</table>
</div>
