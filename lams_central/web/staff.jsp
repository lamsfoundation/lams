<%@ taglib uri="tags-lams" prefix="lams" %>

<HTML>
<HEAD>
<meta http-equiv=Content-Type content="text/html; charset=iso-8859-1">

<%
String protocol = request.getProtocol();
if(protocol.startsWith("HTTPS")){
	protocol = "https://";
}else{
	protocol = "http://";
}
String pathToRoot = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";

%>

<TITLE>Staff:: LAMS</TITLE>
</HEAD>
<BODY bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onUnload="endPreviewSession()">

<p>The Staff page is yet to be written. Use the <A HREF='javascript:window.open("monitoring/dummy.do");'>dummy monitoring page</a> for now.</p>


</BODY>
</HTML>
