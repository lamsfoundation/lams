<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

<html:form action="/tool/nb/authoring" target="_self" >
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
					<td class="formlabel"><fmt:message key="basic.title" /></td>
					<td class="formcontrol">
							<FCK:editor id="richTextTitle" basePath="/lams/fckEditor/"
								height="150"
								width="85%">
								<c:out value="${NbAuthoringForm.title}" escapeXml="false" />
							</FCK:editor>
					</td>
				</tr>
				<tr>
					<td class="formlabel"><fmt:message key="basic.content" /></td>
					<td class="formcontrol">
						<FCK:editor id="richTextContent" basePath="/lams/fckEditor/"
								width="85%"
								height="400">
							<c:out value="${NbAuthoringForm.content}" escapeXml="false" />
						</FCK:editor>
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
	<td align="center"><html:button property="cancel" onclick="window.close()" styleClass="button"><fmt:message key="button.cancel"/></html:button>
	<html:submit property="method" styleClass="button"><fmt:message key="button.ok" /></html:submit>
	</td>
</tr>
</table>
</div>


				<html:hidden property="content" />
				<html:hidden property="title" />
				
	
</html:form>		
