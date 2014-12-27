<%@ include file="/common/taglibs.jsp" %>

<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<input type="hidden" name="hasAttachment" value="${topic.hasAttachment}"/>
<c:if test="${topic.hasAttachment}">
	<table border="0" class="align-left" style="width:400px">
		<c:forEach var="file" items="${topic.message.attachments}">
			<tr>
				<td>
					<fmt:message key="message.label.attachment"/>: 
					<c:set var="downloadURL">
						<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
					</c:set>
					<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <c:out value="${file.fileName}" /> </a>
				</td>
				
				<td>		
					<a href="#" onclick="removeAtt('${sessionMapID}')" class="button"> <fmt:message key="label.delete" /> </a>
				</td>
				
				<td>		
					<img src="${ctxPath}/images/indicator.gif" style="display:none" id="itemAttachmentArea_Busy" />
				</td>
			</tr>
		</c:forEach>
	</table>
</c:if>

<c:if test="${not topic.hasAttachment && allowUpload}">
	<input type="file" name="attachmentFile" size="55"  /> 
</c:if>
