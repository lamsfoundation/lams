<h1 style="padding-bottom: 10px;">
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.th.advancedSettings" />
	</a>
</h1>
<br />

<div class="monitoring-advanced" id="advancedDiv" style="display:none">
<table class="alternative-color">

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
		<td>
			<fmt:message key="monitor.summary.td.addNotebook" />
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

<c:choose>
	<c:when test="${mcGeneralMonitoringDTO.displayAnswers == 'true'}">
		<p>
			<fmt:message key="label.monitoring.yesDisplayAnswers"/>
		</p>
	</c:when>
	<c:when test="${mcGeneralMonitoringDTO.displayAnswers == 'false'}">
		<p>
			<fmt:message key="label.monitoring.noDisplayAnswers1"/>
			<br>
			
			<quote>
				<fmt:message key="label.monitoring.noDisplayAnswers2"/>
				<input onclick="javascript:submitChangeDisplayAnswers(this.value, 'displayAnswers');" class="button" name="displayAnswers" class="button" value="<fmt:message key='button.monitoring.noDisplayAnswers'/>" type="button">	
			</quote>
		</p>
	</c:when>
</c:choose>

</div>
