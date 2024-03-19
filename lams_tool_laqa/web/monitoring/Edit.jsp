<%@ include file="/common/taglibs.jsp"%>

<c:set var="adTitle"><fmt:message key="label.editActivity" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">

<form id='define-later-form' method='post' action='../authoring/definelater.do' target='definelater'>
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
	<input type="hidden" name="toolContentID" value="${content.qaContentId}" />
	<input type="hidden" name="contentFolderID" value="${contentFolderID}" />
</form>

<button type="button" onclick="launchDefineLaterPopup()" class="btn btn-secondary btn-icon-pen float-end m-3">
	<fmt:message key="label.edit" />
</button>
             
<table class="table table-striped table-condensed">
	<tr>
		<td class="field-name" valign="top">
			<fmt:message key="label.authoring.title" />
		</td>
		
		<td>
			<c:out value="${content.title}" escapeXml="true" />
		</td>
	</tr>

	<tr>
		<td class="field-name" valign="top" NOWRAP>
			<fmt:message key="label.authoring.instructions" />
		</td>
		
		<td>
			<c:out value="${content.instructions}" escapeXml="false" />
		</td>
	</tr>
	
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
