<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

 <!-- ********************  CSS ********************** -->
<link href="<lams:WebAppURL/>includes/css/taskList.css" rel="stylesheet" type="text/css">
<lams:css/>
<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme5.css" rel="stylesheet">
<link type="text/css" href="${lams}css/free.ui.jqgrid.custom.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">

<!-- ********************  javascript ********************** -->
<lams:JSImport src="includes/javascript/common.js" />
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>
<lams:JSImport src="includes/javascript/portrait5.js" />