<%@ include file="/common/taglibs.jsp" %>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<input type="hidden" name="hasFile" value="${itemAttachment.hasFile}"/>
<c:choose>
	<c:when test="${itemAttachment.hasFile}">
		<table border="0">
		<tr><td>
			<c:out value="${itemAttachment.fileName}" />
		</td>
		<td>
			<a href="#" onclick="removeItemAttachment(${itemAttachment.itemIndex})" style="button">
				<fmt:message key="label.delete" />
			</a>	
		</td>
		<td>
			<img src="${ctxPath}/includes/images/indicator.gif" style="display:none" id="itemAttachmentArea_Busy" />
		</td>
		</tr>
		</table>
	</c:when>
	<c:otherwise>
		<input type="file" name="file" size="55"  /> 
	</c:otherwise>
</c:choose>
