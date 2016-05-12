<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="spreadsheetURL"><html:rewrite page="/includes/javascript/simple_spreadsheet/spreadsheet_offline.html"/>?lang=${pageContext.response.locale.language}</c:set>

<!-- ********************  CSS ********************** -->
<link href="<html:rewrite page='/includes/css/spreadsheet.css'/>" rel="stylesheet" type="text/css">
<lams:css />

<!-- ********************  javascript ********************** -->
<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
<script type="text/javascript" src="<html:rewrite page='/includes/javascript/spreadsheetcommon.js'/>"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>
	

	
