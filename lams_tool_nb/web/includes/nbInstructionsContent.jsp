<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#FFFFFF">


	<tr>
		<td colspan="2"><fmt:message key="instructions.onlineInstructions" /></td>
	</tr>
	<tr>
		<td colspan="2">
			<html:textarea property="onlineInstructions" rows="10" cols="45" />
		</td>
	</tr>
			
	<tr>
		<td colspan="2"><fmt:message key="instructions.offlineInstructions" /></td>
	</tr>
	<tr>
		<td colspan="2"><html:textarea property="offlineInstructions" rows="10" cols="45" /></td>
	</tr>
	
	<tr>
		<td colspan="2" align="right"><html:submit property="done"><fmt:message key="button.done"/></html:submit>
	</tr>

</table>