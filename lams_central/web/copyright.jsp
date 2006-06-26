<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page import="org.lamsfoundation.lams.security.JspRedirectStrategy" %>
<%@ page import="org.lamsfoundation.lams.web.util.HttpSessionManager" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title><fmt:message key="title.lams"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<lams:css/>
</head>
<body>
	<table width="95%" height="95%" border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td width="100%" align="center">
			<table bgcolor="#ffffff" width="100%" height="100%" cellspacing="5" cellpadding="5" align="center">
				<tr>
					<td align="left">
						<H2><fmt:message key="msg.LAMS.copyright.statement.1"/></H2>
					</td>
				</tr>
				<tr>
					<td align="left">
						<fmt:message key="msg.LAMS.copyright.statement.2"/>
					</td>
				</tr>
				<tr>
					<td align="left">
						<fmt:message key="msg.LAMS.copyright.statement.3"/>
					</td>
				</tr>
				<tr>
					<td align="left">
						<A HREF="<fmt:message key="msg.LAMS.copyright.statement.url"/>" 
						onClick="window.open('<fmt:message key="msg.LAMS.copyright.statement.url"/>','copyright2','resizable,width=650,height=650,scrollbars');return false;">
							&copy; <fmt:message key="msg.LAMS.copyright.statement.url"/>
						</a>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	</table>
</body>	
</html>

