<%@ include file="/common/taglibs.jsp"%>

<table cellspacing="0">
	
	<tr>
		<td colspan="3">
			<div class="field-name">
				<fmt:message key="instructions.onlineInstructions" />
			</div>	
			<div style="padding-left: 20px;">
				<c:out value="${kaltura.onlineInstructions}" escapeXml="false" />
			</div>
		</td>
	</tr>
	
	<c:if test="${not empty kaltura.onlineFileList}">
		<tr>
			<td  colspan="3" class="field-name-alternative-color">
				<fmt:message key="label.attachments" />
			</td>
		</tr>		
	</c:if>
		
	<c:forEach var="file" items="${kaltura.onlineFileList}">
		<tr >
			<td width="7%" style="padding-left: 30px;">			
				<c:out value="${file.fileName}" />
			</td>
			
			<td width="3%" align="left">
				<c:set var="viewURL">
					<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false" />
				</c:set>
				<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')" class="button"> <fmt:message key="link.view" /> </a> &nbsp;
			</td>
			
			<td width="5%" align="left">
				<c:set var="downloadURL">
					<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
				</c:set>
				<a href="<c:out value='${downloadURL}' escapeXml='false'/>" class="button"> <fmt:message key="link.download" /> </a>
			</td>
		</tr>
	</c:forEach>

	
	<tr>
		<td colspan="3">
			<hr />
		</td>
	</tr>
	
	<tr>
		<td colspan="3">
			<div class="field-name">
				<fmt:message key="instructions.offlineInstructions" />
			</div>	
			<div style="padding-left: 20px;">
				<c:out value="${kaltura.offlineInstructions}" escapeXml="false" />
			</div>
		</td>
	</tr>
	
	<c:if test="${not empty kaltura.offlineFileList}">
		<tr>
			<td  colspan="3" class="field-name-alternative-color">
				<fmt:message key="label.attachments" />
			</td>
		</tr>
	</c:if>		
		
	<c:forEach var="file" items="${kaltura.offlineFileList}">
		<tr >
			<td width="7%" style="padding-left: 30px;">			
				<c:out value="${file.fileName}" />
			</td>
			
			<td width="3%" align="left">
				<c:set var="viewURL">
					<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false" />
				</c:set>
				<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')" class="button"> <fmt:message key="link.view" /> </a> &nbsp;
			</td>
			
			<td width="5%" align="left">
				<c:set var="downloadURL">
					<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
				</c:set>
				<a href="<c:out value='${downloadURL}' escapeXml='false'/>" class="button"> <fmt:message key="link.download" /> </a>
			</td>
		</tr>
	</c:forEach>
	
</table>
