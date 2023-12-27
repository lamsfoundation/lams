<%@ include file="/common/taglibs.jsp"%>

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
             
<table class="table table-striped table-condensed">
	<tr>
		<td>
			<fmt:message key="label.learner.answer" />
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
			<fmt:message key="label.use.select.leader.tool.output" />
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