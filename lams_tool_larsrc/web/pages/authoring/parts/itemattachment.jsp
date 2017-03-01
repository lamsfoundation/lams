<%@ include file="/common/taglibs.jsp"%>

<c:choose>
	<c:when test="${itemAttachment.hasFile}">
		<c:out value="${itemAttachment.fileName}" /> &nbsp;
		<a href="#nogo" onclick="removeItemAttachment(${itemAttachment.itemIndex})" class="btn btn-danger btn-sm"><i class="fa fa-trash-o"></i> <fmt:message key="label.delete" /> </a>
	</c:when>
	<c:otherwise>
		<lams:FileUpload fileFieldname="file"  maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/>
		
	</c:otherwise>
</c:choose>
<input type="hidden" name="hasFile" value="${itemAttachment.hasFile}" />
