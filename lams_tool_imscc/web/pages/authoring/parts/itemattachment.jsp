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
				<td>
					<a href="#" onclick="removeItemAttachment(${itemAttachment.itemIndex})"	class="button"> 
						<fmt:message key="label.delete" />
					</a>
				</td>
				<td>
					<i class="fa fa-refresh fa-spin fa-1x fa-fw" style="display:none" id="itemAttachmentArea_Busy"></i>
				</td>
			</tr>
		</table>
	</c:when>
	
	<c:otherwise>
		<div class="input-group" id="addfile">
			<input type="file" name="file" id="file" />
		</div>
		<p class="help-block"><fmt:message key="label.upload.info"><fmt:param>${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}</fmt:param></fmt:message></p>					
		<div id="file-error-msg" class="text-danger" style="display:none"></div>			
	</c:otherwise>
</c:choose>
