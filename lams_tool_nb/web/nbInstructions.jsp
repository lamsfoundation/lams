<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>

<table width="40%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#FFFFFF">
<html:form action="/tool/nb/authoring" target="_self" >
	<tr>
		<td width="33%">
			<html:submit property="method">
				<fmt:message key="button.basic" />
			</html:submit>
		</td>
		<td width="33%">
			<html:submit property="method">
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
						<html:textarea property="onlineInstructions" rows="10" cols="55" />
					</td>
				</tr>
						
				<tr>
					<td colspan="2"><fmt:message key="instructions.offlineInstructions" /></td>
				</tr>
				<tr>
					<td colspan="2"><html:textarea property="offlineInstructions" rows="10" cols="55" /></td>
				</tr>
				
				<tr>
					<td colspan="2" align="right"><html:submit property="method"><fmt:message key="button.done"/></html:submit>
				</tr>
			</table>
		</td>
	</tr>
</html:form>
</table>