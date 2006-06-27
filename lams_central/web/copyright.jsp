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
	<div id="page">	
		<h1 class="no-tabs-below">
			<fmt:message key="msg.LAMS.copyright.statement.1"/>
		</h1>
		<div id="header-no-tabs"></div>
		<div id="content">
			<p><fmt:message key="msg.LAMS.copyright.statement.2"/></p>
			<p><fmt:message key="msg.LAMS.copyright.statement.3"/></p>
			<p><A HREF="http://www.gnu.org/licenses/gpl.txt"  target='copyright2'
					onClick="window.open('http://www.gnu.org/licenses/gpl.txt','copyright2','resizable,width=650,height=650,scrollbars');return false;">
						http://www.gnu.org/licenses/gpl.txt
					</a>
			</p>
		</div>
		<div id="footer"></div>
	</div>

</body>	
</html>

