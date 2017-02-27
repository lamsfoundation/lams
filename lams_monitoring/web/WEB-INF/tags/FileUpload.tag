<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<%@ attribute name="fileFieldname" required="false" rtexprvalue="true"%>
<%@ attribute name="fileInputNameFieldname" required="false" rtexprvalue="true"%>
<%@ attribute name="fileInputMessageKey" required="false" rtexprvalue="true"%>
<%@ attribute name="uploadInfoMessageKey" required="false" rtexprvalue="true"%>
<%@ attribute name="maxFileSize" required="true" rtexprvalue="true"%> 
<%@ attribute name="tabindex" required="false" rtexprvalue="true"%>
			
<c:if test="${empty fileFieldname}">
	<c:set var="fileFieldname" value="fileSelector" />
</c:if>

<c:if test="${empty fileInputNameFieldname}">
	<c:set var="fileInputNameFieldname" value="fileInputName" />
</c:if>

<c:if test="${empty fileInputMessageKey}">
	<c:set var="fileInputMessageKey" value="label.authoring.basic.resource.file.input" />
</c:if>

<c:if test="${empty uploadInfoMessageKey}">
	<c:set var="uploadInfoMessageKey" value="label.upload.info" />
</c:if>

<c:if test="${not empty tabindex}">
	<c:set var="tabindexString" value="tabindex='${tabindex}'" />
</c:if>

<div class="input-group" id="addfile">
	<span class="input-group-btn" style="font-size:inherit"> <%-- font-size:inherit needed for Share Resources authoring or the button is too small --%>
		<button id="fileButtonBrowse" ${tabindexString} type="button" class="btn btn-default">
			<i class="fa fa-upload"></i> <fmt:message key="${fileInputMessageKey}"/>
		</button>
	</span>
	<input type="file" id="${fileFieldname}" name="${fileFieldname}" style="display:none" class="fileUpload"> 
	<input type="text" id="${fileInputNameFieldname}" style="display:none" disabled="disabled" placeholder="File not selected" class="form-control file-input-name">
</div>
<p class="help-block"><fmt:message key="${uploadInfoMessageKey}"><fmt:param>${maxFileSize}</fmt:param></fmt:message></p>					
<div id="file-error-msg" class="text-danger" style="display:none"></div>			

<script type="text/javascript">
	// Fake file upload
	document.getElementById('fileButtonBrowse').addEventListener('click', function() {
		document.getElementById('${fileFieldname}').click();
	});
	
	document.getElementById('${fileFieldname}').addEventListener('change', function() {
		$('#${fileInputNameFieldname}').show();
		document.getElementById('${fileInputNameFieldname}').value = this.value.replace(/^.*\\/, "");
	});
</script>  

