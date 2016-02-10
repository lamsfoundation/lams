<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

 <!-- ********************  CSS ********************** -->
<link href="<html:rewrite page='/includes/css/spreadsheet.css'/>" rel="stylesheet" type="text/css">
<lams:css style="main"/>

<!-- ********************  javascript ********************** -->
<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
<script type="text/javascript" src="<html:rewrite page='/includes/javascript/spreadsheetcommon.js'/>"></script>
<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>    

	
