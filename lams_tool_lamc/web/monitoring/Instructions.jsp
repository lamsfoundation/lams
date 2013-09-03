<%@ include file="/common/taglibs.jsp"%>
<html:errors />

<table cellspacing="0">
	
	<tr>
		<td colspan="3">
			<div class="field-name">
				<fmt:message key="label.onlineInstructions.col" />
			</div>	
			<div style="padding-left: 20px;">
				<c:out value="${mcGeneralMonitoringDTO.onlineInstructions}" escapeXml="false" />
			</div>
		</td>
	</tr>
	
	<c:set var="isOnlineListEmpty" value="${true}" />	
	<c:forEach var="file" items="${mcGeneralMonitoringDTO.attachmentList}">
		<c:if test="${file.fileOnline}">
		 	<c:set var="isOnlineListEmpty" value="${false}" />
		</c:if>
	</c:forEach>
	<c:if test="${not isOnlineListEmpty}">
		<tr>
			<td  colspan="3" class="field-name-alternative-color">
				<fmt:message key="label.attachments" />
			</td>
		</tr>		
	</c:if>
		
	<c:forEach var="file" items="${mcGeneralMonitoringDTO.attachmentList}">
		<c:if test="${file.fileOnline}"> 	
			<tr >
				<td width="7%" style="padding-left: 30px;">			
					<c:out value="${file.fileName}" />
				</td>
				
				<td width="3%" align="left">
					<c:set var="viewURL">
						<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=false" />
					</c:set>
					<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')" class="button"> <fmt:message key="label.view" /> </a> &nbsp;
				</td>
				
				<td width="5%" align="left">
					<c:set var="downloadURL">
						<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=true" />
					</c:set>
					<a href="<c:out value='${downloadURL}' escapeXml='false'/>" class="button"> <fmt:message key="label.download" /> </a>
				</td>
			</tr>
		</c:if>
	</c:forEach>

	
	<tr>
		<td colspan="3">
			<hr />
		</td>
	</tr>
	
	<tr>
		<td colspan="3">
			<div class="field-name">
				<fmt:message key="label.offlineInstructions.col" />
			</div>	
			<div style="padding-left: 20px;">
				<c:out value="${mcGeneralMonitoringDTO.offlineInstructions}" escapeXml="false" />
			</div>
		</td>
	</tr>

	<c:set var="isOfflineListEmpty" value="${true}" />	
	<c:forEach var="file" items="${mcGeneralMonitoringDTO.attachmentList}">
		<c:if test="${not file.fileOnline}">
		 	<c:set var="isOfflineListEmpty" value="${false}" />
		</c:if>
	</c:forEach>
	<c:if test="${not isOfflineListEmpty}">
		<tr>
			<td  colspan="3" class="field-name-alternative-color">
				<fmt:message key="label.attachments" />
			</td>
		</tr>
	</c:if>		
		
	<c:forEach var="file" items="${mcGeneralMonitoringDTO.attachmentList}">
		<c:if test="${not file.fileOnline}"> 
			<tr >
				<td width="7%" style="padding-left: 30px;">			
					<c:out value="${file.fileName}" />
				</td>
				
				<td width="3%" align="left">
					<c:set var="viewURL">
						<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=false" />
					</c:set>
					<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')" class="button"> <fmt:message key="label.view" /> </a> &nbsp;
				</td>
				
				<td width="5%" align="left">
					<c:set var="downloadURL">
						<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=true" />
					</c:set>
					<a href="<c:out value='${downloadURL}' escapeXml='false'/>" class="button"> <fmt:message key="label.download" /> </a>
				</td>
			</tr>
		</c:if>
	</c:forEach>
	
</table>