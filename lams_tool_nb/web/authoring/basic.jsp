<%@ include file="/includes/taglibs.jsp"%>
<!-- Basic Tab Content  -->
<div class="form-group">
    <label for="title"><fmt:message key="basic.title"/></label>
    <input type="text" name="title" class="form-control"/>
</div>

<div class="form-group">
    <label for="title"><fmt:message key="basic.content"/></label>
	<lams:CKEditor id="basicContent" value="${NbAuthoringForm.basicContent}" 
			contentFolderID="${NbAuthoringForm.contentFolderID}" height="400"></lams:CKEditor>
</div>
