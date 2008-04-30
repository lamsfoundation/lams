<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="tags-function" prefix="fn" %>

<table cellspacing="0">
	
	<tr>
		<td colspan="3">
			<div class="field-name">
				<fmt:message key="instructions.onlineInstructions" /> :
			</div>	
			<div style="padding-left: 20px;">
				<c:out value="${formBean.onlineInstructions}" escapeXml="false" />
			</div>
		</td>
	</tr>
	
	<c:if test="${fn:length(formBean.attachmentsList) > 0}">
		<tr>
			<td  colspan="3" class="field-name-alternative-color">
				<fmt:message key="label.attachments" />
			</td>
		</tr>		
	</c:if>
		
	<c:forEach var="file" items="${formBean.attachmentsList}">
		<c:if test="${file.onlineFile}">
			<tr >
				<td width="7%" style="padding-left: 30px;">			
					<c:out value="${file.filename}" />
				</td>
				
				<td width="3%" align="left">
					<c:set var="viewURL">
						<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=false" />
					</c:set>
					<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')" class="button"> <fmt:message key="link.view" /> </a> &nbsp;
				</td>
				
				<td width="5%" align="left">
					<c:set var="downloadURL">
						<html:rewrite page="/download/?uuid=${file.uuid}&versionID=${file.versionId}&preferDownload=true" />
					</c:set>
					<a href="<c:out value='${downloadURL}' escapeXml='false'/>" class="button"> <fmt:message key="link.download" /> </a>
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
				<fmt:message key="instructions.offlineInstructions" /> :
			</div>	
			<div style="padding-left: 20px;">
				<c:out value="${formBean.offlineInstructions}" escapeXml="false" />
			</div>
		</td>
	</tr>
	
	<c:if test="${fn:length(formBean.attachmentsList) > 0}">
		<tr>
			<td  colspan="3" class="field-name-alternative-color">
				<fmt:message key="label.attachments" />
			</td>
		</tr>
	</c:if>		
		
	<c:forEach var="file" items="${taskList.offlineFileList}">
		<c:if test="${not file.onlineFile}">
			<tr >
				<td width="7%" style="padding-left: 30px;">			
					<c:out value="${file.filename}" />
				</td>
				
				<td width="3%" align="left">
					<c:set var="viewURL">
						<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=false" />
					</c:set>
					<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')" class="button"> <fmt:message key="link.view" /> </a> &nbsp;
				</td>
				
				<td width="5%" align="left">
					<c:set var="downloadURL">
						<html:rewrite page="/download/?uuid=${file.uuid}&versionID=${file.versionId}&preferDownload=true" />
					</c:set>
					<a href="<c:out value='${downloadURL}' escapeXml='false'/>" class="button"> <fmt:message key="link.download" /> </a>
				</td>
			</tr>
		</c:if>
	</c:forEach>
	
</table>

