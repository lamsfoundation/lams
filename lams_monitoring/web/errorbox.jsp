<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ page import="org.apache.struts.Globals" %>

<logic:present name="<%=Globals.ERROR_KEY%>">
<tr>
	<td width="10%"  align="right" >
		<img src="<lams:WebAppURL/>images/error.jpg" alt="<fmt:message key="error.title"/>"/>
	</td>
	<td width="90%" valign="center" class="body" colspan="2">
		<html:errors/>
	</td>
</tr>
</logic:present>