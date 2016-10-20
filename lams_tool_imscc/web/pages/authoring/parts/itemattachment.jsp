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
		<input type="file" name="file" />
	</c:otherwise>
</c:choose>
