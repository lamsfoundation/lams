<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
             
<table class="table table-striped table-condensed">
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
			<fmt:message key="label.authoring.advanced.discussion" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.scratchie.discussionSentimentEnabled}">
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
			<fmt:message key="label.authoring.advanced.question.etherpad" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.scratchie.questionEtherpadEnabled}">
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
			<fmt:message key="label.authoring.advanced.reveal.double.click" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.scratchie.revealOnDoubleClick}">
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
			<fmt:message key="label.authoring.advanced.show.scratchies.in.results" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.scratchie.showScrachiesInResults}">
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
			<fmt:message key="admin.preset.marks" />
		</td>
		
		<td>
			<c:out value="${sessionMap.scratchie.presetMarks}" />
		</td>
	</tr>
</table>
</lams:AdvancedAccordian>