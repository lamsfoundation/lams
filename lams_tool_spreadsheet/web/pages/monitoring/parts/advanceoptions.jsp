<c:set var="adTitle"><fmt:message key="label.monitoring.summary.advanced.settings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">

<%-- Overall TaskList information  --%>
             
<table class="table table-striped table-condensed">
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
	</table>

</lams:AdvancedAccordian>