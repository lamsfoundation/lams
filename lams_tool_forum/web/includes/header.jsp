<%@ include file="/includes/taglibs.jsp" %>
<%
String protocol = request.getProtocol();
if(protocol.startsWith("HTTPS")){
	protocol = "https://";
}else{
	protocol = "http://";
}
String pathToRoot = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
String pathToShare = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/../../../..";
%>
<head>
<title><bean:message key="appName" /></title>
<link href='<html:rewrite page="/includes/style.jsp" />' rel="stylesheet" type="text/css" />	
<script src='<%=pathToShare%>/common.jsp'></script>
<script src='<html:rewrite page="/includes/scripts.jsp" />'></script>
<script src='<html:rewrite page="/includes/validator.jsp" />'></script>
</head>

