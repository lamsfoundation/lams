<c:set var="adTitle"><fmt:message key="label.editActivity" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">

<lams:Alert5 id="editWarning" type="warning">
    <fmt:message key="message.monitoring.edit.activity.warning" />
</lams:Alert5>

<form id='define-later-form' method='post' action='../authoring/definelater.do' target='definelater'>
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
	<input type="hidden" name="toolContentID" value="${toolContentID}" />
	<input type="hidden" name="contentFolderID" value="${contentFolderID}" />
</form>
	
<button type="button" onclick="launchDefineLaterPopup()" class="btn btn-secondary btn-icon-pen float-end me-2 mb-3">
	<fmt:message key="label.edit" />
</button>
             
<table class="table table-striped table-condensed">
	<tr>
		<td><fmt:message key="label.authoring.title.col"></fmt:message></td>
		<td><c:out value="${voteGeneralAuthoringDTO.activityTitle}" escapeXml="true"/></td>
	</tr>
						
	<tr>
		<td><fmt:message key="label.authoring.instructions.col"></fmt:message></td>
		<td><c:out value="${voteGeneralAuthoringDTO.activityInstructions}" escapeXml="false"/></td>
	</tr>
	
	<c:forEach items="${listQuestionDTO}" var="currentDTO" varStatus="status">
		<tr>
			<td>
				<fmt:message key="label.nomination" />:
			</td>
			<td>
				<c:out value="${currentDTO.question}" escapeXml="false"/> 
			</td>
		</tr>
	</c:forEach>

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
			<fmt:message key="label.vote.lockedOnFinish" />
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
</table>

</lams:AdvancedAccordian>