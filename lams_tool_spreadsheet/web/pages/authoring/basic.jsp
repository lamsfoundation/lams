<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
<html:hidden property="spreadsheet.code" styleId="spreadsheet.code"/>
		
<!-- Basic Tab Content -->
<table>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.title"></fmt:message>
			</div>
			<html:text property="spreadsheet.title" style="width: 99%;"></html:text>
		</td>
	</tr>
	
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.instruction"></fmt:message>
			</div>
			<lams:FCKEditor id="spreadsheet.instructions"
				value="${formBean.spreadsheet.instructions}"
				contentFolderID="${formBean.contentFolderID}"></lams:FCKEditor>
		</td>
		
	</tr>

	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.spreadsheet"></fmt:message>
			</div>

			<iframe
				id="externalSpreadsheet" name="externalSpreadsheet" src="<html:rewrite page='/includes/javascript/simple_spreadsheet/spreadsheet_offline.html'/>?lang=<%=request.getLocale().getLanguage()%>"
				style="width:99%;" frameborder="no" height="385px"
				scrolling="no">
			</iframe>
		</td>			
	</tr>	
	
</table>



