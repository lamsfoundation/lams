<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<!DOCTYPE html>

<lams:html>
<lams:head>
	<title><fmt:message key="title.lams"/></title>
	<lams:css/>
</lams:head>
<body class="stripes">

	<c:set var="title"><fmt:message key="msg.LAMS.copyright.statement.1"/></c:set>
	<lams:Page type="admin" title="${title}">
	
		<p><fmt:message key="msg.LAMS.copyright.statement.2"/></p>
		<p><fmt:message key="msg.LAMS.copyright.statement.3"/></p>
		<p><A HREF="http://www.gnu.org/licenses/gpl2.txt"  target='copyright2'
				onClick="window.open('http://www.gnu.org/licenses/gpl2.txt','copyright2','resizable,width=650,height=650,scrollbars');return false;">
					http://www.gnu.org/licenses/gpl2.txt
				</a>
		</p>
		<div id="footer"></div>
	</lams:Page>

</body>	
</lams:html>

