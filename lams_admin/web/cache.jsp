<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>

<%
String protocol = request.getProtocol();
if(protocol.startsWith("HTTPS")){
	protocol = "https://";
}else{
	protocol = "http://";
}
String pathToRoot = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
String pathToShare = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/..";
%>
<HTML>
<HEAD>
<meta http-equiv=Content-Type content="text/html; charset=iso-8859-1">
<TITLE><bean:message key="cache.title"/></TITLE>

<link href="<%=pathToShare%>/css/aqua.css" rel="stylesheet" type="text/css">

</HEAD>
<BODY bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onUnload="endPreviewSession()">

<H1><bean:message key="cache.title"/></H1>

<p><bean:message key="cache.explanation1"/></p>
<p><bean:message key="cache.explanation2"/></p>
<p><bean:message key="cache.explanation3"/></p>
<UL>
<!-- cache is a Map, where each key is a node and the value is a set containing the child nodes of this key. Each child node -->
<!-- may or may not have its own value in the map. -->

<p><bean:message key="cache.entries.title"/></p>

<c:forEach var="itemEntry" items="${cache}">

<LI><c:out value="${itemEntry.key}"/> <A HREF="cache.do?method=remove&node=<c:out value="${itemEntry.key}"/>"><bean:message key="cache.button.remove"/></a><BR>
Children:
	<c:forEach var="child" items="${itemEntry.value}">
		<c:out value="${child}"/><BR>
	</c:forEach>

</c:forEach>
</UL>
</BODY>
</HTML>
