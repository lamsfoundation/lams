<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<%-- Usually fileFieldname and fileFieldId are the same, but DACO needs them to be different.
 If they can be the same, set  fileFieldname and  fileFieldId will be set to the same. 
 If you need them different, then define both. 
 If you define neither, fileFieldname = fileFieldId = "fileSelector" --%>
<%@ attribute name="fileFieldname" required="false" rtexprvalue="true"%>
<%@ attribute name="fileFieldId" required="false" rtexprvalue="true"%>

<%@ attribute name="fileInputNameFieldname" required="false" rtexprvalue="true"%>
<%@ attribute name="fileInputMessageKey" required="false" rtexprvalue="true"%>

<%-- Set uploadInfoMessageKey to '-' to NOT show the "not exe and max file size" type message. Leave it blank for the default key  label.upload.info --%>
<%@ attribute name="uploadInfoMessageKey" required="false" rtexprvalue="true"%>
<%@ attribute name="maxFileSize" required="true" rtexprvalue="true"%> 
<%@ attribute name="tabindex" required="false" rtexprvalue="true"%>

<%-- Only set if you have more than one file field on the screen (such as DACO) --%>			
<%@ attribute name="errorMsgDiv" required="false" rtexprvalue="true"%>
<%@ attribute name="fileButtonBrowse" required="false" rtexprvalue="true"%>

			
<c:if test="${empty fileFieldname}">
	<c:set var="fileFieldname" value="fileSelector" />
</c:if>

<c:if test="${empty fileFieldId}">
	<c:set var="fileFieldId" value="${fileFieldname}" />
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

<c:if test="${empty errorMsgDiv}">
	<c:set var="errorMsgDiv" value="file-error-msg" />
</c:if>

<c:if test="${empty fileButtonBrowse}">
	<c:set var="fileButtonBrowse" value="fileButtonBrowse" />
</c:if>


<div class="d-inline">
	<button id="${fileButtonBrowse}" ${tabindexString} type="button" class="btn btn-outline-secondary">
		<i class="fa fa-upload"></i> <fmt:message key="${fileInputMessageKey}"/>
	</button>
	<input type="file" id="${fileFieldId}" name="${fileFieldname}" class="fileUpload d-none"> 
	<input type="text" id="${fileInputNameFieldname}" disabled="disabled" placeholder="File not selected" class="form-control d-none form-control-sm w-25 ms-1">
</div>
<c:if test="${uploadInfoMessageKey ne '-'}">
<p class="form-text"><fmt:message key="${uploadInfoMessageKey}"><fmt:param>${maxFileSize}</fmt:param></fmt:message></p>					
</c:if>
<div id="${errorMsgDiv}" class="text-danger" style="display:none"></div>			

<script type="text/javascript">
	// Fake file upload
	document.getElementById('${fileButtonBrowse}').addEventListener('click', function() {
		document.getElementById('${fileFieldId}').click();
	});
	
	document.getElementById('${fileFieldId}').addEventListener('change', function() {
		$('#${fileInputNameFieldname}').removeClass('d-none').addClass("d-inline");
		document.getElementById('${fileInputNameFieldname}').value = this.value.replace(/^.*\\/, "");
	});
</script>  

