<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

<head>
	<title>
		<bean:message key="appName" />
	</title>
	<lams:css />

	<!-- HTTP 1.1 -->
	<meta http-equiv="Cache-Control" content="no-store"/>
	<!-- HTTP 1.0 -->
	<meta http-equiv="Pragma" content="no-cache"/>
	<!-- Prevents caching at the Proxy Server -->
	<meta http-equiv="Expires" content="0"/>
	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 

    <link href="<html:rewrite page='/includes/css/aqua.css'/>" rel="stylesheet" type="text/css">
	<!-- this is the custom CSS for hte tool -->
	<link href="<html:rewrite page='/includes/css/tool_custom.css'/>" rel="stylesheet" type="text/css">
    <link href="<html:rewrite page='/includes/css/fckeditor_style.css'/>" rel="stylesheet" type="text/css">

 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
    <script type="text/javascript" src="${lams}fckeditor/fckeditor.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	
    <script type="text/javascript" src="<html:rewrite page='/includes/javascript/fckcontroller.js'/>"></script>
    <script type="text/javascript" src="<html:rewrite page='/includes/javascript/tabcontroller.js'/>"></script>    
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/prototype.js'/>"></script>


</head>

