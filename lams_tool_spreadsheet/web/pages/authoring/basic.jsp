<%@ include file="/common/taglibs.jsp"%>
<form:hidden path="spreadsheet.code" id="spreadsheet-code"/>
		
<!-- Basic Tab Content -->
<div class="form-group">
    <label for="spreadsheet.title"><fmt:message key="label.authoring.basic.title"/></label>
    <input type="text" name="spreadsheet.title" value="${spreadsheetForm.spreadsheet.title}" class="form-control"/>
</div>
<div class="form-group">
    <label for="spreadsheet.instructions"><fmt:message key="label.authoring.basic.instruction" /></label>
    <lams:CKEditor id="spreadsheet.instructions" value="${spreadsheetForm.spreadsheet.instructions}" contentFolderID="${spreadsheetForm.contentFolderID}"></lams:CKEditor>
</div>
<div class="form-group">
    <label for="spreadsheet.title"><fmt:message key="label.authoring.basic.spreadsheet"/></label>
	<iframe id="externalSpreadsheet" name="externalSpreadsheet" src="<lams:WebAppURL/>includes/javascript/simple_spreadsheet/spreadsheet_offline.html?lang=<%=request.getLocale().getLanguage()%>"
		style="width:99%;" frameborder="no" height="385px" scrolling="no">
	</iframe>
</div>


