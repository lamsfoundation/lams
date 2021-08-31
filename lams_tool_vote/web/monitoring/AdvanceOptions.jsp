<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
             
<table class="table table-striped table-condensed">

	<tr>
		<td>
			<fmt:message key="label.use.select.leader.tool.output" />
			<lams:Popover>
				<fmt:message key="label.use.select.leader.tool.output.tip.1" /><br>
			 </lams:Popover>
		</td>
		
		<td>
			<c:choose>
				<c:when test="${useSelectLeaderToolOuput}">
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
			<fmt:message key="label.vote.lockedOnFinish" />
			 <lams:Popover>
				<fmt:message key="label.advanced.lockOnFinished.tip.1" /><br>
			 </lams:Popover>
		</td>
		<td>
			<c:choose>
				<c:when test="${lockOnFinish}">
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
			<fmt:message key="label.allowText" />
		</td>
		<td>
			<c:choose>
				<c:when test="${allowText}">
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
			<fmt:message key="label.maxNomCount" />
		</td>
		<td>	
			${maxNominationCount}
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.minNomCount" />
		</td>
		<td>	
			${minNominationCount}
		</td>
	</tr>	
	
	<tr>
		<td>
			<fmt:message key="label.show.results" />
			<lams:Popover>
	            <fmt:message key="label.show.results.tip.1" /><br>
	         </lams:Popover>
		</td>	
		<td>
			<c:choose>
				<c:when test="${showResults}">
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
				<c:when test="${reflect}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${reflect}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>
				<td>	
					<lams:out value="${reflectionSubject}" escapeHtml="true"/>
				</td>
			</tr>
		</c:when>
	</c:choose>
</table>

</lams:AdvancedAccordian>