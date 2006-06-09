<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<table width="80%" algin="center" valign="middle">
<tr>
	<td width="30%" align="left"><img src="<lams:LAMSURL/>/images/error.jpg" /></td>
	<td></td>
</tr>
<tr>
	<td colspan=2 align=center>
		<html:errors/>
	</td>
</tr>
</table>