<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

 	<!-- ********************  CSS ********************** -->
	<link href="<c:url value='/includes/css/daco.css'/>" rel="stylesheet" type="text/css">
	<lams:css/>
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap5.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager5.css"> 

 	<!-- ********************  javascript ********************** -->
	<lams:JSImport src="includes/javascript/common.js" />

	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script> 
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script> 
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script> 
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script> 

	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>    
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>		
	
	<lams:JSImport src="includes/javascript/dacoCommon.js" relative="true" />