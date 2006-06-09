<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

	
	<!-- HTTP 1.1 -->
	<meta http-equiv="Cache-Control" content="no-store"/>
	<!-- HTTP 1.0 -->
	<meta http-equiv="Pragma" content="no-cache"/>
	<!-- Prevents caching at the Proxy Server -->
	<meta http-equiv="Expires" content="0"/>
	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 

 	<!-- ********************  CSS ********************** -->
	<lams:css />

	<link href="<html:rewrite page='/includes/css/tool_custom.css'/>" rel="stylesheet" type="text/css">
	<link href="<html:rewrite page='/includes/css/rsrc.css'/>" rel="stylesheet" type="text/css">

 	<!-- ********************  javascript ********************** -->
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/rsrccommon.js'/>"></script>
	<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>    
	<script type="text/javascript" src="${tool}includes/javascript/xmlrequest.js"></script>

	<link href="${tool}css/fckeditor_style.css" rel="stylesheet" type="text/css">
   	<script type="text/javascript" src="${tool}includes/javascript/fckcontroller.js"></script>
    <script type="text/javascript" src="${lams}fckeditor/fckeditor.js"></script>
