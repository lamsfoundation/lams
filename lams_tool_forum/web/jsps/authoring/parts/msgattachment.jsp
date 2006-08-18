<%@ include file="/common/taglibs.jsp" %>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<input type="hidden" name="hasAttachment" value="${itemAttachment.hasAttachment}"/>
<c:choose>
	<c:when test="${itemAttachment.hasAttachment}">
		<table border="0" style="align:left;width:400px">
		<tr><td>
			<c:out value="${itemAttachment.attachmentName}" />
		</td>
		<td>
			<a href="#" onclick="removeItemAttachment(${topicIndex})" class="button">
				<fmt:message key="label.delete" />
			</a>	
		</td>
		<td>
			<img src="${ctxPath}/images/indicator.gif" style="display:none" id="itemAttachmentArea_Busy" />
		</td>
		</tr>
		</table>
	</c:when>
	<c:otherwise>
		<input type="file" name="attachmentFile" size="55"  /> 
	</c:otherwise>
</c:choose>
