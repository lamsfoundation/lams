<%@ include file="/common/taglibs.jsp"%>

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
             
<table class="table table-striped table-condensed">
	<tr>
		<td>
			<fmt:message key="label.learner.answer" />
			<lams:Popover>
	            <fmt:message key="label.learner.answer.tip.1" /><br>
	        </lams:Popover>
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.showOtherAnswers}">
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
			<fmt:message key="label.show.names" />
			<lams:Popover>
	        	<fmt:message key="label.show.names.tip.1" /><br>
	        </lams:Popover>
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.usernameVisible}">
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
			<fmt:message key="label.notify.teachers.on.response.submit" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.notifyTeachersOnResponseSubmit}">
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
			<fmt:message key="label.authoring.allow.rate.answers" />
			<lams:Popover>
	            <fmt:message key="label.authoring.allow.rate.answers.tip.1" /><br>
	        </lams:Popover>
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.allowRateAnswers}">
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
				<c:when test="${content.reflect}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${content.reflect}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>	
				<td>
					<lams:out value="${content.reflectionSubject}" escapeHtml="true"/>
				</td>
			</tr>
		</c:when>
	</c:choose>
	
	<tr>
		<td>
			<fmt:message key="radiobox.questionsSequenced" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.questionsSequenced}">
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
			<fmt:message key="label.lockWhenFinished" />
			<lams:Popover>
				<fmt:message key="label.advanced.lockOnFinished.tip.1" /><br>
			</lams:Popover>
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.lockWhenFinished}">
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
			<fmt:message key="label.allowRichEditor" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.allowRichEditor}">
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
			<fmt:message key="label.no.reedit.allowed" />
			<lams:Popover>
	            <fmt:message key="label.no.reedit.allowed.tip.1" /><br>
	        </lams:Popover>
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.noReeditAllowed}">
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
			<fmt:message key="label.use.select.leader.tool.output" />
			<lams:Popover>
				<fmt:message key="label.use.select.leader.tool.output.tip.1" /><br>
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

</table>
</lams:AdvancedAccordian>