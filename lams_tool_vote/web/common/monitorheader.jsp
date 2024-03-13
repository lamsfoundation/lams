<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams" scope="request"><lams:LAMSURL /></c:set>
<c:set var="tool" scope="request"><lams:WebAppURL /></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>

<!-- ********************  CSS ********************** -->
<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap5.css">
<link type="text/css" href="${lams}css/jquery.tablesorter.pager5.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui-bootstrap-theme5.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/defaultHTML_chart.css" rel="stylesheet">

<!-- ********************  javascript ********************** -->
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>  
<script type="text/javascript" src="${lams}includes/javascript/d3.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/chart.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
<lams:JSImport src="includes/javascript/portrait5.js" />
