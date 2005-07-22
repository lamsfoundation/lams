<%@ taglib uri="/WEB-INF/struts/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

<html:form action="/authoring" target="_self" enctype="multipart/form-data" >
<div id="datatablecontainer">
<table width="100%" align="center">
<tr>
	<td align="center">
	<html:submit property="method" styleClass="button">
	
				<fmt:message key="button.basic" />
		</html:submit>
	
		<html:submit property="method" disabled="true" styleClass="button">
			<fmt:message key="button.advanced" />
		</html:submit>
	
		<html:submit property="method" styleClass="button">
			<fmt:message key="button.instructions" />
		</html:submit>
	</td>
</tr>
</table>
</div>

						
<div id="formtablecontainer">
<table class="forms">
	<tr>
		<td>
			<table width="65%" align="center">
				<tr>
					<td class="formlabel"><fmt:message key="instructions.onlineInstructions" /></td>
					<td class="formcontrol">
							<FCK:editor id="richTextOnlineInstructions" basePath="/lams/fckEditor/"
								height="200"
								width="85%">
								<c:out value="${NbAuthoringForm.onlineInstructions}" escapeXml="false" />
							</FCK:editor>
					</td>
				</tr>
				
				<tr>
					<td class="formlabel">
						<fmt:message key="instructions.uploadOnlineInstr" />
					</td>
					<td class="formcontrol">
					</td>						
				</tr>
				<tr>
					<td class="formlabel"><fmt:message key="instructions.offlineInstructions" /></td>
					<td class="formcontrol">
						<FCK:editor id="richTextOfflineInstructions" basePath="/lams/fckEditor/"
								width="85%"
								height="200">
							<c:out value="${NbAuthoringForm.offlineInstructions}" escapeXml="false" />
						</FCK:editor>
					</td>
				</tr>
				<tr>
					<td class="formlabel">
						<fmt:message key="instructions.uploadOfflineInstr" />
					</td>
					<td class="formcontrol">
					</td>						
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>

<div id="datatablecontainer">
<table width="100%" align="center">
<tr>
	<td align="center"><html:submit property="method"><fmt:message key="button.done"/></html:submit>
	</td>
</tr>
</table>
</div>
				<html:hidden property="onlineInstructions" />
				<html:hidden property="offlineInstructions" />
				
</html:form>
