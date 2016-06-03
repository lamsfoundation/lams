<%@ include file="/common/taglibs.jsp"%>

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">

	<table class="table table-condensed">

		<tr>
			<td>
				<fmt:message key="advanced.lockOnFinished" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${kaltura.lockOnFinished}">
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
				<fmt:message key="advanced.allowContributeVideos" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${kaltura.allowContributeVideos}">
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
				<fmt:message key="advanced.allowSeeingOtherUsersRecordings" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${kaltura.allowSeeingOtherUsersRecordings}">
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
				<fmt:message key="advanced.learnerContributionLimit" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${kaltura.learnerContributionLimit == -1}">
						<fmt:message key="advanced.unlimited" />
					</c:when>
					<c:otherwise>
						${kaltura.learnerContributionLimit}
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="advanced.allowComments" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${kaltura.allowComments}">
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
				<fmt:message key="advanced.allowRatings" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${kaltura.allowRatings}">
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
					<c:when test="${kaltura.reflectOnActivity == true}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
			
		<c:choose>
			<c:when test="${kaltura.reflectOnActivity == true}">
				<tr>
					<td>
						<fmt:message key="monitor.summary.td.notebookInstructions" />
					</td>
					<td>
						<lams:out value="${kaltura.reflectInstructions}" escapeHtml="true"/>
					</td>
				</tr>
			</c:when>
		</c:choose>

	</table>
</lams:AdvancedAccordian>
