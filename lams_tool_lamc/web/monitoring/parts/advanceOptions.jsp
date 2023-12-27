<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
             
<table class="table table-striped table-condensed">
	<tr>
		<td>
			<fmt:message key="label.use.select.leader.tool.output" />
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
			<fmt:message key="label.prefix.sequential.letters.for.each.answer" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${prefixAnswersWithLetters}">
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
			<fmt:message key="radiobox.onepq" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${questionsSequenced}">
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
			<fmt:message key="label.showMarks" />
		</td>
		<td>
			<c:choose>
				<c:when test="${showMarks}">
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
			<fmt:message key="label.randomize" />
		</td>
		<td>	
			<c:choose>
				<c:when test="${randomize}">
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
			<fmt:message key="label.displayAnswers" />
		</td>	
		<td>
			<c:choose>
				<c:when test="${displayAnswers}">
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
			<fmt:message key="label.displayFeedbackOnly" />
		</td>	
		<td>
			<c:choose>
				<c:when test="${displayFeedbackOnly}">
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
			<fmt:message key="radiobox.retries" />
		</td>
		<td>	
			<c:choose>
				<c:when test="${retries}">
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
			<fmt:message key="radiobox.passmark" />
		</td>
		<td>	
			${passMark}
		</td>
	</tr>

	<tr>
		<td colspan="2">
		<c:choose>
			<c:when test="${mcGeneralMonitoringDTO.displayAnswers == 'true'}">
				<fmt:message key="label.monitoring.yesDisplayAnswers"/>
			</c:when>
			<c:when test="${mcGeneralMonitoringDTO.displayAnswers == 'false' && mcGeneralMonitoringDTO.displayFeedbackOnly == 'true'}">
				<fmt:message key="label.monitoring.yesDisplayFeedbackOnly"/><br/>
				<quote>
					<fmt:message key="label.monitoring.noDisplayAnswers2"/>&nbsp;
					<input onclick="javascript:turnOnDisplayAnswers();" name="displayAnswers" 
							class="btn btn-default btn-xs" value="<fmt:message key='button.monitoring.yes'/>" type="button">	
				</quote>
			</c:when>
			<c:when test="${mcGeneralMonitoringDTO.displayAnswers == 'false' && mcGeneralMonitoringDTO.displayFeedbackOnly == 'false'}">
				<fmt:message key="label.monitoring.noDisplayAnswers1"/><br/>
				<quote>
					<fmt:message key="label.monitoring.noDisplayAnswers2"/>&nbsp;
					<input onclick="javascript:turnOnDisplayAnswers();" name="displayAnswers" 
							class="btn btn-default btn-xs" value="<fmt:message key='button.monitoring.yes'/>" type="button">	
				</quote><br/>
				<quote>
					<fmt:message key="label.monitoring.noDisplayFeedbackOnly2"/>&nbsp;
					<input onclick="javascript:turnOnDisplayFeedbackOnly();" name="displayFeedbackOnly" 
							class="btn btn-default btn-xs" value="<fmt:message key='button.monitoring.yes'/>" type="button">	
				</quote>
			</c:when>
		</c:choose>
		</td>
	</tr>

</table>

</lams:AdvancedAccordian>