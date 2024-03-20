<%@ include file="/common/taglibs.jsp"%>
<c:set var="dto" value="${mindmapDTO}" />

<c:set var="adTitle"><fmt:message key="button.editActivity" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
	<c:if test="${dto.contentInUse}">
		<lams:Alert5 type="warn" id="alertContentEdit">
			<fmt:message key="message.alertContentEdit" />
		</lams:Alert5>
	</c:if>
	
	
		<form id='define-later-form' method='post' action='../authoring/definelater.do' target='definelater'>
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<input type="hidden" name="toolContentID" value="${dto.toolContentId}" />
			<input type="hidden" name="contentFolderID" value="${contentFolderID}" />
		</form>
	
		<button type="button" onclick="launchDefineLaterPopup()" class="btn btn-secondary btn-icon-pen float-end m-3">
			<fmt:message key="button.editActivity" />
		</button>
	

	<table class="table table-condensed table-striped mb-0">
		<tr>
			<td>
				<fmt:message key="label.authoring.basic.title" />
			</td>
			
			<td>
				<c:out value="${dto.title}" escapeXml="true" />
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.basic.instructions" />
			</td>
			
			<td>
				<c:out value="${dto.instructions}" escapeXml="false" />
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="advanced.lockOnFinished" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${dto.lockOnFinish}">
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
				<fmt:message key="advanced.multiUserMode" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${dto.multiUserMode}">
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
