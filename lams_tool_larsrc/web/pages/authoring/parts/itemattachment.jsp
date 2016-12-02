<%@ include file="/common/taglibs.jsp"%>
<c:choose>
	<c:when test="${itemAttachment.hasFile}">
		<c:out value="${itemAttachment.fileName}" /> &nbsp;
		<a href="#nogo" onclick="removeItemAttachment(${itemAttachment.itemIndex})" class="btn btn-danger btn-sm"><i class="fa fa-trash-o"></i> <fmt:message key="label.delete" /> </a>
	</c:when>
	<c:otherwise>

		<div class="input-group">
	    <span class="input-group-btn" style="font-size:inherit;">
				<button id="fileButtonBrowse" type="button" class="btn btn-sm btn-default">
					<i class="fa fa-upload"></i> <fmt:message key="label.authoring.basic.resource.file.input"/>
				</button>
			</span>
			<input type="file" id="fileSelector" name="file" style="display:none"> 
			<input type="text" id="fileInputName" style="display:none;" disabled="disabled" placeholder="File not selected" class="form-control input-sm">
		</div>

		<script type="text/javascript">
			// Fake file upload
			document.getElementById('fileButtonBrowse').addEventListener('click', function() {
				document.getElementById('fileSelector').click();
			});
			
			document.getElementById('fileSelector').addEventListener('change', function() {
				$('#fileInputName').show();
				document.getElementById('fileInputName').value = this.value.replace(/^.*\\/, "");
				
			});
		</script>    

		
	</c:otherwise>
</c:choose>
<input type="hidden" name="hasFile" value="${itemAttachment.hasFile}" />
