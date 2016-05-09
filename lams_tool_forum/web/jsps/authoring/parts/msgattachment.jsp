<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
<input type="hidden" name="hasAttachment" value="${itemAttachment.hasAttachment}" />
<c:choose>
	<c:when test="${itemAttachment.hasAttachment}">
		<ul>
			<li>
				<c:forEach var="file" items="${topic.message.attachments}">
					<c:set var="downloadURL">
						<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
					</c:set>
					<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
						<c:out value="${file.fileName}" /> 
					</a>
				</c:forEach>	
				
				<a href="#" onclick="removeItemAttachment()" class="btn btn-default btn-xs"> 
					<fmt:message key="label.delete" /> 
				</a>
				
				<img src="${ctxPath}/images/indicator.gif" style="display:none"	id="itemAttachmentArea_Busy" class="space-left" />
				
			</li>
		</ul>

	</c:when>
	<c:otherwise>
		<input type="file" name="attachmentFile" class="form-control" />
	</c:otherwise>
</c:choose>
