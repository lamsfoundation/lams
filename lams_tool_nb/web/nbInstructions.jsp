<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
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
					<td colspan="2"><fmt:message key="instructions.onlineInstructions" /></td>
				</tr>
				<tr>
					<td colspan="2">
						<FCK:editor id="richTextOnlineInstructions" basePath="/lams/fckEditor/"
							imageBrowserURL="/lams/fckEditor/editor/filemanager/browser/default/browser.html?Type=Image&Connector=connectors/jsp/connector"
							linkBrowserURL="/lams/fckEditor/editor/filemanager/browser/default/browser.html?Connector=connectors/jsp/connector"
							height="300"
						><%=session.getAttribute("richTextOnlineInstructions")%>
						</FCK:editor>
					</td>
				</tr>
		<!--		<tr>
					<td colspan="2">
						<html:textarea property="onlineInstructions" rows="10" cols="55" />
					</td>
				</tr> -->
						
			<tr>
					<td colspan="2"><fmt:message key="instructions.offlineInstructions" /></td>
				</tr>
				
				<tr>
					<td colspan="2">
						<FCK:editor id="richTextOfflineInstructions" basePath="/lams/fckEditor/"
							imageBrowserURL="/lams/fckEditor/editor/filemanager/browser/default/browser.html?Type=Image&Connector=connectors/jsp/connector"
							linkBrowserURL="/lams/fckEditor/editor/filemanager/browser/default/browser.html?Connector=connectors/jsp/connector"
							height="300"
						><%=session.getAttribute("richTextOfflineInstructions")%>
						</FCK:editor>
					</td>
				</tr> 
			<!--	<tr>
					<td colspan="2"><html:textarea property="offlineInstructions" rows="10" cols="55" /></td>
				</tr> -->
				<html:hidden property="onlineInstructions" />
				<html:hidden property="offlineInstructions" />
				<tr>
					<td colspan="2" align="right"><html:submit property="method"><fmt:message key="button.done"/></html:submit>
				</tr>
			</table>
		</td>
	</tr>
	<html:hidden property="title" />
	<html:hidden property="content" />
</html:form>
</table>