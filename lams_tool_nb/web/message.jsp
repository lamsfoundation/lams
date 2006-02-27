<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>

<div id="datatablecontainer">
<table width="100%">
<logic:messagesPresent message="true">
<html:messages id="message" message="true">	
<tr>
	<td>
		<bean:write name="message"/>
	</td>	
</tr>
</html:messages>
</logic:messagesPresent>

<tr>
	<td align="right">
		<html:button property="closeWindow" onclick="window.close()" styleClass="button">
			<fmt:message key="button.next"/>
		</html:button>
	</td>
</tr>
</table>

</div>
<BR>
<hr>
