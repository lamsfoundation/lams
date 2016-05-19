<%@ include file="/common/taglibs.jsp"%>
<c:choose>
	<c:when test="${itemAttachment.hasFile}">
		<c:out value="${itemAttachment.fileName}" /> &nbsp;
		<a href="#" onclick="removeItemAttachment(${itemAttachment.itemIndex})" class="btn btn-default btn-sm"> <fmt:message key="label.delete" /> </a>
	</c:when>
	<c:otherwise>
		<input type="file" name="file" class="form-control form-control-inline" style="display:inline"/>
	</c:otherwise>
</c:choose>
<input type="hidden" name="hasFile" value="${itemAttachment.hasFile}" />
