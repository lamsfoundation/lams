<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"
	scope="request" />
<input type="hidden" name="hasAttachment"
	value="${itemAttachment.hasAttachment}" />
<c:choose>
	<c:when test="${itemAttachment.hasAttachment}">
		<ul>
			<li>
				<c:out value="${itemAttachment.attachmentName}" />
				
				<a href="#" onclick="removeItemAttachment()" class="space-left"> <fmt:message
				key="label.delete" /> </a>
				
				<img src="${ctxPath}/images/indicator.gif" style="display:none"
					id="itemAttachmentArea_Busy" class="space-left" />
				
			</li>
		</ul>

	</c:when>
	<c:otherwise>
		<input type="file" name="attachmentFile" />
	</c:otherwise>
</c:choose>
