<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
<input type="hidden" name="hasFile" value="${itemAttachment.hasFile}" />
<c:choose>
	<c:when test="${itemAttachment.hasFile}">
		<table border="0" style="align:left;width:400px">
			<tr>
				<td>
					<c:out value="${itemAttachment.fileName}" />
				</td>
				
			<td width="3%" align="left">
				<c:set var="viewURL">
					<html:rewrite page="/download/?uuid=${itemAttachment.fileUuid}&preferDownload=false" />
				</c:set>
				<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')" class="button"> <fmt:message key="label.view" /> </a> &nbsp;
			</td>
				
				
				<td>
					<a href="#"	onclick="removeItemAttachment(${itemAttachment.imageIndex})" class="button">
						<fmt:message key="label.delete" /> 
					</a>
				</td>
				<td>
					<img src="${ctxPath}/includes/images/indicator.gif"	style="display:none" id="itemAttachmentArea_Busy" />
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<input type="file" name="file" />
	</c:otherwise>
</c:choose>
<!-- 
<c:choose>
	<c:when test="${itemAttachment.hasFile}">
			<iframe
				id="externalSpreadsheet" name="externalSpreadsheet" src="<html:rewrite page='/download/?uuid=${itemAttachment.fileUuid}&preferDownload=false' />"
				style="width:99%;" frameborder="no" height="385px"
				scrolling="no">
			</iframe>
	</c:when>
</c:choose>
-->
