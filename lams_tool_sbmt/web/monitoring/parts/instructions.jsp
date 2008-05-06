<%@ include file="/common/taglibs.jsp"%>

<table cellspacing="0">
	
	<tr>
		<td colspan="3">
			<div class="field-name">
				<fmt:message key="label.authoring.online.instruction" />
			</div>	
			<div style="padding-left: 20px;">
				<c:out value="${authoring.onlineInstruction}" escapeXml="false" />
			</div>
		</td>
	</tr>
	
	<c:if test="${not empty authoring.onlineFiles}">
		<tr>
			<td  colspan="3" class="field-name-alternative-color">
				<fmt:message key="monitoring.instructions.attachments" />
			</td>
		</tr>		
	</c:if>
		
	<c:forEach var="file" items="${authoring.onlineFiles}">
		<tr >
			<td width="7%" style="padding-left: 30px;">			
				<c:out value="${file.name}" />
			</td>
			
			<td width="3%" align="left">
				<c:set var="viewURL">
					<html:rewrite page="/download/?uuid=${file.uuID}&preferDownload=false" />
				</c:set>
				<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')" class="button"> <fmt:message key="label.view" /> </a> &nbsp;
			</td>
			
			<td width="5%" align="left">
				<c:set var="downloadURL">
					<html:rewrite page="/download/?uuid=${file.uuID}&versionID=${file.versionID}&preferDownload=true" />
				</c:set>
				<a href="<c:out value='${downloadURL}' escapeXml='false'/>" class="button"> <fmt:message key="label.download" /> </a>
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
				<fmt:message key="label.authoring.offline.instruction" />
			</div>	
			<div style="padding-left: 20px;">
				<c:out value="${authoring.offlineInstruction}" escapeXml="false" />
			</div>
		</td>
	</tr>
	
	<c:if test="${not empty authoring.offlineFiles}">
		<tr>
			<td  colspan="3" class="field-name-alternative-color">
				<fmt:message key="monitoring.instructions.attachments" />
			</td>
		</tr>
	</c:if>		
		
	<c:forEach var="file" items="${authoring.offlineFiles}">
		<tr >
			<td width="7%" style="padding-left: 30px;">			
				<c:out value="${file.name}" />
			</td>
			
			<td width="3%" align="left">
				<c:set var="viewURL">
					<html:rewrite page="/download/?uuid=${file.uuID}&preferDownload=false" />
				</c:set>
				<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')" class="button"> <fmt:message key="label.view" /> </a> &nbsp;
			</td>
			
			<td width="5%" align="left">
				<c:set var="downloadURL">
					<html:rewrite page="/download/?uuid=${file.uuID}&versionID=${file.versionID}&preferDownload=true" />
				</c:set>
				<a href="<c:out value='${downloadURL}' escapeXml='false'/>" class="button"> <fmt:message key="label.download" /> </a>
			</td>
		</tr>
	</c:forEach>
	
</table>
