<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />

<input type="hidden" name="hasFile" value="${not empty CommonCartridgeItemForm and CommonCartridgeItemForm.hasFile}" />

<c:choose>
	<c:when test="${itemAttachment.hasFile}">
		<table border="0" style="align:left;width:400px">
			<tr>
				<td>
					<c:out value="${itemAttachment.fileName}" />
				</td>
				<td>
					<a href="#" onclick="removeItemAttachment(${itemAttachment.itemIndex})"	class="button"> 
						<fmt:message key="label.delete" />
					</a>
				</td>
			</tr>
		</table>
	</c:when>
	
	<c:otherwise>
		<lams:FileUpload fileFieldname="file" fileInputMessageKey="label.authoring.basic.resource.file" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
	</c:otherwise>
</c:choose>
<lams:WaitingSpinner  id="itemAttachmentArea_Busy"/>
