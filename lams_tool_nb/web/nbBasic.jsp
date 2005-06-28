<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

<table width="45%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#FFFFFF">
<html:form action="/tool/nb/authoring" target="_self" >
	<tr>
		<td width="33%">
			<html:submit property="method">
				<fmt:message key="button.basic" />
			</html:submit>
		</td>
		<td width="33%">
			<html:submit property="method" disabled="true">
				<fmt:message key="button.advanced" />
			</html:submit>
		</td>
		<td width="33%">
			<html:submit property="method">
				<fmt:message key="button.instructions" />
			</html:submit>
		</td>
	</tr>
	<tr>
		<td colspan="3">
			<hr />
		</td>
	</tr>
	<tr>
		<td colspan="3">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
				<tr>
					<td><fmt:message key="basic.title" /></td>
					<td><html:text property="title" size="50"/></td>
				
				</tr>
			<tr>
					<td colspan="2"><fmt:message key="basic.content" /></td>
				
				</tr> 
		 		<tr>
					<td colspan="2">
						<FCK:editor id="richTextContent" basePath="/lams/fckEditor/"
							imageBrowserURL="/lams/fckEditor/editor/filemanager/browser/default/browser.html?Type=Image&Connector=connectors/jsp/connector"
							linkBrowserURL="/lams/fckEditor/editor/filemanager/browser/default/browser.html?Connector=connectors/jsp/connector"
							height="400"
						><c:out value="${NbAuthoringForm.content}" escapeXml="false" />
					
						</FCK:editor>
					</td>
				</tr>
				<html:hidden property="content" />
				<tr>
					<td><html:button property="cancel" onclick="window.close()"><fmt:message key="button.cancel"/></html:button></td>
				<!--	<td><html:submit property="method" onclick="window.close()"><fmt:message key="button.cancel"/></html:submit></td> -->
					<td><html:submit property="method"><fmt:message key="button.ok" /></html:submit></td>
				</tr>
			</table>
		</td>
	</tr>
	
</html:form>		
</table>