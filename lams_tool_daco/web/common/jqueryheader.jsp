<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams" scope="request"><lams:LAMSURL/></c:set>
<c:set var="tool" scope="request"><lams:WebAppURL/></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

 <!-- ********************  CSS ********************** -->
<link href="<c:url value='/includes/css/daco.css'/>" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap5.css">
<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager5.css"> 

<!-- ********************  javascript ********************** -->
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script> 
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script> 
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script> 
<lams:JSImport src="includes/javascript/dacoCommon.js" relative="true" />