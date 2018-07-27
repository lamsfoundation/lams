<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
<html:hidden property="spreadsheet.code" styleId="spreadsheet-code"/>
		
<!-- Basic Tab Content -->
<div class="form-group">
    <label for="spreadsheet.title"><fmt:message key="label.authoring.basic.title"/></label>
    <html:text property="spreadsheet.title" styleClass="form-control"></html:text>
</div>
<div class="form-group">
    <label for="spreadsheet.instructions"><fmt:message key="label.authoring.basic.instruction" /></label>
    <lams:CKEditor id="spreadsheet.instructions" value="${formBean.spreadsheet.instructions}" contentFolderID="${formBean.contentFolderID}"></lams:CKEditor>
</div>
<div class="form-group">
    <label for="spreadsheet.title"><fmt:message key="label.authoring.basic.spreadsheet"/></label>
	<iframe id="externalSpreadsheet" name="externalSpreadsheet" src="<html:rewrite page='/includes/javascript/simple_spreadsheet/spreadsheet_offline.html'/>?lang=<%=request.getLocale().getLanguage()%>"
		style="width:99%;" frameborder="no" height="385px" scrolling="no">
	</iframe>
</div>


