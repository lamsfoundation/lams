<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page import="org.lamsfoundation.lams.security.JspRedirectStrategy" %>
<%@ page import="org.lamsfoundation.lams.web.util.HttpSessionManager" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<!DOCTYPE html>
		
<lams:html>
<lams:head>
	<title><fmt:message key="title.lams"/></title>
	<lams:css  style="core"/>
</lams:head>
<body class="stripes">
	<div id="page">	
		<h1 class="no-tabs-below">
			<fmt:message key="msg.LAMS.copyright.statement.1"/>
		</h1>
		<div id="header"></div>
		<div id="content">
			<p><fmt:message key="msg.LAMS.copyright.statement.2"/></p>
			<p><fmt:message key="msg.LAMS.copyright.statement.3"/></p>
			<p><A HREF="http://www.gnu.org/licenses/gpl2.txt"  target='copyright2'
					onClick="window.open('http://www.gnu.org/licenses/gpl2.txt','copyright2','resizable,width=650,height=650,scrollbars');return false;">
						http://www.gnu.org/licenses/gpl2.txt
					</a>
			</p>
		</div>
		<div id="footer"></div>
	</div>

</body>	
</lams:html>

