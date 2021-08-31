<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
             
<table class="table table-striped table-condensed">
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.lock.on.finished" />
				<lams:Popover>
					<fmt:message key="label.advanced.lockOnFinished.tip.1" /><br>
				</lams:Popover>
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.survey.lockWhenFinished}">
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
				<fmt:message key="label.authoring.advance.show.on.one.page" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.survey.showOnePage}">
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
				<fmt:message key="label.show.answers.from.other.users" />
				<lams:Popover>
					<fmt:message key="label.show.answers.from.other.users.tip.1" /><br>
				</lams:Popover>
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.survey.showOtherUsersAnswers}">
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
				<fmt:message key="label.authoring.advanced.notify.onanswersubmit" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.survey.notifyTeachersOnAnswerSumbit}">
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
					<c:when test="${sessionMap.survey.reflectOnActivity}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<c:choose>
			<c:when test="${sessionMap.survey.reflectOnActivity}">
				<tr>
					<td>
						<fmt:message key="monitor.summary.td.notebookInstructions" />
					</td>
					<td>
						<lams:out value="${sessionMap.survey.reflectInstructions}" escapeHtml="true"/>
					</td>
				</tr>
			</c:when>
		</c:choose>
	</table>
</lams:AdvancedAccordian>