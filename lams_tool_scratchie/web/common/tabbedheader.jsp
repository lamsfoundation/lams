<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

	<!-- ********************  CSS ********************** -->
<link href="<lams:WebAppURL/>includes/css/scratchie.css" rel="stylesheet" type="text/css">
<lams:css/>

	<!-- ********************  javascript ********************** -->
<lams:JSImport src="includes/javascript/common.js" />
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/thickbox.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>