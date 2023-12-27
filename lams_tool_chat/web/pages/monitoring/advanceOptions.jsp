<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
             
<table class="table table-striped table-condensed">
	<tr>
		<td>
			<fmt:message key="advanced.lockOnFinished" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${monitoringDTO.lockOnFinish == true}">
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
			<fmt:message key="advanced.filteringEnabled" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${monitoringDTO.filteringEnabled == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${monitoringDTO.filteringEnabled == true}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.filteredWords" />
				</td>
				<td>
					<lams:out value="${monitoringDTO.filteredKeyWords}" escapeHtml="true"/>
				</td>
			</tr>
		</c:when>
	</c:choose>
</table>
</lams:AdvancedAccordian>