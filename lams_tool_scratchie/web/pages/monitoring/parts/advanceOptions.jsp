<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
             
<table class="table table-striped table-condensed">
	<tr>
		<td>
			<fmt:message key="label.authoring.advanced.give.extra.point" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.scratchie.extraPoint}">
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
			<fmt:message key="label.authoring.advanced.burning.questions" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.scratchie.burningQuestionsEnabled}">
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
			<fmt:message key="label.authoring.advanced.shuffle.items" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.scratchie.shuffleItems}">
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
				<c:when test="${sessionMap.scratchie.reflectOnActivity}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${sessionMap.scratchie.reflectOnActivity}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>
				<td>
					<lams:out value="${sessionMap.scratchie.reflectInstructions}" escapeHtml="true"/>	
				</td>
			</tr>
		</c:when>
	</c:choose>
</table>
</lams:AdvancedAccordian>