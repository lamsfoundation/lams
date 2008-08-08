<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"
	scope="request" />
<input type="hidden" name="hasFile" value="${questionAttachment.hasFile}" />
<c:choose>
	<c:when test="${questionAttachment.hasFile}">
		<table border="0" style="align:left;width:400px">
			<tr>
				<td>
					<c:out value="${questionAttachment.fileName}" />
				</td>
				<td>
					<a href="#"
						onclick="removeQuestionAttachment(${questionAttachment.questionIndex})"
						class="button"> <fmt:message key="label.common.delete" /> </a>
				</td>
				<td>
					<img src="${ctxPath}/includes/images/indicator.gif"
						style="display:none" id="questionAttachmentArea_Busy" />
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<input type="file" name="file" />
	</c:otherwise>
</c:choose>
