<%@ include file="/common/taglibs.jsp"%>

<c:choose>
	<c:when test="${not empty resourceItemForm and resourceItemForm.hasFile}">
		<c:out value="${resourceItemForm.fileName}" /> &nbsp;
		<a href="#nogo" onclick="removeItemAttachment(${resourceItemForm.itemIndex})" class="btn btn-danger btn-sm"><i class="fa fa-trash-o"></i> <fmt:message key="label.delete" /> </a>
	</c:when>
	<c:otherwise>
		<input type="hidden" id="tmpFileUploadId" name="tmpFileUploadId" value="${resourceItemForm.tmpFileUploadId}" />
		<div id="image-upload-area" class="voffset20"></div>
		<script>
			initFileUpload('${resourceItemForm.tmpFileUploadId}', extensionValidation, '<lams:user property="localeLanguage"/>');
		</script>
	</c:otherwise>
</c:choose>

<input type="hidden" name="hasFile" value="${not empty resourceItemForm and resourceItemForm.hasFile}" />