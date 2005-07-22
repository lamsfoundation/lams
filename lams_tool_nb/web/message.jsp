<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>

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
